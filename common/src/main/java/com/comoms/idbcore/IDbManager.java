package com.comoms.idbcore;

/**
 * @author 一杯咖啡
 * @desc 数据库管理
 * @createTime 2019-01-04-13:05
 */
public interface IDbManager {
    boolean isDBExists();

    void clear();

    void open();

    void close();

    void merge();
}
