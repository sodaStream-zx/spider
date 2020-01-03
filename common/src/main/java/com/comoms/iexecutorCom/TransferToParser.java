package com.comoms.iexecutorCom;

/**
 * @author 一杯咖啡
 * @desc 数据传输接口
 * @createTime 2018-12-27-16:11
 */
public interface TransferToParser<T> {
    void transfer(T t);
}
