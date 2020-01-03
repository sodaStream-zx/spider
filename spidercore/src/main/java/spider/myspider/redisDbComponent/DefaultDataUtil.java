package spider.myspider.redisDbComponent;

import com.spider.spiderCore.idbcore.IDbManager;
import com.spider.spiderCore.idbcore.IDbWritor;
import com.spider.spiderCore.idbcore.IGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 一杯咖啡
 * @desc 数据库组件汇总
 * @createTime 2019-01-04-13:13
 */
@Component
public class DefaultDataUtil {
    //数据库提取组件
    private IGenerator iGenerator;
    //数据库写入组件
    private IDbWritor iDbWritor;
    //数据库管理组件
    private IDbManager iDbManager;

    public DefaultDataUtil() {
    }

    @Autowired
    public DefaultDataUtil(IGenerator iGenerator, IDbWritor iDbWritor, IDbManager iDbManager) {
        this.iGenerator = iGenerator;
        this.iDbWritor = iDbWritor;
        this.iDbManager = iDbManager;
    }

    @Override
    public String toString() {
        return "DefaultRedisDataBase{" +
                "iGenerator=" + iGenerator +
                ", iDbWritor=" + iDbWritor +
                ", iDbManager=" + iDbManager +
                '}';
    }

    public IGenerator getiGenerator() {
        return iGenerator;
    }

    public void setiGenerator(IGenerator iGenerator) {
        this.iGenerator = iGenerator;
    }

    public IDbWritor getiDbWritor() {
        return iDbWritor;
    }

    public void setiDbWritor(IDbWritor iDbWritor) {
        this.iDbWritor = iDbWritor;
    }

    public IDbManager getiDbManager() {
        return iDbManager;
    }

    public void setiDbManager(IDbManager iDbManager) {
        this.iDbManager = iDbManager;
    }
}
