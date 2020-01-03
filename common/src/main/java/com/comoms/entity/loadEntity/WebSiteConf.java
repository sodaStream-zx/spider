package commoncore.entity.loadEntity;

import java.io.Serializable;

/**
 * @author Twilight
 * @desc 网站配置，入口，深度等
 * @createTime 2019-01-21-16:20
 */
public class WebSiteConf implements Serializable {
    private static final long serialVersionUID = 9865874136L;
    //id
    private int webId;
    //网站名称
    private String siteName;
    //网站入口
    private String seeds;
    //抓取深度
    private int deepPath;

    private boolean forceFecther = false;

    public WebSiteConf() {
    }

    public WebSiteConf(String siteName, String seeds, int deepPath, boolean forceFecther) {
        this.siteName = siteName;
        this.seeds = seeds;
        this.deepPath = deepPath;
        this.forceFecther = forceFecther;
    }

    @Override
    public String toString() {
        return "WebSiteConf{" +
                "webId=" + webId +
                ", siteName='" + siteName + '\'' +
                ", seeds='" + seeds + '\'' +
                ", deepPath=" + deepPath +
                ", forceFecther=" + forceFecther +
                '}';
    }

    public boolean isForceFecther() {
        return forceFecther;
    }

    public void setForceFecther(boolean forceFecther) {
        this.forceFecther = forceFecther;
    }

    public int getWebId() {
        return webId;
    }

    public void setWebId(int webId) {
        this.webId = webId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSeeds() {
        return seeds;
    }

    public void setSeeds(String seeds) {
        this.seeds = seeds;
    }

    public int getDeepPath() {
        return deepPath;
    }

    public void setDeepPath(int deepPath) {
        this.deepPath = deepPath;
    }

    public void configOwn(WebSiteConf wbs) {
        this.webId = wbs.getWebId();
        this.siteName = wbs.getSiteName();
        this.seeds = wbs.getSeeds();
        this.deepPath = wbs.getDeepPath();
    }
}
