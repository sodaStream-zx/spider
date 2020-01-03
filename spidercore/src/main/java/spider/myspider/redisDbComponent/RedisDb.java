package spider.myspider.redisDbComponent;

import com.spider.spiderCore.idbcore.IDataBase;
import commoncore.entity.loadEntity.RedisDbKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 一杯咖啡
 * @desc redis数据库表
 */
@Component
public class RedisDb implements IDataBase<String> {

    @Autowired
    private RedisDbKeys redisDbKeys;

    @Override
    public String getSeedList() {
        return redisDbKeys.getSeedsList();
    }

    @Override
    public String getDoneList() {
        return redisDbKeys.getDoneList();
    }

    @Override
    public String getUnDoneList() {
        return redisDbKeys.getUndoneList();
    }

    @Override
    public String getRedirectDB() {
        return redisDbKeys.getRedirectList();
    }

    /**
     * desc: 清空数据库
     **/
    @Override
    public void clear() {
    }
}
