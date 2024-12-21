package file;

import javax.swing.UIManager;
import java.awt.*;

public class MessageBoard {
    public MessageBoard() {
        initMessageBoard();
    }

    private void initMessageBoard() {
        // สร้างอินสแตนซ์ GUI และ EventProcessor
        GUI gui = new GUI();
        EventProcessor eventProcessor = new EventProcessor(gui);

        // เริ่มต้น GUI
        gui.initGUI(eventProcessor);
        eventProcessor.initProcessing();

        // ตรวจสอบขนาดหน้าจอและ GUI
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = gui.getSize();

        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }

        // จัดหน้าต่างให้อยู่ตรงกลาง
        gui.setLocation(
            (screenSize.width - frameSize.width) / 2,
            (screenSize.height - frameSize.height) / 2
        );

        // แสดงหน้าต่าง GUI
        gui.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new MessageBoard();
    }
}