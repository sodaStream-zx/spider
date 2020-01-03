package spider.config;

import commoncore.entity.fetcherEntity.FetcherState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zxx
 * @desc
 * @createTime 2019-10-09-下午 5:08
 */
@Configuration
public class CommonConfig {

    @Bean
    public FetcherState fetcherState() {
        return new FetcherState();
    }
}
