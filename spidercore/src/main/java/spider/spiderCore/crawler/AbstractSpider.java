package spider.spiderCore.crawler;

import com.spider.myspider.redisDbComponent.DefaultDataUtil;
import com.spider.spiderCore.entitys.FetcherTasks;
import com.spider.spiderCore.entitys.RegexRule;
import com.spider.spiderCore.fetchercore.Fetcher;
import com.spider.spiderCore.iexecutorCom.ISpider;
import commoncore.customUtils.StringSplitUtil;
import commoncore.entity.loadEntity.UrlRule;
import commoncore.entity.loadEntity.WebSiteConf;
import commoncore.entity.requestEntity.FetcherTask;
import commoncore.exceptionHandle.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

/**
 * @author Twilight
 * @desc 爬虫抽象类
 * @createTime 2019-01-08-14:34
 */
public abstract class AbstractSpider implements ISpider {

    public final static int RUNNING = 1;
    public final static int STOPED = 2;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSpider.class);
    //网站配置
    protected UrlRule urlRule;
    //网站入口和爬虫配置
    protected WebSiteConf webSiteConf;
    //入口临时容器
    protected FetcherTasks fetcherTasks = new FetcherTasks();
    //正则规则
    protected RegexRule regexRule;
    //执行单元调度器
    protected Fetcher fetcher;
    //数据存储器（考虑将种子注入和 解析注入分开）
    protected DefaultDataUtil defaultDataUtil;
    private int status;

    /**
     * desc: 必要内容
     **/
    public AbstractSpider(UrlRule urlRule,
                          WebSiteConf webSiteConf,
                          RegexRule regexRule,
                          Fetcher fetcher,
                          DefaultDataUtil defaultDataUtil) {
        this.urlRule = urlRule;
        this.webSiteConf = webSiteConf;
        this.regexRule = regexRule;
        this.fetcher = fetcher;
        this.defaultDataUtil = defaultDataUtil;
    }

    public static int getRUNNING() {
        return RUNNING;
    }

    public static int getSTOPED() {
        return STOPED;
    }

    /**
     * desc:加载所有外部配置
     **/
    @Override
    public abstract void loadOuterConfig() throws MyException;

    /**
     * @desc 装载所有内部配置
     * urlRules url 解析正则表达式
     * seeds 入口 url
     * conPickRules 正文提取正则表达式
     */
    @Override
    public void loadInnerConfig() throws MyException {
        if (webSiteConf == null || webSiteConf.getSiteName() == null || urlRule == null || urlRule.getPageParse() == null) {
            throw new MyException("网站配置文件未加载,或者无配置内容");
        }
        //seeds 种子url
        String[] seeds = StringSplitUtil.splitRule(webSiteConf.getSeeds());
        Stream.of(seeds).forEach(x -> {
            FetcherTask task = new FetcherTask(x);
            task.setForceFecther(webSiteConf.isForceFecther());
            fetcherTasks.addTask(task);
        });
        //url 提取正则
        String[] urlRules = StringSplitUtil.splitRule(urlRule.getUrlPares());
        //conPickRules 需要提取正文的 url 正则表达式
        String[] conPickRules = StringSplitUtil.splitRule(urlRule.getPageParse());
        regexRule.addContentRegexRule(conPickRules);
        regexRule.addPickReges(urlRules);
        regexRule.addPickReges(conPickRules);
        LOG.info("url提取规则注入" + regexRule.info());
    }

    /**
     * desc: 注入入口种子到数据库中
     **/
    @Override
    public void injectSeeds() throws MyException {
        LOG.info("seeds注入：" + fetcherTasks.get(0).toString());
        defaultDataUtil.getiDbWritor().inject(fetcherTasks);
    }

    /**
     * desc:开启爬取
     *
     * @Return: boolean
     **/
    @Override
    public boolean startSpider() throws MyException {
        this.loadOuterConfig();
        this.loadInnerConfig();
        defaultDataUtil.getiDbManager().open();
        //注入入口
        this.injectSeeds();

        status = RUNNING;

        LOG.info("爬虫配置完成，开始抓取.........");
        Instant start = Instant.now();
        int totalGenerate = fetcher.fetcherStart();
        Instant end = Instant.now();
        long costTime = Duration.between(start, end).getSeconds();
        LOG.info("\nfinished: " +
                "\n           total urls: " + totalGenerate + "" +
                "\n           total time: " + costTime + " seconds");
        defaultDataUtil.getiDbManager().close();
        //关闭后不继续 提取后续网站配置
        if (status == RUNNING) {
            this.afterStopSpider();
        }
        return true;
    }

    @Override
    public boolean stopSpider() {
        status = STOPED;
        LOG.info("-----------关闭爬虫------------");
        return fetcher.stopFetcher();
    }

    @Override
    public abstract void afterStopSpider() throws MyException;

    public RegexRule getRegexRule() {
        return regexRule;
    }

    public void setRegexRule(RegexRule regexRule) {
        this.regexRule = regexRule;
    }

    public Fetcher getFetcher() {
        return fetcher;
    }

    public void setFetcher(Fetcher fetcher) {
        this.fetcher = fetcher;
    }

    public DefaultDataUtil getDefaultDataUtil() {
        return defaultDataUtil;
    }

    public void setDefaultDataUtil(DefaultDataUtil defaultDataUtil) {
        this.defaultDataUtil = defaultDataUtil;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
