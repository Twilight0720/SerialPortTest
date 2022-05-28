package com.atschool.serialport.gui;

import com.atschool.serialport.controller.SerialPortController;
import com.atschool.serialport.listener.DataAvailableListener;
import com.atschool.serialport.utils.DataUtils;
import com.atschool.serialport.utils.ShowUtils;
import gnu.io.SerialPort;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 主界面
 *
 * @author whoo
 * @create 2022-05-28 14:37
 */
@SuppressWarnings("all")
public class MainFrame extends JFrame {

    // 程序界面宽度
    public static final int WIDTH = 530;
    // 程序界面高度
    public static final int HEIGHT = 390;

    // 数据显示区
    public static JTextArea dataView = new JTextArea();
    private JScrollPane scrollDataView = new JScrollPane(dataView);


    // 串口设置面板
    private JPanel serialPortPanel = new JPanel();
    private JLabel serialPortLabel = new JLabel("串口");
    private JLabel baudrateLabel = new JLabel("波特率");
    private JComboBox commChoice = new JComboBox();
    private JComboBox baudrateChoice = new JComboBox();
    private ButtonGroup dataChoice = new ButtonGroup();
    private JRadioButton dataASCIIChoice = new JRadioButton("ASCII", true);
    private JRadioButton dataHexChoice = new JRadioButton("Hex");

    // 操作面板
    private JPanel operatePanel = new JPanel();
    private JTextArea dataInput = new JTextArea();
    private JButton serialPortOperate = new JButton("打开串口");
    private JButton dataSend = new JButton("发送数据");

    // 串口列表
    private List<String> commList = null;
    // 串口对象
    private SerialPort serialport;

    public MainFrame() {
        initView();
        initComponents();
        actionListener();
        initData();
    }

    /**
     * 初始化窗口
     */
    private void initView() {
        // 关闭程序
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // 禁止窗口最大化，固定大小
        setResizable(false);

        // 设置程序窗口居中显示
        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
        this.setLayout(null);

        setTitle("串口通信");
    }

    /**
     * 初始化控件
     */
    private void initComponents() {
        // 数据显示
        dataView.setFocusable(false);
        scrollDataView.setBounds(10, 10, 505, 200);
        add(scrollDataView);

        // 串口设置
        serialPortPanel.setBorder(BorderFactory.createTitledBorder("串口设置"));
        serialPortPanel.setBounds(10, 220, 170, 130);
        serialPortPanel.setLayout(null);
        add(serialPortPanel);

        serialPortLabel.setForeground(Color.gray);
        serialPortLabel.setBounds(10, 25, 40, 20);
        serialPortPanel.add(serialPortLabel);

        commChoice.setFocusable(false);
        commChoice.setBounds(60, 25, 100, 20);
        serialPortPanel.add(commChoice);

        baudrateLabel.setForeground(Color.gray);
        baudrateLabel.setBounds(10, 60, 40, 20);
        serialPortPanel.add(baudrateLabel);

        baudrateChoice.setFocusable(false);
        baudrateChoice.setBounds(60, 60, 100, 20);
        serialPortPanel.add(baudrateChoice);

        dataASCIIChoice.setBounds(20, 95, 55, 20);
        dataHexChoice.setBounds(95, 95, 55, 20);
        dataChoice.add(dataASCIIChoice);
        dataChoice.add(dataHexChoice);
        serialPortPanel.add(dataASCIIChoice);
        serialPortPanel.add(dataHexChoice);

        // 操作
        operatePanel.setBorder(BorderFactory.createTitledBorder("操作"));
        operatePanel.setBounds(200, 220, 315, 130);
        operatePanel.setLayout(null);
        add(operatePanel);

        dataInput.setBounds(25, 25, 265, 50);
        dataInput.setLineWrap(true);
        dataInput.setWrapStyleWord(true);
        operatePanel.add(dataInput);

        serialPortOperate.setFocusable(false);
        serialPortOperate.setBounds(45, 95, 90, 20);
        operatePanel.add(serialPortOperate);

        dataSend.setFocusable(false);
        dataSend.setBounds(180, 95, 90, 20);
        operatePanel.add(dataSend);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //查询所有可用串口
        commList = SerialPortController.findPorts();

        // 检查是否有可用串口，有则加入选项中
        if (commList == null || commList.size() < 1) {
            ShowUtils.warningMessage("没有搜索到有效串口！");
        } else {
            for (String s : commList) {
                commChoice.addItem(s);
            }
        }

        //波特率设置
        baudrateChoice.addItem("9600");
        baudrateChoice.addItem("19200");
        baudrateChoice.addItem("38400");
        baudrateChoice.addItem("57600");
        baudrateChoice.addItem("115200");
    }

    /**
     * 按钮监听事件
     */
    private void actionListener() {
        // 串口
        commChoice.addPopupMenuListener(new PopupMenuListener() {

            /**
             * 检查有效串口，有则加入选项
             * @param e
             */
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                commList = SerialPortController.findPorts();
                // 检查是否有可用串口，有则加入选项中
                if (commList == null || commList.size() < 1) {
                    ShowUtils.warningMessage("没有搜索到有效串口！");
                } else {
                    int index = commChoice.getSelectedIndex();
                    commChoice.removeAllItems();
                    for (String s : commList) {
                        commChoice.addItem(s);
                    }
                    commChoice.setSelectedIndex(index);
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                //
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                //
            }
        });

        // 打开|关闭串口
        serialPortOperate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if ("打开串口".equals(serialPortOperate.getText()) && serialport == null) {
                    openSerialPort(e);
                } else {
                    closeSerialPort(e);
                }
            }
        });

        // 发送数据
        dataSend.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendData(e);
            }
        });
    }

    /**
     * 打开串口
     *
     * @param evt 点击事件
     */
    private void openSerialPort(ActionEvent evt) {
        // 获取串口名称
        String commName = (String) commChoice.getSelectedItem();
        // 获取波特率，默认为9600
        int baudrate = 9600;
        String bps = (String) baudrateChoice.getSelectedItem();
        baudrate = Integer.parseInt(bps);

        // 检查串口名称是否获取正确
        if (commName == null || commName.equals("")) {
            ShowUtils.warningMessage("没有搜索到有效串口！");
        } else {
            serialport = SerialPortController.openPort(commName, baudrate, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            if (serialport != null) {
                dataView.setText("串口已打开" + "\r\n");
                serialPortOperate.setText("关闭串口");

            }
        }

        // 添加串口监听
        SerialPortController.addListener(serialport, new DataAvailableListener() {

            @Override
            public void dataAvailable() {

                byte[] data = null;
                try {
                    if (serialport == null) {
                        ShowUtils.errorMessage("串口对象为空，监听失败！");
                    } else {
                        // 读取串口数据
                        data = SerialPortController.readFromPort(serialport);

                        // 以字符串的形式接收数据
                        if (dataASCIIChoice.isSelected()) {
                            dataView.append(new String(data) + "\r\n");
                        }

                        // 以十六进制的形式接收数据
                        if (dataHexChoice.isSelected()) {
                            dataView.append(DataUtils.byteArrayToHexString(data) + "\r\n");
                        }
                    }
                } catch (Exception e) {
                    ShowUtils.errorMessage(e.toString());
                    // 发生读取错误时显示错误信息后退出系统
                    System.exit(0);
                }
            }
        });
    }

    /**
     * 关闭串口
     *
     * @param evt 点击事件
     */
    private void closeSerialPort(ActionEvent evt) {

        SerialPortController.closePort(serialport);
        dataView.setText("串口已关闭" + "\r\n");
        serialPortOperate.setText("打开串口");
        serialport = null;
    }

    /**
     * 发送数据
     *
     * @param evt 点击事件
     */
    private void sendData(ActionEvent evt) {
        // 待发送数据
        String data = dataInput.getText().toString();

        if (serialport == null) {
            ShowUtils.warningMessage("请先打开串口！");
            return;
        }

        if ("".equals(data) || data == null) {
            ShowUtils.warningMessage("请输入要发送的数据！");
            return;
        }

        // 以字符串的形式发送数据
        if (dataASCIIChoice.isSelected()) {
            SerialPortController.sendToPort(serialport, data.getBytes());
        }

        // 以十六进制的形式发送数据
        if (dataHexChoice.isSelected()) {
            SerialPortController.sendToPort(serialport, DataUtils.hexStr2Byte(data));
        }
    }

    /**
     * 接收数据
     */
    private void receiveData(){
        // 读取串口数据
        byte[] data = SerialPortController.readFromPort(serialport);

        // 以字符串的形式接收数据
        if (dataASCIIChoice.isSelected()) {
            dataView.append(new String(data) + "\r\n");
        }

        // 以十六进制的形式接收数据
        if (dataHexChoice.isSelected()) {
            dataView.append(DataUtils.byteArrayToHexString(data) + "\r\n");
        }
    }
}
