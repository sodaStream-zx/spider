package com.comoms.idbcore;

/**
 * @author 一杯咖啡
 * @desc 数据库提取工具
 * @createTime 2019-01-04-12:15
 */
public interface IGenerator<T> {
    T next();

    T nextWithoutFilter();

    int totalGeneretNum();

    boolean clear();

    boolean close();
}
