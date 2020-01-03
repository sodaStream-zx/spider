package spider.myspider.redisDbComponent.dbutils;

import com.spider.myspider.redisDbComponent.DefaultDataUtil;
import com.spider.spiderCore.idbcore.IDataBase;
import com.spider.spiderCore.idbcore.IDbManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 一杯咖啡
 * @desc redis 数据库管理操作
 * @createTime 2019-01-04-13:11
 */
@Component
public class DefaultRedisManager implements IDbManager {
    private static final Logger log = LoggerFactory.getLogger(DefaultDataUtil.class);
    private IDataBase iDataBase;
    private RedisTemplate redisTemplate;

    @Autowired
    public DefaultRedisManager(IDataBase iDataBase,
                               RedisTemplate redisTemplate) {
        this.iDataBase = iDataBase;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isDBExists() {
        RedisConnection flag = redisTemplate.getConnectionFactory().getConnection();
        if (flag != null) {
            flag.close();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        log.info("清空数据库");
    }

    @Override
    public void open() {
        log.info("开启数据库");
    }

    @Override
    public void close() {
        log.info("关闭数据库");
    }

    /**
     * desc:将入口url和待抓取任务url合并
     **/
    @Override
    public void merge() {
        String seeds = (String) iDataBase.getSeedList();
        String undone = (String) iDataBase.getUnDoneList();
        while (redisTemplate.opsForList().size(seeds) > 0) {
            String seedStr = (String) redisTemplate.opsForList().leftPop(seeds);
            //合并时将种子放在最右边，右边为出队列
            redisTemplate.opsForList().rightPush(undone, seedStr);
        }
        log.info("合并任务库");
    }
}
