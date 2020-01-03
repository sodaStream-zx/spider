package spider.myspider.httpComponent;

import com.alibaba.fastjson.JSON;
import com.spider.spiderCore.entitys.HttpResponse;
import com.spider.spiderCore.ihttp.IProxyGain;
import com.spider.spiderCore.ihttp.ISendRequest;
import commoncore.entity.httpEntity.ResponseData;
import commoncore.entity.requestEntity.FetcherTask;
import commoncore.exceptionHandle.MyException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * @author 一杯咖啡
 * @desc 请求发送器
 * @createTime 2018-12-28-13:17
 */
@Component(value = "defaultRequest")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultHttpRequest implements ISendRequest<ResponseData> {
    public static final Logger LOG = LoggerFactory.getLogger(DefaultHttpRequest.class);

    static {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception ex) {
            LOG.info("Exception", ex);
        }
    }

    private HttpConfig httpConfig;
    private IProxyGain iProxyGain;

    @Autowired
    public DefaultHttpRequest(HttpConfig httpConfig, IProxyGain iProxyGain) {
        this.httpConfig = httpConfig;
        this.iProxyGain = iProxyGain;
    }

    /**
     * desc: 将httpResponse 封装为responsePage
     *
     * @throws IOException,MyException
     **/
    @Override
    public ResponseData converterResponsePage(FetcherTask fetcherTask) throws IOException, MyException {
        // LOG.info("httputil 配置" + this.httpConfig.toString());
        HttpResponse httpResponse = this.sendRequest(fetcherTask);
        ResponseData responseData = new ResponseData(
                fetcherTask,
                httpResponse.code(),
                httpResponse.contentType(),
                httpResponse.content()
        );
        LOG.warn("响应数据：" + responseData.getHtml().length());
        return responseData;
    }

    /**
     * desc: 设置请求头
     **/
    @Override
    public void configHttpRequest(HttpURLConnection httpURLConnection) {
        httpURLConnection.setInstanceFollowRedirects(httpConfig.isFollowRedirects());
        httpURLConnection.setDoInput(httpConfig.isDoinput());
        httpURLConnection.setDoOutput(httpConfig.isDooutput());
        httpURLConnection.setConnectTimeout(httpConfig.getTimeoutForConnect());
        httpURLConnection.setReadTimeout(httpConfig.getTimeoutForRead());
        MultiValueMap header = httpConfig.getHeaderMap();
        if (!header.containsKey("User-Agent")) {
            header.set("User-Agent", httpConfig.getUserAgent());
        }
        if (!header.containsKey("Cookie")) {
            header.set("Cookie", httpConfig.getCookie());
        }
        if (header.size() > 0) {
            Set<Map.Entry<String, List<String>>> keyAndValue = header.entrySet();
            keyAndValue.forEach(stringListEntry -> {
                int valueLen = stringListEntry.getValue().size();
                for (int i = 0; i < valueLen; i++) {
                    httpURLConnection.addRequestProperty(stringListEntry.getKey(), stringListEntry.getValue().get(i));
                }
            });
        }
    }

    /**
     * desc: 发送请求，返回响应数据
     *
     * @throws IOException,MyException
     **/
    @Override
    public HttpResponse sendRequest(FetcherTask fetcherTask) throws IOException, MyException {
        HttpURLConnection con = null;
        InputStream is = null;
        int code = -1;
        int realRedirectNum = Math.max(0, httpConfig.getMaxRedirect());
        URL url = new URL(fetcherTask.getUrl());
        HttpResponse response = new HttpResponse(url);
        //请求重试次数
        for (int redirect = 0; redirect <= realRedirectNum; redirect++) {
            Proxy proxy = iProxyGain.nextRandom();
            if (proxy == null) {
                con = (HttpURLConnection) url.openConnection();
            } else {
                con = (HttpURLConnection) url.openConnection(proxy);
            }
            con.setRequestMethod(fetcherTask.getMethod());
            this.configHttpRequest(con);
            //post 请求写入信息
            MultiValueMap postBody = httpConfig.getPostBodyMap();
            if (postBody != null && postBody.size() > 0) {
                OutputStream postStream = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(postStream, StandardCharsets.UTF_8));
                String jsonPostBody = JSON.toJSONString(postBody);
                bufferedWriter.write(jsonPostBody);
                bufferedWriter.flush();
                bufferedWriter.close();
            }
            code = con.getResponseCode();
            /*只记录第一次返回的code*/
            if (redirect == 0) {
                response.code(code);
            }
            if (code == HttpURLConnection.HTTP_NOT_FOUND) {
                response.setNotFound(true);
                return response;
            }
            boolean needBreak = false;
            switch (code) {
                case HttpURLConnection.HTTP_MOVED_PERM:
                case HttpURLConnection.HTTP_MOVED_TEMP:
                    response.setRedirect(true);
                    if (redirect == httpConfig.getMaxRedirect()) {
                        throw new MyException("redirect to much time");
                    }
                    String location = con.getHeaderField("Location");
                    if (location == null) {
                        throw new MyException("redirect with no location");
                    }
                    String originUrl = url.toString();
                    url = new URL(url, location);
                    response.setRealUrl(url);
                    LOG.info("redirect from " + originUrl + " to " + url.toString());
                    continue;
                default:
                    needBreak = true;
                    break;
            }
            if (needBreak) {
                break;
            }
        }

        is = con.getInputStream();
        String contentEncoding = con.getContentEncoding();
        if (contentEncoding != null && contentEncoding.equals("gzip")) {
            is = new GZIPInputStream(is);
        }

        byte[] buf = new byte[2048];
        int read;
        int sum = 0;
        int maxsize = httpConfig.getMaxReceiveSize() * 1024 * 1024;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((read = is.read(buf)) != -1) {
            if (maxsize > 0) {
                sum = sum + read;
                if (maxsize > 0 && sum > maxsize) {
                    read = maxsize - (sum - read);
                    bos.write(buf, 0, read);
                    break;
                }
            }
            bos.write(buf, 0, read);
        }
        response.content(bos.toByteArray());
        response.headers(con.getHeaderFields());
        bos.close();
        if (is != null) {
            is.close();
        }
        return response;


    }

    public void removeHeader(String key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        httpConfig.getHeaderMap().remove(key);
    }

    /**
     * desc: 在现有header上添加key value
     **/
    @Override
    public void addHeader(String key, String value) {
        if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
            httpConfig.getHeaderMap().add(key, value);
        }
    }

    @Override
    public void addPostMap(String key, String value) {
        if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
            this.httpConfig.getPostBodyMap().add(key, value);
        }
    }

    /**
     * desc: 设置key value 如果存在则覆盖
     **/
    public void setHeader(String key, String value) {
        if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
            httpConfig.getHeaderMap().set(key, value);
        }
    }
}
