package spider.spiderCore.entitys;


import commoncore.entity.requestEntity.FetcherTask;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 用于存储多个CrawlDatum的数据结构
 *
 * @author hu
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FetcherTasks implements Serializable {

    private LinkedList<FetcherTask> dataList = new LinkedList<FetcherTask>();

    public FetcherTasks() {
    }

    public FetcherTasks(List<String> list) {
        list.stream().map(FetcherTask::new).forEach(this::addTask);
    }

    public FetcherTasks(List<String> list, int deepPath) {
        list.stream().map(x -> new FetcherTask(x, deepPath)).forEach(this::addTask);
    }

    public void add(String url) {
        FetcherTask task = new FetcherTask(url);
        this.dataList.add(task);
    }

    public FetcherTasks addTask(FetcherTask task) {
        this.dataList.add(task);
        return this;
    }

    public FetcherTasks addList(List<FetcherTask> tasks) {
        tasks.stream().forEach(this::addTask);
        return this;
    }

    public FetcherTasks addFetcherTasks(FetcherTasks tasks) {
        tasks.getStream().forEach(this::addTask);
        return this;
    }

    public FetcherTask get(int index) {
        return dataList.get(index);
    }

    public int size() {
        return dataList.size();
    }

    public FetcherTask remove(int index) {
        return dataList.remove(index);
    }

    public boolean remove(FetcherTask task) {
        return dataList.remove(task);
    }

    public void clear() {
        dataList.clear();
    }

    public boolean isEmpty() {
        return dataList.isEmpty();
    }

    public int indexOf(FetcherTask task) {
        return dataList.indexOf(task);
    }

    public Stream<FetcherTask> getStream() {
        return this.dataList.stream();
    }

    @Override
    public String toString() {
        return dataList.toString();
    }

}
