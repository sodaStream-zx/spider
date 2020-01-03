package spider.spiderCore.fetchercore;


import com.spider.spiderCore.idbcore.IGenerator;
import commoncore.customUtils.SleepUtil;
import commoncore.entity.fetcherEntity.FetcherState;
import commoncore.entity.requestEntity.FetcherTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 一杯咖啡
 * @desc 任务生产者
 * @createTime
 */
@Component
public class QueueFeeder implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(QueueFeeder.class);
    private FetchQueue queue;
    private IGenerator<FetcherTask> iGenerator;
    //queeue 大小最大值设置
    private int queueMaxSize;

    private FetcherState fetcherState;

    @Autowired
    public QueueFeeder(FetchQueue queue,
                       IGenerator<FetcherTask> iGenerator,
                       @Value(value = "${queue.MaxSize}") int queueMaxSize,
                       FetcherState fetcherState) {
        this.queue = queue;
        this.iGenerator = iGenerator;
        this.queueMaxSize = queueMaxSize;
        this.fetcherState = fetcherState;
    }

    /**
     * desc:关闭管道添加工具
     **/
    public boolean stopFeeder() {
        this.closeGenerator();
        fetcherState.setFeederRunnning(false);
        SleepUtil.pause(1, 0);
        return !fetcherState.isFeederRunnning();
    }

    /**
     * desc:关闭任务生成工具
     **/
    public void closeGenerator() {
        if (iGenerator != null) {
            iGenerator.close();
            LOG.info("关闭数据库提取工具:" + iGenerator.getClass().getName());
        }
    }

    /**
     * desc: feeder 线程运行
     **/
    @Override
    public void run() {
        //获取任务生成工具 （从数据库中提取数据）
        LOG.info("任务生产者开始运行.......：数据提取工具：" + iGenerator.getClass());
        boolean hasMore = true;
        fetcherState.setFeederRunnning(true);

        while (hasMore && fetcherState.isFeederRunnning()) {
            //监听queue中数量，当queue中数量为1000时，线程等待，
            int feed = queueMaxSize - queue.getSize();
            if (feed <= 0) {
                SleepUtil.pause(1, 0);
                continue;
            }
            LOG.info("queue size：" + feed);
            //如果queue中小于1000，往queue中添加新任务，未提取到任务 count 等待时间
            int count = 0;
            while (feed > 0 && hasMore && fetcherState.isFeederRunnning()) {
                //任务生成器 如果下一个任务为空，返回空。判断dbmananger中是否有后续任务
                FetcherTask task = iGenerator.next();
                LOG.warn("获取任务：" + task);
                LOG.info("当前任务deepPath:" + task.getDeepPath());
                // FetcherTask task = new FetcherTask("testUrl");
                if (task != null) {
                    LOG.warn("加入任务到缓冲区：" + task.getUrl());
                    queue.addTask(task);
                    feed--;//一直填到queue为1000
                    count = 0;
                } else {
                    count++;
                    SleepUtil.pause(1, 0);
                    if (count < 5) {
                        LOG.info("redis 中无后续任务，等待第 " + count + " 秒");
                    } else {
                        LOG.info("等待超时，关闭程序");
                        fetcherState.setFeederRunnning(false);
                        hasMore = false;
                    }
                }
            }
        }
    }

    public IGenerator<FetcherTask> getiGenerator() {
        return iGenerator;
    }

    public int getQueueMaxSize() {
        return queueMaxSize;
    }

    public FetcherState getFetcherState() {
        return fetcherState;
    }
}
