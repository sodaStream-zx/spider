package com.comoms.ihttp;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-28-16:32
 */
public interface IProxyGain {
    /**
     * desc: 从mysql中加载代理ip
     **/
    void addProxysFromMysql();

    /**
     * desc: 从文件中加载代理ip
     *
     * @param file 代理ip文件
     **/
    void addProxysFromFile(File file) throws IOException;

    /**
     * desc: 从文件中加载代理ip
     **/
    Proxy nextRandom();

    /**
     * desc:添加 ip port
     **/
    void add(String ip, int port);

    /**
     * desc:添加 1.1.1.1.1:8080
     **/
    void add(String proxyStr);
}
