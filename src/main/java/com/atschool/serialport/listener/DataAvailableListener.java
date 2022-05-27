package com.atschool.serialport.listener;

/**
 * 串口数据可用监听器
 * @author whoo
 * @create 2022-05-28 1:19
 */
public interface DataAvailableListener {

    /**
     * 串口存在有效数据的时候调用
     */
    void dataAvailable();
}