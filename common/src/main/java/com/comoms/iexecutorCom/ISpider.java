package com.comoms.iexecutorCom;

import com.comoms.exceptionHandle.MyException;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-08-14:55
 */
public interface ISpider {
    void loadInnerConfig() throws MyException;

    void injectSeeds() throws MyException;

    boolean startSpider() throws MyException;

    boolean stopSpider();

    void afterStopSpider() throws MyException;

    void loadOuterConfig() throws MyException;
}
