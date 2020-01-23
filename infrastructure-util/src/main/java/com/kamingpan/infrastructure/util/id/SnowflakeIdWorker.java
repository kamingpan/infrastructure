package com.kamingpan.infrastructure.util.id;

import lombok.extern.slf4j.Slf4j;

/**
 * id生成器
 *
 * @author kamingpan
 * @since 2017-05-10
 */
@Slf4j
public class SnowflakeIdWorker {

    /**
     * 开始时间截 (2000-01-01)
     * 时间起始标记点，作为基准，一般取系统的最近时间（一旦确定不能变动，否则修改前后生成出来的id会无法正确排序）
     */
    private static final long START_STAMP = 946656000000L;

    /**
     * 机器标识id所占的位数
     */
    private static final long WORKER_ID_BIT = 5L;

    /**
     * 数据标识id所占的位数
     */
    private static final long DATA_CENTER_ID_BIT = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BIT); // 由“-1L ^ (-1L << WORKER_ID_BIT)”替换而来

    /**
     * 支持的最大数据标识id，结果是31
     */
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BIT); // 由“-1L ^ (-1L << DATA_CENTER_ID_BIT)”替换而来

    /**
     * 序列在id中占的位数
     */
    private static final long SEQUENCE_BIT = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = SEQUENCE_BIT;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long dataCenterIdShift = SEQUENCE_BIT + WORKER_ID_BIT;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = SEQUENCE_BIT + WORKER_ID_BIT + DATA_CENTER_ID_BIT;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = ~(-1L << SEQUENCE_BIT); // 由“-1L ^ (-1L << SEQUENCE_BIT)”替换而来

    /**
     * 工作机器ID(0~31)
     */
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private long dataCenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param dataCenterId 数据中心ID (0~31)
     */
    public SnowflakeIdWorker(long workerId, long dataCenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("data center id can't be greater than %d or less than 0", MAX_DATA_CENTER_ID));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }


    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // 时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        // 上次生成ID的时间截
        lastTimestamp = timestamp;

        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - START_STAMP) << timestampLeftShift) | (dataCenterId << dataCenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }


    /**
     * 测试
     */
    public static void main(String[] args) {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        for (int i = 0; i < 1000; i++) {
            long id = idWorker.nextId();
            log.debug(String.valueOf(id));
        }
    }

}
