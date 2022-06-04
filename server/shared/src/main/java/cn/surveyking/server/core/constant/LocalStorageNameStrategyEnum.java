package cn.surveyking.server.core.constant;

/**
 * 本地存储文件名策略
 * @author Jiutwo
 */
public enum LocalStorageNameStrategyEnum {
    /**
     * 序列号加原文件名，例如：1653122982531_fileName.jpg
     */
    SEQ_ADN_ORIGINAL_NAME("seqAndOriginalName"),
    /**
     * 原文件名+序列号，例如：fileName_1653122982531.jpg
     */
    ORIGINAL_NAME_AND_SEQ("originalNameAndSeq"),
    /**
     *  序列号（项目启动时间戳的自增），例如：1653122982531.jpg
     */
    SEQ("seq"),
    /**
     * 去除短杠'-'的UUID，例如：8328839eae07f93443733bc7b0468f04.jpg
     */
    UUID("uuid");

    private final String strategy;

    LocalStorageNameStrategyEnum(String strategy) {
        this.strategy = strategy;
    }

    public String getStrategy() {
        return strategy;
    }
}
