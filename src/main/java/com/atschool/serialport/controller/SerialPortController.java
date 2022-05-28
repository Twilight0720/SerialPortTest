package com.atschool.serialport.controller;

import cn.hutool.core.util.ArrayUtil;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * 基于RXTX的串口调试工具(环境为JDK8)
 * 串口控制类
 *
 * @author whoo
 * @create 2022-05-27 20:35
 */
public class SerialPortController {

    /**
     * 查找所有可用端口
     *
     * @return 可用端口名称列表
     */
    public List<String> findPorts() {
        //保存所有可用串口
        List<String> portNameList = new ArrayList<>();
        //获得当前所有可用串口
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();

        //将可用串口名添加到portNameList
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
//            System.out.println(portName);
            portNameList.add(portName);
        }

        /*JDK11才有迭代器*/
//        Iterator<CommPortIdentifier> iterator = portList.asIterator();
//        while (iterator.hasNext()) {
//            String portName = iterator.next().getName();
//            System.out.println(portName);
//            portNameList.add(portName);
//        }

        //返回
        return portNameList;
    }

    /**
     * 打开串口
     *
     * @param portName 端口名
     * @param baudRate 波特率
     * @param data     数据位
     * @param stop     停止位
     * @param verify   校验位
     * @return 返回对应被打开的串口
     */
    public SerialPort openPort(String portName, Integer baudRate, Integer data,
                                      Integer stop, Integer verify) {

        SerialPort serialPort = null;
        //通过端口名识别端口
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            //打开端口，并给端口取名，设置超时时间为2s
            CommPort commPort = portIdentifier.open(portName, 2000);

            //判断是否为串口
            if (commPort instanceof SerialPort) {
                serialPort = (SerialPort) commPort;
                //设置串口波特率等参数（数据位data、停止位stop、校验位verify）
                serialPort.setSerialPortParams(baudRate, data, stop, verify);
                System.out.println("串口打开成功");
            } else {
//                System.out.println(portName + "不是串口，打开失败");
                throw new NoSuchPortException();
            }
        } catch (NoSuchPortException e) {
            throw new RuntimeException(e);
        } catch (PortInUseException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedCommOperationException e) {
            throw new RuntimeException(e);
        }
        return serialPort;
    }

    /**
     * 往串口发送数据
     *
     * @param serialPort 串口对象
     * @param data       待发送数据
     */
    public void sendToPort(SerialPort serialPort, byte[] data) {
        //串口为空，返回
        if (serialPort == null) {
            return;
        }

//        OutputStream os = null;
        PrintStream ps = null;
        try {
            //获取输出流
            ps = new PrintStream(serialPort.getOutputStream(), true, "utf-8");
            ps.print(new String(data));
//            os = serialPort.getOutputStream();
//            os.write(data);
//            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            if (os != null) {
//                try {
//                    os.close();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * 从串口读取数据
     *
     * @param serialPort 当前已建立连接的SerialPort对象
     * @return 读取到的数据（字节形式）
     */
    public byte[] readFromPort(SerialPort serialPort) {

        byte[] result = null;
        if (serialPort == null) {
            return result;
        }

        InputStream is = null;
        try {
            //获取输入流
            is = serialPort.getInputStream();

            //设置缓冲区大小为一个字节
            byte[] buffer = new byte[1];

//            int len;
//            while((len = is.read(buffer)) != -1){
//                result = ArrayUtil.addAll(result, buffer);
//            }

//            int len = is.available();//获取数据长度
//            while (len != 0) {
//                is.read(buffer);
//                result = ArrayUtil.addAll(result, buffer);
//                len = is.available();//需要重新获取数据长度
//            }
            /*简化*/
            while (is.available() != 0){
                is.read(buffer);
                result = ArrayUtil.addAll(result, buffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }

    /**
     * 添加监听器
     *
     * @param serialPort 串口对象
     * @param listener   串口事件监听器，意味着串口存在有效数据监听
     */
    public void addListener(SerialPort serialPort, SerialPortEventListener listener) {
        //串口空，返回
        if (serialPort == null) {
            return;
        }
        try {
            //给串口添加监听器
            serialPort.addEventListener(listener);
            //设置当有数据到达时唤醒监听接收线程
            serialPort.notifyOnDataAvailable(true);
            //设置当通信中断时唤醒中断线程
            serialPort.notifyOnBreakInterrupt(true);

        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭串口
     *
     * @param serialPort 串口名
     */
    public void closePort(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.close();
        }
    }
}
