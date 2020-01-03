package spider.spiderCore.fetchercore;

import com.spider.myspider.executorCompont.DefaultExecutor;
import com.spider.spiderCore.iexecutorCom.IExecutor;
import commoncore.customUtils.BeanGainer;
import commoncore.customUtils.SleepUtil;
import commoncore.entity.fetcherEntity.FetcherState;
import commoncore.entity.requestEntity.FetcherTask;
import commoncore.exceptionHandle.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author 一杯咖啡
 * @desc 任务消费者
 * @createTime
 */
@Component(value = "fetcherThread")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FetcherThread implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(FetcherThread.class);
    private FetcherState fetcherState;
    private FetchQueue fetchQueue;
    private long defaultExecuteInterval;
    private IExecutor<FetcherTask> iExecutor;

    @Autowired
    public FetcherThread(FetcherState fetcherState,
                         FetchQueue fetchQueue,
                         @Value(value = "${fetcher.executeInterval}") long defaultExecuteInterval) {
        this.fetcherState = fetcherState;
        this.fetchQueue = fetchQueue;
        this.defaultExecuteInterval = defaultExecuteInterval;
        this.iExecutor = BeanGainer.getBean("iExecutor", DefaultExecutor.class);
        LOG.info("加载默认执行器" + iExecutor.toString());
    }

    @Override
    public void run() {
        FetcherTask task;
        //判断调度器是否处于运行状态
        while (fetcherState.isFetcherRunning() || fetchQueue.getSize() > 0) {
            //从queue中取出任务
            task = fetchQueue.getTask();
            if (task == null) {
                LOG.warn("缓冲区 无后续任务");
                //判断生产者是否运行中，如果没有，等待任务管道被消费完，停止执行线程
                if (fetcherState.isFeederRunnning()) {
                    SleepUtil.pause(0, 200);
                    continue;
                } else {
                    break;
                }
            } else {
                LOG.warn("从QUEUE 取得任务" + task.getUrl());
                //调用执行器
                try {
                    iExecutor.execute(task);
                } catch (MyException e) {
                    LOG.error("http请求工具配置错误：" + e.getMsg());
                }

                //当前页面执行完毕，间隔
                long executeInterval = defaultExecuteInterval;
                if (executeInterval > 0) {
                    SleepUtil.pause(0, executeInterval);
                }
            }
        }
    }
}
