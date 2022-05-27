import com.atschool.serialport.controller.SerialPortController;
import com.atschool.serialport.gui.MainFrame;
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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

}
