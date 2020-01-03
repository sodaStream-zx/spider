package spider.myspider.services;


import com.spider.myspider.redisDbComponent.DefaultDataUtil;
import com.spider.spiderCore.crawler.AbstractSpider;
import com.spider.spiderCore.entitys.RegexRule;
import com.spider.spiderCore.fetchercore.Fetcher;
import commoncore.customUtils.SerializeUtil;
import commoncore.customUtils.SleepUtil;
import commoncore.entity.loadEntity.RedisDbKeys;
import commoncore.entity.loadEntity.UrlRule;
import commoncore.entity.loadEntity.WebSiteConf;
import commoncore.exceptionHandle.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 爬虫组件类
 *
 * @author 一杯咖啡
 */
@Service
public class MySpider extends AbstractSpider {
    private static final Logger LOG = LoggerFactory.getLogger(MySpider.class.getSimpleName());
    private RedisTemplate redisTemplate;
    private RedisDbKeys redisDbKeys;
    @Value(value = "${redisDb.config}")
    private String redisConfig;

    @Autowired
    public MySpider(RegexRule regexRule,
                    Fetcher fetcher,
                    DefaultDataUtil defaultDataUtil,
                    RedisTemplate redisTemplate,
                    WebSiteConf webSiteConf,
                    RedisDbKeys redisDbKeys,
                    UrlRule urlRule) {
        super(urlRule, webSiteConf, regexRule, fetcher, defaultDataUtil);
        this.redisTemplate = redisTemplate;
        this.redisDbKeys = redisDbKeys;
    }

    /**
     * desc: 加载外部所有爬虫配置
     */
    @Override
    public void loadOuterConfig() {
        String wbStr;
        String urlRuleStr;
        //加载网站配置
        wbStr = this.loadWebConfig();
        Optional<WebSiteConf> wbs = SerializeUtil.deserializeToObject(wbStr);
        webSiteConf.configOwn(wbs.get());
        if (wbs.isPresent()) {
            //通过网站配置加载url解析规则
            urlRuleStr = this.loadUrlRule(wbs.get().getSiteName());
            Optional<UrlRule> ur = SerializeUtil.deserializeToObject(urlRuleStr);
            urlRule.configOwn(ur.get());
            if (ur.isPresent()) {
                LOG.info("-----------配置清单：-----------"
                        + "\n" + this.redisDbKeys.toString()
                        + "\n" + this.webSiteConf.toString()
                        + "\n" + this.urlRule.toString());
                LOG.info("--------------------");
            }
        }
    }

    /**
     * desc: 加载爬虫配置，网站配置
     **/
    public String loadWebConfig() {
        LOG.info("-----------加载网站配置 配置-----------");
        String wbStr;
        //读取redis队列任务，并开始抓取；阻塞直到能取出值
        while (true) {
            BoundListOperations wbConfig = redisTemplate.boundListOps(redisDbKeys.getWsConfList());
            wbStr = (String) wbConfig.rightPop();
            wbConfig.leftPush(wbStr);
            // wbStr = (String) redisTemplate.opsForList().rightPop(redisDbKeys.getWsConfList());
            if ("".equals(wbStr) || null == wbStr) {
                LOG.error("redis 网站配置数据为空");
                SleepUtil.pause(1, 0);
            } else {
                break;
            }
        }
        return wbStr;
    }

    /**
     * desc: 加载url提取配置
     **/
    public String loadUrlRule(String siteName) {
        LOG.info("-----------加载url解析规则 配置-----------");
        String urlStr;
        //读取redis队列任务，并开始抓取；阻塞直到能取出值
        while (true) {
            urlStr = (String) redisTemplate.opsForHash().get(redisDbKeys.getUrlRuleHash(), siteName);
            if ("".equals(urlStr) || null == urlStr) {
                LOG.error("redis中 网站url配置数据为空");
                SleepUtil.pause(1, 0);
            } else {
                break;
            }
        }
        return urlStr;
    }

    /**
     * desc；结束后再从redis中读取下一个网站配置
     *
     * @Return:
     **/
    @Override
    public void afterStopSpider() throws MyException {
        this.startSpider();
    }
}
