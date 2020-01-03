package com.comoms.ihttp;

import com.spider.spiderCore.entitys.HttpResponse;
import commoncore.entity.requestEntity.FetcherTask;
import commoncore.exceptionHandle.MyException;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * @author 一杯咖啡
 * @desc 发送请求，包装请求
 * @createTime 2018-12-28-0:22
 */
public interface ISendRequest<T> {
    /**
     * desc: 配置请求
     **/
    void configHttpRequest(HttpURLConnection httpURLConnection);

    /**
     * desc: 获取返回值
     **/
    HttpResponse sendRequest(FetcherTask fetcherTask) throws Exception;

    /**
     * desc:转换 封装 响应数据
     **/
    T converterResponsePage(FetcherTask fetcherTask) throws IOException, MyException;

    void addHeader(String key, String value);

    void addPostMap(String key, String value);
}
