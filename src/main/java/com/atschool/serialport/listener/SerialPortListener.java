package com.atschool.serialport.listener;

import com.atschool.serialport.utils.ShowUtils;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * 串口监听器
 * @author whoo
 * @create 2022-05-28 1:01
 */
public class SerialPortListener implements SerialPortEventListener {

    //串口数据可用监听器
    private DataAvailableListener listener;

    public SerialPortListener(DataAvailableListener listener) {
        this.listener = listener;
    }

    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {
            case SerialPortEvent.DATA_AVAILABLE:
                //串口存在有效数据
                if (listener != null) {
                    listener.dataAvailable();
                }
                break;
            //输出缓冲区已清空
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            //清除待发送数据
            case SerialPortEvent.CTS:
                break;
            //待发送数据准备好了
            case SerialPortEvent.DSR:
                break;
            //振铃指示
            case SerialPortEvent.RI:
                break;
            //载波检测
            case SerialPortEvent.CD:
                break;
            //溢位（溢出）错误
            case SerialPortEvent.OE:
                break;
            //奇偶校验错误
            case SerialPortEvent.PE:
                break;
            //帧错误
            case SerialPortEvent.FE:
                break;
            //通讯中断
            case SerialPortEvent.BI:
                ShowUtils.errorMessage("与串口设备通讯中断");
                break;

            default:
                break;
        }
    }
}
