package spider.myspider.redisDbComponent.dbutils;

import com.spider.spiderCore.idbcore.IGeneratorFilter;
import commoncore.entity.loadEntity.WebSiteConf;
import commoncore.entity.requestEntity.FetcherTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author 一杯咖啡
 * @desc 爬虫任务状态 最大执行次数过滤
 */
@Component
public class StatusGeneratorFilter implements IGeneratorFilter<FetcherTask> {
    private static final Logger log = LoggerFactory.getLogger(StatusGeneratorFilter.class);
    @Value(value = "${igenerator.maxExecuteCount}")
    private int maxExecuteCount;
    @Autowired
    private WebSiteConf webSiteConf;

    /**
     * desc: 判断url携带的元数据 中的状态 过滤已爬取的
     *
     * @Return: CrawlDatumn
     **/
    @Override
    public FetcherTask filter(FetcherTask task) {
        if (task.getDeepPath() > webSiteConf.getDeepPath()) {
            return null;
        }
        if (task.getStatus() == FetcherTask.STATUS_DB_SUCCESS && task.getExecuteCount() > maxExecuteCount) {
            return null;
        } else {
            return task;
        }
    }
}
