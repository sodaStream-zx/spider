package spider.spiderCore.fetchercore;

import commoncore.entity.requestEntity.FetcherTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 一杯咖啡
 * @desc 任务缓存管道
 * @createTime
 */
@Component
public class FetchQueue {
    private static final Logger LOG = LoggerFactory.getLogger(FetchQueue.class);
    public final List<FetcherTask> queue = Collections.synchronizedList(new LinkedList<>());
    public AtomicInteger totalSize = new AtomicInteger(0);

    public void clearQueue() {
        queue.clear();
    }

    public int getSize() {
        return queue.size();
    }

    /**
     * desc:添加任务到queue中
     *
     * @Return: void
     **/
    public synchronized void addTask(FetcherTask item) {
        if (item == null) {
            return;
        }
        queue.add(item);
        totalSize.incrementAndGet();
    }

    /**
     * desc:从queue中提取任务
     **/
    public synchronized FetcherTask getTask() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.remove(0);
    }

    /**
     * desc:打印queue中的任务
     **/
    public synchronized void dump() {
        this.queue.forEach(crawlDatum -> LOG.info("queue中任务：" + crawlDatum.getUrl()));
    }
}
