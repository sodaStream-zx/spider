package commoncore.entity.fetcherEntity;

/**
 * @author 一杯咖啡
 * @desc 调度器各个组件状态管理
 * @createTime 2018-12-26-16:49
 */
public class FetcherState {
    private boolean feederRunnning = false;
    private boolean fetcherRunning = false;

    public FetcherState() {
    }

    public boolean isFeederRunnning() {
        return feederRunnning;
    }

    public void setFeederRunnning(boolean feederRunnning) {
        this.feederRunnning = feederRunnning;
    }

    public boolean isFetcherRunning() {
        return fetcherRunning;
    }

    public void setFetcherRunning(boolean fetcherRunning) {
        this.fetcherRunning = fetcherRunning;
    }

    @Override
    public String toString() {
        return "FetcherState{" +
                "\n任务生产者状态：" + (feederRunnning ? "运行中..." : "已停止...") +
                "\n调度器状态=" + (fetcherRunning ? "运行中..." : "已停止...") +
                '}';
    }
}
