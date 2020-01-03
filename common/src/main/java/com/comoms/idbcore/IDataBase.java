package com.comoms.idbcore;

/**
 * @author Twilight
 * @desc 数据库
 */
public interface IDataBase<T> {
    /**
     * 总任务队列
     */
    T getSeedList();

    /**
     * 入口任务
     */
    T getDoneList();

    /**
     * 解析入口生成后续任务
     */
    T getUnDoneList();

    /**
     * 重试任务
     */
    T getRedirectDB();

    void clear();
}
