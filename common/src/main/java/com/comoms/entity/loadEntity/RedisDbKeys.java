package commoncore.entity.loadEntity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author Twilight
 * @desc 保存redis 中的配置信息数据表名
 * @createTime 2019-01-21-16:50
 */
@ConfigurationProperties(value = "redisdb")
@Configuration
public class RedisDbKeys implements Serializable {
    private static final long serialVersionUID = -54454855L;
    private int keyId;
    //网站配置表名list
    private String wsConfList;
    //网站提取规则表名hashmap
    private String urlRuleHash;
    //网站正文提取规则hashmap
    private String domainRuleHash;
    //种子表名list
    private String seedsList;
    //已抓取任务list
    private String undoneList;
    //后续任务list
    private String doneList;
    //重试任务list
    private String redirectList;
    //解析队列
    private String parseList;

    public RedisDbKeys() {
    }

    public RedisDbKeys(String wsConfList, String urlRuleHash, String domainRuleHash, String seedsList, String undoneList, String doneList, String redirectList, String parseList) {
        this.wsConfList = wsConfList;
        this.urlRuleHash = urlRuleHash;
        this.domainRuleHash = domainRuleHash;
        this.seedsList = seedsList;
        this.undoneList = undoneList;
        this.doneList = doneList;
        this.redirectList = redirectList;
        this.parseList = parseList;
    }

    @Override
    public String toString() {
        return "RedisDbKeys{" +
                "keyId=" + keyId +
                ", wsConfList='" + wsConfList + '\'' +
                ", urlRuleHash='" + urlRuleHash + '\'' +
                ", domainRuleHash='" + domainRuleHash + '\'' +
                ", seedsList='" + seedsList + '\'' +
                ", undoneList='" + undoneList + '\'' +
                ", doneList='" + doneList + '\'' +
                ", redirectList='" + redirectList + '\'' +
                ", parseList='" + parseList + '\'' +
                '}';
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public String getWsConfList() {
        return wsConfList;
    }

    public void setWsConfList(String wsConfList) {
        this.wsConfList = wsConfList;
    }

    public String getUrlRuleHash() {
        return urlRuleHash;
    }

    public void setUrlRuleHash(String urlRuleHash) {
        this.urlRuleHash = urlRuleHash;
    }

    public String getDomainRuleHash() {
        return domainRuleHash;
    }

    public void setDomainRuleHash(String domainRuleHash) {
        this.domainRuleHash = domainRuleHash;
    }

    public String getSeedsList() {
        return seedsList;
    }

    public void setSeedsList(String seedsList) {
        this.seedsList = seedsList;
    }

    public String getUndoneList() {
        return undoneList;
    }

    public void setUndoneList(String undoneList) {
        this.undoneList = undoneList;
    }

    public String getDoneList() {
        return doneList;
    }

    public void setDoneList(String doneList) {
        this.doneList = doneList;
    }

    public String getRedirectList() {
        return redirectList;
    }

    public void setRedirectList(String redirectList) {
        this.redirectList = redirectList;
    }

    public String getParseList() {
        return parseList;
    }

    public void setParseList(String parseList) {
        this.parseList = parseList;
    }

    public void configOwn(RedisDbKeys redisDbKeys) {
        this.wsConfList = redisDbKeys.getWsConfList();
        this.urlRuleHash = redisDbKeys.getUrlRuleHash();
        this.domainRuleHash = redisDbKeys.getDomainRuleHash();
        this.seedsList = redisDbKeys.getSeedsList();
        this.undoneList = redisDbKeys.getUndoneList();
        this.doneList = redisDbKeys.getDoneList();
        this.redirectList = redisDbKeys.getRedirectList();
        this.parseList = redisDbKeys.getParseList();
    }
}
