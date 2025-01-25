// MonthlyCalendar.java
package example;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.UIManager;

public class MonthlyCalendar {
    public MonthlyCalendar() {
        initMonthlyCalendar();
    }

    private void initMonthlyCalendar() {
        GUI gui = new GUI();
        EventProcessor eventProcessor = new EventProcessor(gui);
        
        gui.initGUI(eventProcessor);
        
        gui.validate();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = gui.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        gui.setLocation((screenSize.width - frameSize.width) / 2,
                        (screenSize.height - frameSize.height) / 2);
        gui.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MonthlyCalendar();
    }
}