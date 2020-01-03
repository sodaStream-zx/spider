package spider.myspider.redisDbComponent.dbutils;

import com.spider.spiderCore.idbcore.IGenerator;
import com.spider.spiderCore.idbcore.IGeneratorFilter;
import commoncore.customUtils.SerializeUtil;
import commoncore.entity.requestEntity.FetcherTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author 一杯咖啡
 * @desc redis数据库提取提取工具
 * @createTime 2019-01-04-13:30
 */
@Component
public class DefaultRedisGenerator implements IGenerator<FetcherTask> {
    public static final Logger LOG = LoggerFactory.getLogger(DefaultRedisGenerator.class);
    private com.spider.spiderCore.idbcore.IDataBase<String> IDataBase;
    private IGeneratorFilter<FetcherTask> filter;
    private int topN;
    private RedisTemplate redisTemplate;
    //提取工具计数
    private int totalGenerate = 0;

    @Autowired
    public DefaultRedisGenerator(@Autowired(required = false) IGeneratorFilter<FetcherTask> filter,
                                 com.spider.spiderCore.idbcore.IDataBase<String> IDataBase,
                                 RedisTemplate redisTemplate,
                                 @Value(value = "${generator.topNumber}") int topN) {
        this.filter = filter;
        this.IDataBase = IDataBase;
        this.redisTemplate = redisTemplate;
        this.topN = topN;
    }

    @Override
    public FetcherTask next() {
        if (topN > 0 && totalGenerate >= topN) {
            return null;
        }
        FetcherTask task;
        while (true) {
            task = nextWithoutFilter();
            if (task == null) {
                return null;
            }
            if (filter != null) {
                if (filter.filter(task) == null) {
                    continue;
                }
            }
            totalGenerate += 1;
            return task;
        }
    }

    /**
     * desc: 从redis待爬取任务表中提取任务(右出)
     *
     * @Return:CrawlDatum
     **/
    @Override
    public FetcherTask nextWithoutFilter() {
        String undone = IDataBase.getUnDoneList();
        String datumString = (String) redisTemplate.opsForList().rightPop(undone);
        if (datumString != null) {
            Optional<FetcherTask> taskOp = SerializeUtil.deserializeToObject(datumString);
            if (taskOp.isPresent()) {
                LOG.warn("redis 提取任务 :" + taskOp.get());
                return taskOp.get();
            }
        }
        return null;
    }

    @Override
    public int totalGeneretNum() {
        return this.totalGenerate;
    }

    @Override
    public boolean clear() {
        this.totalGenerate = 0;
        return true;
    }

    @Override
    public boolean close() {
        return true;
    }

    @Override
    public String toString() {
        return "DefaultRedisGenerator{" +
                "IDataBase=" + IDataBase +
                ", filter=" + filter +
                ", topN=" + topN +
                ", totalGenerate=" + totalGenerate +
                ", redisTemplate=" + redisTemplate +
                '}';
    }
}
