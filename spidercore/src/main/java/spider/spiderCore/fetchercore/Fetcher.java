package spider.spiderCore.fetchercore;

import com.spider.myspider.redisDbComponent.DefaultDataUtil;
import commoncore.customUtils.BeanGainer;
import commoncore.customUtils.SleepUtil;
import commoncore.entity.fetcherEntity.FetcherState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 一杯咖啡
 * @desc 调度器，生产者，任务管道，消费者调度器
 * @createTime
 */
@Component
public class Fetcher {
    private static final Logger LOG = LoggerFactory.getLogger(Fetcher.class);
    private FetchQueue fetchQueue;
    private QueueFeeder queueFeeder;
    private FetcherState fetcherState;
    private DefaultDataUtil defaultDataUtil;
    private int threads;

    @Autowired
    public Fetcher(FetchQueue fetchQueue,
                   QueueFeeder queueFeeder,
                   FetcherState fetcherState,
                   DefaultDataUtil defaultDataUtil,
                   @Value(value = "${spider.totalThreads}") int threads) {
        this.fetchQueue = fetchQueue;
        this.queueFeeder = queueFeeder;
        this.fetcherState = fetcherState;
        this.defaultDataUtil = defaultDataUtil;
        this.threads = threads;
    }

    /**
     * 抓取当前所有任务，会阻塞到爬取完成 开启 feeder 和 执行爬取线程。
     *
     * @throws IOException 异常
     */
    public Integer fetcherStart() {
        //合并 入口和解析 任务库到 运行任务库
        defaultDataUtil.getiDbManager().merge();
        defaultDataUtil.getiDbWritor().initSegmentWriter();

        fetcherState.setFetcherRunning(true);
        //创建线程池，允许核心线程超时关闭
        ThreadPoolExecutor threadsExecutor = new ThreadPoolExecutor(threads + 1, threads + (threads / 2), 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
        threadsExecutor.allowCoreThreadTimeOut(true);

        LOG.info("启动任务生产者----------");
        threadsExecutor.execute(queueFeeder);
        //等待管道先添加任务
        SleepUtil.pause(2, 0);

        LOG.info("启动任务消费者----------");
        //初始化消费者 从queue中读取任务
        for (int i = 0; i < threads; i++) {
            FetcherThread ft = BeanGainer.getBean("fetcherThread", FetcherThread.class);
            if (ft == null) {
                this.stopFetcher();
                break;
            }
            threadsExecutor.execute(ft);
        }
        //主线程循环打印 线程池状态
        do {
            SleepUtil.pause(1, 0);
            LOG.info("执行器状态:\n" + fetcherState.toString());
            LOG.info("【线程池状态：\n" + threadsExecutor.toString() + " 】\n"
                    + "queue size" + fetchQueue.getSize());
        } while (threadsExecutor.getActiveCount() > 1 || fetcherState.isFetcherRunning());
        //立即停止调度器
        this.stopFetcher();
        threadsExecutor.shutdown();
        LOG.info("线程池状态？？---" + threadsExecutor.toString());
        LOG.info("线程池关闭？" + (threadsExecutor.isTerminated() ? "已关闭" : "未关闭"));
        defaultDataUtil.getiDbWritor().closeSegmentWriter();
        LOG.info("关闭数据库写入工具:" + defaultDataUtil.getiDbWritor().getClass().getName());
        //返回生成的任务总数
        return defaultDataUtil.getiGenerator().totalGeneretNum();
    }

    /**
     * 停止爬取
     */
    public boolean stopFetcher() {
        boolean feederStoped = queueFeeder.stopFeeder();
        if (feederStoped) {
            //停止调度器
            fetcherState.setFetcherRunning(false);
        } else {
            this.stopFetcher();
        }
        SleepUtil.pause(1, 0);
        return !fetcherState.isFeederRunnning();
    }

    public FetchQueue getFetchQueue() {
        return fetchQueue;
    }

    public QueueFeeder getQueueFeeder() {
        return queueFeeder;
    }

    public FetcherState getFetcherState() {
        return fetcherState;
    }

    public DefaultDataUtil getiDataUtil() {
        return defaultDataUtil;
    }
}
