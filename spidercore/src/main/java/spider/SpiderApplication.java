package spider;


import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import commoncore.customUtils.SerializeUtil;
import commoncore.entity.loadEntity.RedisDbKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author 一杯咖啡
 * @desc 爬虫启动类 （springboot 启动后，启动爬虫；检查环境）
 * @createTime ${YEAR}-${MONTH}-${DAY}-${TIME}
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com", "commoncore"})
@EnableDiscoveryClient
public class SpiderApplication {
    private static final Logger LOG = LoggerFactory.getLogger(SpiderApplication.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private RedisDbKeys redisDbKeys;
    @Value(value = "${redisDb.config}")
    private String redisDbConfig;

    public static void main(String[] args) {
        SpringApplication.run(SpiderApplication.class, args);
    }

    /**
     * desc: 初始化配置监视，出异常直接退出
     *
     * @throws SQLException
     **/
    @PostConstruct
    public void monitor() throws SQLException {
        redisTemplate.setKeySerializer(new FastJsonRedisSerializer<>(Object.class));
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        redisTemplate.setHashKeySerializer(new FastJsonRedisSerializer<>(Object.class));
        redisTemplate.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        redisTemplate.afterPropertiesSet();
        LOG.info("环境监视：\n");
        this.loadRedisConfig();
        dataSource.getConnection();
    }

    /**
     * desc: 加载redisDb配置
     **/
    public void loadRedisConfig() {
        //加载redis配置
        String redisDbStr = (String) redisTemplate.opsForValue().get(redisDbConfig);
        Optional<RedisDbKeys> rs = SerializeUtil.deserializeToObject(redisDbStr);
        if (rs.isPresent()) {
            redisDbKeys.configOwn(rs.get());
        }
        LOG.info(redisDbKeys.toString());
    }
}
