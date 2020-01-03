package spider.myspider.redisDbComponent.dbutils;

import com.spider.spiderCore.entitys.FetcherTasks;
import com.spider.spiderCore.idbcore.IDataBase;
import com.spider.spiderCore.idbcore.IDbManager;
import com.spider.spiderCore.idbcore.IDbWritor;
import commoncore.customUtils.SerializeUtil;
import commoncore.entity.requestEntity.FetcherTask;
import commoncore.exceptionHandle.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author 一杯咖啡
 * @desc redis数据库写入工具
 * @createTime 2019-01-04-13:04
 */
@Component
@Scope("prototype")
public class DefaultRedisDbWritor implements IDbWritor<FetcherTask, FetcherTasks> {
    private static final Logger log = LoggerFactory.getLogger(DefaultRedisDbWritor.class);
    private IDbManager iDbManager;
    private RedisTemplate redisTemplate;
    private IDataBase iDataBase;

    @Autowired
    public DefaultRedisDbWritor(IDbManager iDbManager, RedisTemplate redisTemplate, IDataBase iDataBase) {
        this.iDbManager = iDbManager;
        this.redisTemplate = redisTemplate;
        this.iDataBase = iDataBase;
    }

    /**
     * desc: 种子注入 （左进）
     **/
    @Override
    public void inject(FetcherTasks tasks) throws MyException {
        if (!iDbManager.isDBExists()) {
            throw new MyException("redis 数据库无效");
        }
        tasks.getStream()
                .map(SerializeUtil::serializeToString)
                .forEach(x -> {
                    //注入任务到入口库
                    String seeds = (String) iDataBase.getSeedList();
                    log.warn("入口数据库 :" + seeds);
                    redisTemplate.opsForList().leftPush(seeds, x.get());
                });
        log.info("注入种子完成::" + tasks.size());
    }

    @Override
    public void initSegmentWriter() {

    }

    /**
     * desc: 写入已爬取任务(左进)
     **/
    @Override
    public void writeFetchSegment(FetcherTask fetchDatum) {
        log.warn("写入任务到已爬取。。。。。。。");
        Optional<String> taskString = SerializeUtil.serializeToString(fetchDatum);
        redisTemplate.opsForList().leftPush(iDataBase.getDoneList(), taskString.get());
    }

    /**
     * desc:写入待爬取任务
     **/
    @Override
    public void writeParseSegment(FetcherTasks parseDatums) {
        log.warn("写入任务到已后续任务。。。。。。。" + parseDatums.size());
        if (!parseDatums.isEmpty()) {
            log.info(Thread.currentThread().getName() + "-- 写入后续任务数量::" + parseDatums.size());
            parseDatums.getStream()
                    .map(SerializeUtil::serializeToString)
                    .forEach(t -> redisTemplate.opsForList()
                            .leftPush(iDataBase.getUnDoneList(), t.get()));
        }
    }

    @Override
    public void closeSegmentWriter() {

    }
}
