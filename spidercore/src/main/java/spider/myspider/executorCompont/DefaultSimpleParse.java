package spider.myspider.executorCompont;

import com.spider.spiderCore.entitys.FetcherTasks;
import com.spider.spiderCore.entitys.Links;
import com.spider.spiderCore.entitys.RegexRule;
import com.spider.spiderCore.iexecutorCom.ISimpleParse;
import commoncore.entity.httpEntity.ResponseData;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Twilight
 * @desc 后续任务解析器
 * @createTime 2019-01-07-15:34
 */
@Component(value = "defaultSimpleParse")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultSimpleParse implements ISimpleParse<FetcherTasks, ResponseData> {
    private static final Logger log = LoggerFactory.getLogger(DefaultSimpleParse.class);

    private RegexRule regexRule;

    @Autowired
    public DefaultSimpleParse(RegexRule regexRule) {
        this.regexRule = regexRule;
    }

    /**
     * desc:只能解析html类型的网页，json需后续处理
     **/
    @Override
    public FetcherTasks parseLinks(ResponseData responseData) {
        FetcherTasks next = new FetcherTasks();
        String contentType = responseData.getContentType();
        if (contentType != null && contentType.contains("text/html")) {
            Document doc = responseData.getDoc();
            if (doc != null) {
                //从页面中取出需要的url 形成接下来的任务
                int nowDeep = responseData.getFetcherTask().getDeepPath();
                FetcherTasks fetcherTasks = new Links().addHrefByRegx(doc, regexRule).toCrawlDatums(nowDeep);
                next.addFetcherTasks(fetcherTasks);
            }
        }
        return next;
    }
}
