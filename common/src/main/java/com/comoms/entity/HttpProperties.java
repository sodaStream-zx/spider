package com.comoms.entity;

import org.springframework.boot.json.GsonJsonParser;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.List;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2019-01-02-12:02
 */
public class HttpProperties implements Serializable {
    private static final long serialVersionUID = 3L;
    private int httpId;

    private MultiValueMap headerMap;
    private MultiValueMap postBodyMap;

    public static void main(String[] args) {
        String keval = "zxx:a,s,d;xcc:s,f,g,h;fff:z,s,f,g";
        String[] list = keval.split(";");
        for (String x : list) {
            List<Object> li = new GsonJsonParser().parseList(x);
            System.out.println(x);
        }
    }

    public int getHttpId() {
        return httpId;
    }

    public void setHttpId(int httpId) {
        this.httpId = httpId;
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

    @Override
    public String toString() {
        return "HttpProperties{" +
                "httpId=" + httpId +
                ", headerMap=" + headerMap +
                ", postBodyMap=" + postBodyMap +
                '}';
    }

}
