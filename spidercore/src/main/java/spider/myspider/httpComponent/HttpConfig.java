package spider.myspider.httpComponent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-09-12:00
 */
@Component
//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HttpConfig {
    @Value(value = "${httpconfig.maxRedirect}")
    private int maxRedirect;
    @Value(value = "${httpconfig.maxReceiveSize}")
    private int maxReceiveSize;
    @Value(value = "${httpconfig.timeoutForConnect}")
    private int timeoutForConnect;
    @Value(value = "${httpconfig.timeoutForRead}")
    private int timeoutForRead;
    @Value(value = "${httpconfig.userAgent}")
    private String userAgent;
    private String cookie = null;
    private MultiValueMap headerMap = new LinkedMultiValueMap();
    private MultiValueMap postBodyMap = new LinkedMultiValueMap();
    private boolean doinput = true;
    private boolean dooutput = true;
    private boolean followRedirects = false;

    public HttpConfig() {
    }

    public HttpConfig(String cookie, MultiValueMap headerMap, MultiValueMap postBodyMap, boolean doinput, boolean dooutput, boolean followRedirects) {
        this.cookie = cookie;
        this.headerMap = headerMap;
        this.postBodyMap = postBodyMap;
        this.doinput = doinput;
        this.dooutput = dooutput;
        this.followRedirects = followRedirects;
    }

    @Override
    public String toString() {
        return "HttpConfig{" +
                "maxRedirect=" + maxRedirect +
                ", maxReceiveSize=" + maxReceiveSize +
                ", timeoutForConnect=" + timeoutForConnect +
                ", timeoutForRead=" + timeoutForRead +
                ", userAgent='" + userAgent + '\'' +
                ", cookie='" + cookie + '\'' +
                ", headerMap=" + headerMap +
                ", postBodyMap=" + postBodyMap +
                ", doinput=" + doinput +
                ", dooutput=" + dooutput +
                ", followRedirects=" + followRedirects +
                '}';
    }

    public int getMaxRedirect() {
        return maxRedirect;
    }

    public void setMaxRedirect(int maxRedirect) {
        this.maxRedirect = maxRedirect;
    }

    public int getMaxReceiveSize() {
        return maxReceiveSize;
    }

    public void setMaxReceiveSize(int maxReceiveSize) {
        this.maxReceiveSize = maxReceiveSize;
    }

    public int getTimeoutForConnect() {
        return timeoutForConnect;
    }

    public void setTimeoutForConnect(int timeoutForConnect) {
        this.timeoutForConnect = timeoutForConnect;
    }

    public int getTimeoutForRead() {
        return timeoutForRead;
    }

    public void setTimeoutForRead(int timeoutForRead) {
        this.timeoutForRead = timeoutForRead;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public MultiValueMap getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(MultiValueMap headerMap) {
        this.headerMap = headerMap;
    }

    public MultiValueMap getPostBodyMap() {
        return postBodyMap;
    }

    public void setPostBodyMap(MultiValueMap postBodyMap) {
        this.postBodyMap = postBodyMap;
    }

    public boolean isDoinput() {
        return doinput;
    }

    public void setDoinput(boolean doinput) {
        this.doinput = doinput;
    }

    public boolean isDooutput() {
        return dooutput;
    }

    public void setDooutput(boolean dooutput) {
        this.dooutput = dooutput;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }
}
