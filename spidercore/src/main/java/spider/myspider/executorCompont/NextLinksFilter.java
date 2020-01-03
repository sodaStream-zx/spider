package spider.myspider.executorCompont;

import com.spider.spiderCore.entitys.FetcherTasks;
import com.spider.spiderCore.iexecutorCom.INextLinksFilter;
import commoncore.entity.loadEntity.WebSiteConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-22-16:59
 */
@Component
public class NextLinksFilter implements INextLinksFilter {
    private static final Logger log = LoggerFactory.getLogger(NextLinksFilter.class);
    private WebSiteConf webSiteConf;

    @Autowired
    public NextLinksFilter(WebSiteConf webSiteConf) {
        this.webSiteConf = webSiteConf;
    }

    @Override
    public FetcherTasks filter(FetcherTasks tasks) {
        log.warn("过滤后续任务" + tasks.size());
        FetcherTasks filted = new FetcherTasks();
        tasks.getStream()
                .filter(x -> webSiteConf.getDeepPath() >= x.getDeepPath())
                .forEach(filted::addTask);
        log.warn("过滤后续任务后" + filted.size());
        return filted;
    }
}
