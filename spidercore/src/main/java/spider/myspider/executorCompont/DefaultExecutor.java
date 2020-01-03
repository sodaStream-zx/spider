package spider.myspider.executorCompont;

import com.spider.spiderCore.entitys.FetcherTasks;
import com.spider.spiderCore.idbcore.IDbWritor;
import com.spider.spiderCore.iexecutorCom.IExecutor;
import com.spider.spiderCore.iexecutorCom.ISimpleParse;
import com.spider.spiderCore.ihttp.ISendRequest;
import commoncore.customUtils.BeanGainer;
import commoncore.entity.httpEntity.ResponseData;
import commoncore.entity.loadEntity.WebSiteConf;
import commoncore.entity.requestEntity.FetcherTask;
import commoncore.exceptionHandle.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @author Twilight
 * @desc 默认执行器
 * @createTime 2019-01-07-15:35
 */
@Component(value = "iExecutor")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultExecutor implements IExecutor<FetcherTask> {
    private static final Logger log = LoggerFactory.getLogger(DefaultExecutor.class);

    private IDbWritor iDbWritor;
    private ISendRequest<ResponseData> iSendRequest;
    private ISimpleParse<FetcherTasks, ResponseData> iSimpleParse;
    private com.spider.spiderCore.iexecutorCom.IContentNeeded IContentNeeded;
    private com.spider.spiderCore.iexecutorCom.INextLinksFilter INextLinksFilter;
    private WebSiteConf webSiteConf;

    @Autowired
    public DefaultExecutor(IDbWritor iDbWritor, WebSiteConf webSiteConf) {
        this.iDbWritor = iDbWritor;
        this.webSiteConf = webSiteConf;
        log.info("加载默认执行器组件");
        this.iSendRequest = BeanGainer.getBean("defaultRequest", ISendRequest.class);
        this.iSimpleParse = BeanGainer.getBean("defaultSimpleParse", ISimpleParse.class);
        this.IContentNeeded = BeanGainer.getBean("defaultContentPageFilter", com.spider.spiderCore.iexecutorCom.IContentNeeded.class);
        this.INextLinksFilter = BeanGainer.getBean("", com.spider.spiderCore.iexecutorCom.INextLinksFilter.class);
        log.info("网络请求组件:" + iSendRequest
                + "\nisimparse:" + iSimpleParse
                + "\nicontentPageFilter:" + IContentNeeded
                + "\ninextfilter:" + INextLinksFilter);
    }

    /**
     * desc:任务执行
     **/
    @Override
    public void execute(FetcherTask task) throws MyException {
        ResponseData responseData = null;
        try {
            //1.调用请求工具获取响应页面
            responseData = iSendRequest.converterResponsePage(task);
            log.info("done: " + task.briefInfo());
            task.setStatus(FetcherTask.STATUS_DB_SUCCESS);
        } catch (IOException e) {
            log.info("failed: " + task.briefInfo());
            log.error(e.toString());
            task.setStatus(FetcherTask.STATUS_DB_FAILED);
        }
        // 2.写入当前任务到已爬取库
        task.incrExecuteCount(1);
        task.setExecuteTime(System.currentTimeMillis());
        iDbWritor.writeFetchSegment(task);

        //3.传输当前页面到解析模块

        IContentNeeded.getContentPageData(responseData);

        //4.解析响应页面的urls 并过滤
        if (webSiteConf.getDeepPath() > task.getDeepPath()) {
            FetcherTasks next = iSimpleParse.parseLinks(responseData);
            if (next.isEmpty()) {
                log.info("当前页面未解析出后续任务");
            } else {
                //5.过滤解析的后续任务
                if (INextLinksFilter != null) {
                    next = INextLinksFilter.filter(next);
                }
                //6.写入解析的后续任务，
                iDbWritor.writeParseSegment(next);
            }
        }
    }

    @Override
    public String toString() {
        return "DefaultExecutor{" +
                "iDbWritor=" + iDbWritor +
                ", iSendRequest=" + iSendRequest +
                ", iSimpleParse=" + iSimpleParse +
                ", IContentNeeded=" + IContentNeeded +
                ", INextLinksFilter=" + INextLinksFilter +
                '}';
    }
}
