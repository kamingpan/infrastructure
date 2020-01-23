package com.kamingpan.infrastructure.util.id;

/**
 * 获取id工具
 *
 * @author kamingpan
 * @since 2017-07-26
 */
public class IdWorker {

    private static SnowflakeIdWorker snowflakeIdWorker;

    private Long workId;

    private Long dataCenterId;

    public IdWorker(Long workId, Long dataCenterId) {
        this.workId = null != workId ? workId : 0L;
        this.dataCenterId = null != dataCenterId ? dataCenterId : 0L;
    }

    public final void init() {
        if (null == IdWorker.snowflakeIdWorker) {
            IdWorker.snowflakeIdWorker = new SnowflakeIdWorker(this.workId, this.dataCenterId);
        }
    }

    /**
     * 获取string类型id
     *
     * @return string类型id
     */
    public static String getStringId() {
        return String.valueOf(IdWorker.snowflakeIdWorker.nextId());
    }

    /**
     * 获取long类型id
     *
     * @return long类型id
     */
    public static long getLongId() {
        return IdWorker.snowflakeIdWorker.nextId();
    }

}
