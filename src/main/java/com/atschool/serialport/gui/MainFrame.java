package com.atschool.serialport.gui;

import com.atschool.serialport.controller.SerialPortController;
import com.atschool.serialport.utils.DataUtils;
import com.atschool.serialport.utils.ShowUtils;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

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
    public static final int WIDTH = 525;
    // 程序界面高度
    public static final int HEIGHT = 310;

    // 数据显示区
    public static JTextArea dataView = new JTextArea();
    private JScrollPane scrollDataView = new JScrollPane(dataView);


    // 串口设置面板
    private JPanel serialPortPanel = new JPanel();
    private JLabel serialPortLabel = new JLabel("串口号");
    private JLabel baudrateLabel = new JLabel("波特率");
    private JComboBox commChoice = new JComboBox();
    private JComboBox baudrateChoice = new JComboBox();
    private ButtonGroup dataChoice = new ButtonGroup();
    private JRadioButton dataASCIIChoice = new JRadioButton("ASCII", true);
    private JRadioButton dataHexChoice = new JRadioButton("Hex");

    // 操作面板
    private JPanel operatePanel = new JPanel();
    private JTextArea dataInput = new JTextArea();
    private JButton serialPortOperate = new JButton("开启串口");
    private JButton dataSend = new JButton("发送");

    // 串口列表
    private List<String> commList = null;
    // 串口对象
    private SerialPort serialport;

    private SerialPortController controller;

    public MainFrame() {
        controller = new SerialPortController();
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

        setTitle("串口调试助手");
    }

    /**
     * 初始化控件
     */
    private void initComponents() {
        // 数据显示
        dataView.setFocusable(false);
        scrollDataView.setBounds(10, 10, 320, 130);
        add(scrollDataView);

        // 串口设置
        serialPortPanel.setBorder(BorderFactory.createTitledBorder("串口设置"));
        serialPortPanel.setBounds(340, 10, 170, 130);
        serialPortPanel.setLayout(null);
        add(serialPortPanel);

        serialPortLabel.setBounds(10, 25, 40, 20);
        serialPortPanel.add(serialPortLabel);
        serialPortLabel.setForeground(Color.BLACK);

        commChoice.setFocusable(false);
        commChoice.setBackground(Color.WHITE);
        commChoice.setBounds(60, 25, 100, 20);
        serialPortPanel.add(commChoice);

        baudrateLabel.setForeground(Color.BLACK);
        baudrateLabel.setBounds(10, 60, 40, 20);
        serialPortPanel.add(baudrateLabel);

        baudrateChoice.setFocusable(false);
        baudrateChoice.setBounds(60, 60, 100, 20);
        baudrateChoice.setBackground(Color.WHITE);
        serialPortPanel.add(baudrateChoice);

        dataASCIIChoice.setBounds(20, 95, 55, 20);
        dataHexChoice.setBounds(95, 95, 55, 20);
        dataChoice.add(dataASCIIChoice);
        dataChoice.add(dataHexChoice);
        serialPortPanel.add(dataASCIIChoice);
        serialPortPanel.add(dataHexChoice);

        // 操作
        operatePanel.setBorder(BorderFactory.createTitledBorder("操作"));
        operatePanel.setBounds(10, 150, 500, 110);
        operatePanel.setLayout(null);
        add(operatePanel);

        dataInput.setBounds(15, 25, 300, 65);
        dataInput.setLineWrap(true);
        dataInput.setWrapStyleWord(true);
        operatePanel.add(dataInput);

        serialPortOperate.setFocusable(false);
        serialPortOperate.setBackground(Color.WHITE);
        serialPortOperate.setBounds(350, 25, 120, 25);
        operatePanel.add(serialPortOperate);

        dataSend.setFocusable(false);
        dataSend.setBackground(Color.WHITE);
        dataSend.setBounds(350, 65, 120, 25);
        operatePanel.add(dataSend);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //查询所有可用串口
        commList = controller.findPorts();

        // 检查是否有可用串口，有则加入选项中
        if (commList == null || commList.size() < 1) {
            ShowUtils.warningMessage("没有搜索到有效串口！");
        } else {
            for (String s : commList) {
                commChoice.addItem(s);
            }
        }

        //波特率设置
        baudrateChoice.addItem("4800");
        baudrateChoice.addItem("9600");
        baudrateChoice.addItem("19200");
        baudrateChoice.addItem("38400");
        baudrateChoice.addItem("57600");
        baudrateChoice.addItem("115200");
        baudrateChoice.addItem("128000");
        baudrateChoice.addItem("256000");
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
                commList = controller.findPorts();
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
        // 获取波特率，默认为4800
        int baudrate = 4800;
        String bps = (String) baudrateChoice.getSelectedItem();
        baudrate = Integer.parseInt(bps);

        // 检查串口名称是否获取正确
        if (commName == null || commName.equals("")) {
            ShowUtils.warningMessage("没有搜索到有效串口！");
        } else {
            serialport = controller.openPort(commName, baudrate, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            if (serialport != null) {
                serialPortOperate.setText("关闭串口");
            }
        }

        // 添加串口监听
        controller.addListener(serialport, new SerialPortEventListener() {

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                byte[] data = null;
                try {
                    if (serialport == null) {
                        ShowUtils.errorMessage("串口对象为空，监听失败！");
                    } else {
                        // 读取串口数据
                        data = controller.readFromPort(serialport);

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

        controller.closePort(serialport);

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
            controller.sendToPort(serialport, data.getBytes());
        }

        // 以十六进制的形式发送数据
        if (dataHexChoice.isSelected()) {
            controller.sendToPort(serialport, DataUtils.hexStr2Byte(data));
        }
    }

    /**
     * 接收数据
     */
    private void receiveData(){
        // 读取串口数据
        byte[] data = controller.readFromPort(serialport);

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
