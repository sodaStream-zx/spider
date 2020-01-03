package commoncore.entity.httpEntity;

import java.io.Serializable;

/**
 * @author Twilight
 * @desc 传输到解析模块的数据 封装
 * @createTime 2019-01-17-15:20
 */
public class ParseData implements Serializable {
    private static final long serialVersionUID = 12546852L;
    private int dataId;
    private String siteName;//网站名
    private String pageUrl;
    private String contentType;
    private String contentString;

    public ParseData() {
    }

    public ParseData(String siteName, String pageUrl, String contentType, String contentString) {
        this.siteName = siteName;
        this.pageUrl = pageUrl;
        this.contentType = contentType;
        this.contentString = contentString;
    }

    @Override
    public String toString() {
        return new StringBuffer()
                .append("siteName: ").append(siteName)
                .append("pageUrl: ").append(pageUrl)
                .append("contentType: ").append(contentType)
                .append("contentString(limit 20): ").append(contentString, 0, 20)
                .toString();
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentString() {
        return contentString;
    }

    public void setContentString(String contentString) {
        this.contentString = contentString;
    }

    public String pumpInfo() {
        return new StringBuffer()
                .append("siteName: ")
                .append(siteName)
                .append(",url: ")
                .append(pageUrl).toString();
    }
}
