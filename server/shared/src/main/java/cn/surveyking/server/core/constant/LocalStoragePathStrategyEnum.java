package cn.surveyking.server.core.constant;


/**
 * 本地存储路径策略
 * @author Jiutwo
 */
public enum LocalStoragePathStrategyEnum {

    /**
     * 所有文件存储在 rootPath 下
     */
    BY_NO("byNo"),
    /**
     * 按照项目的short-id分文件夹存储,例如 rootPath/RyP2rR
     */
    BY_ID("byId"),
    /**
     * 按照上传日期存储，例如 rootPath/2022/06/01
     */
    BY_DATE("byDate");

    private final String strategy;

    LocalStoragePathStrategyEnum(String strategy) {
        this.strategy = strategy;
    }

    public String getStrategy() {
        return strategy;
    }
}
