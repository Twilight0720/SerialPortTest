import cn.hutool.core.util.HexUtil;
import com.atschool.serialport.controller.SerialPortController;
import com.atschool.serialport.gui.MainFrame;
import gnu.io.SerialPort;
import org.junit.Test;

import java.util.List;

/**
 * 测试工具类
 *
 * @author whoo
 * @create 2022-05-27 21:57
 */
public class SerialPortTest {

    /**
     * 测试查找所有的可用端口名
     */
    @Test
    public void testFindPorts(){
        List<String> ports = SerialPortController.findPorts();
        ports.forEach(System.out::println);
    }


    /**
     * 测试
     */
    public static void main(String[] args) {

//        // 打开串口
//        SerialPort serialPort = SerialPortController.openPort("COM1", 115200, SerialPort.DATABITS_8,
//                SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
//
////        // 往串口发送数据
////        byte[] data = {1, 2, 3};
////        SerialPortController.sendToPort(serialPort, data);
//
//        // 监听串口读取数据
//        SerialPortController.addListener(serialPort, () -> {
//            byte[] data = SerialPortController.readFromPort(serialPort);
////            System.out.println(data);
//            System.out.println(HexUtil.encodeHexStr(data));
//        });

        // 测试可用端口
 //       SerialPortUtils.listPortName().forEach(o -> System.out.println(o));

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
