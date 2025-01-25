// EventProcessor.java
package example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JButton;

public class EventProcessor implements ActionListener {
    public EventProcessor(GUI gui) {
        ownerGUI = gui;
    }

    public void initCalendar() {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("JST"));
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        calendar.set(year, month - 1, 1);
        int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        ownerGUI.displayCalendar(year, month, startDayOfWeek);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        JButton b = (JButton) evt.getSource();
        if (b.getText().equals("<")) month--;
        if (b.getText().equals(">")) month++;
        if (b.getText().equals("<<")) year--;
        if (b.getText().equals(">>")) year++;
        if (b.getText().equals("現在の月に戻る")) { 
            initCalendar(); 
            return;
        }
        if (month < 1) {
            year--;
            month = 12;
        }
        if (month > 12) {
            year++;
            month = 1;
        }
        calendar.set(year, month - 1, 1);
        int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        ownerGUI.displayCalendar(year, month, startDayOfWeek);
    }

    private GUI ownerGUI;
    private Calendar calendar;
    private int year;
    private int month;
}