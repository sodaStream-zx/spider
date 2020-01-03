package com.comoms.idbcore;

/**
 *@author Twilight
 * @desc 数据库提取数据过滤器
 **/
public interface IGeneratorFilter<T> {

     T filter(T task);
}
