import cn.hutool.core.util.HexUtil;
import com.atschool.serialport.controller.SerialPortController;
import com.atschool.serialport.gui.MainFrame;
import gnu.io.SerialPort;
import org.junit.Test;

import java.awt.*;
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
        SerialPortController controller = new SerialPortController();
        List<String> ports = controller.findPorts();
        ports.forEach(System.out::println);
    }

    /**
     * 测试
     */
    public static void main(String[] args) {

        SerialPortController controller = new SerialPortController();
        // 打开串口
        SerialPort serialPort = controller.openPort("COM1", 115200, SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        // 往串口发送数据
//        byte[] data = {1, 2, 3};
//        controller.sendToPort(serialPort, data);

//        // 监听串口读取数据
//        controller.addListener(serialPort, (var) -> {
//            byte[] bytes = controller.readFromPort(serialPort);
////            System.out.println(data);
//            System.out.println(HexUtil.encodeHexStr(bytes));
//        });

        /*最终测试*/
        EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

}
