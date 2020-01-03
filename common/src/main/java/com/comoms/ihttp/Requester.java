package com.comoms.ihttp;


import commoncore.entity.httpEntity.ResponseData;
import commoncore.entity.requestEntity.FetcherTask;

/**
 *发送请求接口
 */
public interface Requester {
    ResponseData getResponse(FetcherTask fetcherTask) throws Exception;
}
