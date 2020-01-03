package com.comoms.iexecutorCom;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-07-15:23
 */
@FunctionalInterface
public interface ISimpleParse<T, D> {
    T parseLinks(D dataObj);
}
