package com.trq.xtdemo.common.util.idworker;

import java.util.HashSet;
import java.util.Set;

/**
 * 基于雪花算法生成分布式id（返回一个长度位15的 long类型的数字）。
 * 注意服务器不能发生时钟回拨，即系统时间发生错误，因为雪花算法是基于时间来生成，
 * 所以当发生时钟回拨后会导致出现重复ID的问题。
 * @author trq
 * @version 1.0
 * @since 2022/5/15 15:16
 */
public class SnowFlakeIdWorker {

    /**
     * 开始时间戳，单位毫秒；这里是2021-06-01
     */
    private static final long TW_EPOCH = 1622476800000L;
    /**
     * 机器 ID 所占的位数
     */
    private static final long WORKER_ID_BITS = 5L;
    /**
     * 数据标识 ID 所占的位数
     */
    private static final long DATA_CENTER_ID_BITS = 5L;
    /**
     * 支持的最大机器ID，最大为31
     * PS. Twitter的源码是 -1L ^ (-1L << workerIdBits)；这里最后和-1进行异或运算，由于-1的二进制补码的特殊性，就相当于进行取反。
     *      
     */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    /**
     * 支持的最大机房ID，最大为31
     */
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    /**
     * 序列在 ID 中占的位数
     */
    private static final long SEQUENCE_BITS = 12L;
    /**
     * 机器 ID 向左移12位
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    /**
     * 机房 ID 向左移17位
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    /**
     * 时间截向左移22位
     */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    /**
     * 生成序列的掩码最大值，最大为4095
     */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    /**
     * 工作机器 ID(0~31)
     */
    private final long workerId;
    /**
     * 机房 ID(0~31)
     */
    private final long dataCenterId;
    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;
    /**
     * 上次生成 ID 的时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 用于初始化实例
     */
    private static class IdHolder {
        // 初始化对象
        private static SnowFlakeIdWorker idWorker = new SnowFlakeIdWorker(1, 3);
    }

    /**
     * 创建 ID 生成器的方式一: 使用工作机器的序号(也就是将机房的去掉给机器ID使用)，范围是 [0, 1023]，优点是方便给机器编号
     * @param workerId 工作机器 ID
     */
    public SnowFlakeIdWorker(long workerId) {
        // 计算最大值
        long maxMachineId = (MAX_DATA_CENTER_ID + 1) * (MAX_WORKER_ID + 1) - 1;

        if (workerId < 0 || workerId > maxMachineId) {
            throw new IllegalArgumentException(String.format("Worker ID can't be greater than %d or less than 0", maxMachineId));
        }

        // 取高位部分作为机房ID部分
        this.dataCenterId = (workerId >> WORKER_ID_BITS) & MAX_DATA_CENTER_ID;
        // 取低位部分作为机器ID部分
        this.workerId = workerId & MAX_WORKER_ID;
    }

    /**
     * 创建 ID 生成器的方式二: 使用工作机器 ID 和机房 ID，优点是方便分机房管理
     * @param dataCenterId 机房 ID (0~31)
     * @param workerId     工作机器 ID (0~31)
     */
    public SnowFlakeIdWorker(long dataCenterId, long workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("Worker ID can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("DataCenter ID can't be greater than %d or less than 0", MAX_DATA_CENTER_ID));
        }
        // 配置参数
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 获得下一个 ID(该方法是线程安全的)
     * @return 返回一个长度位15的 long类型的数字
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        // 如果当前时间小于上一次 ID 生成的时间戳，说明发生时钟回拨，为保证ID不重复抛出异常。
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            // 同一时间生成的，则序号+1
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // 毫秒内序列溢出：超过最大值
            if (sequence == 0) {
                // 阻塞到下一个毫秒，获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 时间戳改变，毫秒内序列重置
            sequence = 0L;
        }
        // 上次生成 ID 的时间戳
        lastTimestamp = timestamp;

        // 移位并通过或运算拼到一起
        // 生成 ID 并返回结果：
        // (timestamp - TW_EPOCH) << TIMESTAMP_LEFT_SHIFT           时间戳部分
        // datacenterId << DATACENTER_ID_SHIFT                      数据中心部分
        // machineId << WORKER_ID_SHIFT                             机器标识部分
        // sequence                                                 序列号部分
        return ((timestamp - TW_EPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 当某一毫秒时间内产生的ID数超过最大值则进入等待，
     * 循环判断当前时间戳是否已经变更到下一毫秒，
     * 是则返回最新的时间戳
     * @param lastTimestamp 待比较的时间戳
     * @return 当前时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获取系统当前时间
     * @return 系统当前时间（毫秒）
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 生成分布式id
     * @return
     */
    public static long genID() {
        return IdHolder.idWorker.nextId();
    }

    public static void main(String[] args) {
        Set<Long> idSet = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            long id = SnowFlakeIdWorker.genID();
            idSet.add(id);
            System.out.println(id);
        }
        System.out.println("idSet.size():" + idSet.size());
    }

}
