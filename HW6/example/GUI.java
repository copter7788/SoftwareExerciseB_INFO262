// GUI.java
package example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame {
    public GUI() {}

    // GUIの初期化
    public void initGUI(EventProcessor eventProcessor) {
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        this.setSize(new Dimension(widthOfDisplayPanel + 23, heightOfDisplayPanel + 350));
        this.setTitle("Monthly Calendar");
        buttonPanel.add(lastYearButton);
        buttonPanel.add(lastMonthButton);
        buttonPanel.add(currentMonthButton);
        buttonPanel.add(nextMonthButton);
        buttonPanel.add(nextYearButton);
        buttonPanel.setBackground(Color.GREEN);
        lastMonthButton.addActionListener(eventProcessor);
        nextMonthButton.addActionListener(eventProcessor);
        lastYearButton.addActionListener(eventProcessor);
        nextYearButton.addActionListener(eventProcessor);
        currentMonthButton.addActionListener(eventProcessor);
        contentPane.add(displayPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        eventProcessor.initCalendar();
    }

    // DisplayFieldにカレンダーを表示
    public void displayCalendar(int year, int month, int startDayOfWeek) {
        this.year = year;
        this.month = month;
        this.dayOfWeek = startDayOfWeek;
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
            daysOfMonth[1] = 29;
        else
            daysOfMonth[1] = 28;
        repaint();
    }

    private JPanel contentPane;
    private JPanel buttonPanel = new JPanel();
    private DisplayPanel displayPanel = new DisplayPanel();
    private JButton lastMonthButton = new JButton("<");
    private JButton nextMonthButton = new JButton(">");
    private JButton lastYearButton = new JButton("<<");
    private JButton nextYearButton = new JButton(">>");
    private JButton currentMonthButton = new JButton("現在の月に戻る");
    private int widthOfDisplayPanel = 500;
    private int heightOfDisplayPanel = 462;
    private int widthOfCalendarFrame = widthOfDisplayPanel - 20;
    private int heightOfCalendarFrame = heightOfDisplayPanel - 90;
    private int year;
    private int month;
    private int dayOfWeek;
    private Font font = new Font("Courier", Font.BOLD, 30);
    int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    String[] week = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

    // 内部クラス
    class DisplayPanel extends JPanel {
        public void paintComponent(Graphics g) {
            int dayOfWeek1 = dayOfWeek;
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, widthOfDisplayPanel, heightOfDisplayPanel);
            g.setColor(Color.GREEN);
            g.drawRect(0, 0, widthOfDisplayPanel, heightOfDisplayPanel);
            g.setColor(Color.WHITE);
            g.fillRect(10, 80, widthOfCalendarFrame, heightOfCalendarFrame);
            g.setColor(Color.BLUE);
            g.setFont(font);
            g.drawString(year + "年" + month + "月", widthOfDisplayPanel / 3, 35);
            g.setColor(Color.BLACK);
            for (int i = 0; i < 7; i++)
                g.drawString(week[i], 15 + i * widthOfCalendarFrame / 7, 70);
            g.setColor(Color.GRAY);
            for (int y = 0; y < 6; y++)
                for (int x = 0; x < 7; x++)
                    g.drawRect(x * widthOfCalendarFrame / 7 + 10, y * heightOfCalendarFrame / 6 + 80,
                            widthOfCalendarFrame / 7, heightOfCalendarFrame / 6);

            Calendar today = Calendar.getInstance();
            int currentYear = today.get(Calendar.YEAR);
            int currentMonth = today.get(Calendar.MONTH) + 1;
            int currentDay = today.get(Calendar.DAY_OF_MONTH);

            int springEquinox = calculateSpringEquinox(year);
            int autumnEquinox = calculateAutumnEquinox(year);

            int row = 0;
            List<String> holidaysInMonth = new ArrayList<>();
            List<Holiday> substituteHolidays = getSubstituteHolidays(year);
            List<Holiday> happyMondayHolidays = getHappyMondayHolidays(year);

            for (int d = 1; d <= daysOfMonth[month - 1]; d++) {
                int x = (dayOfWeek1 - 1) * widthOfCalendarFrame / 7 + 10;
                int y = row * heightOfCalendarFrame / 6 + 80;

                if (year == currentYear && month == currentMonth && d == currentDay) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(x, y, widthOfCalendarFrame / 7, heightOfCalendarFrame / 6);
                }

                String holidayName = getHolidayName(month, d, springEquinox, autumnEquinox);
                boolean isSubstituteHoliday = isSubstituteHoliday(month, d, substituteHolidays);
                boolean isHappyMondayHoliday = isHappyMondayHoliday(month, d, happyMondayHolidays);

                if (holidayName != null) {
                    g.setColor(Color.RED);
                    holidaysInMonth.add(String.format("%d月%d日: %s", month, d, holidayName));
                } else if (isSubstituteHoliday) {
                    g.setColor(Color.RED);
                    holidaysInMonth.add(String.format("%d月%d日: 振替休日", month, d));
                } else if (isHappyMondayHoliday) {
                    for (Holiday holiday : happyMondayHolidays) {
                        if (holiday.month == month && holiday.day == d) {
                            holidaysInMonth.add(String.format("%d月%d日: ハッピーマンデー (%s)", month, d, holiday.name));
                        }
                    }
                    g.setColor(Color.RED);
                } else if (dayOfWeek1 == 1) {
                    g.setColor(Color.RED);
                } else if (dayOfWeek1 == 7) {
                    g.setColor(Color.BLUE);
                } else {
                    g.setColor(Color.BLACK);
                }

                g.drawString(Integer.toString(d), x + widthOfCalendarFrame / 14,
                        y + heightOfCalendarFrame / 6 - 15);

                dayOfWeek1++;
                if (dayOfWeek1 > 7) {
                    row++;
                    dayOfWeek1 = 1;
                }
            }

            // Display holidays below the calendar
            g.setFont(new Font("Courier", Font.BOLD, 14));
            g.setColor(Color.BLACK);
            int yOffset = heightOfCalendarFrame + 110;
            g.drawString("祝日:", 20, yOffset);
            for (int i = 0; i < holidaysInMonth.size(); i++) {
                g.drawString(holidaysInMonth.get(i), 20, yOffset + (i + 1) * 20);
            }
        }

        private String getHolidayName(int month, int day, int springEquinox, int autumnEquinox) {
            for (Holiday holiday : holidays) {
                if (holiday.month == month && holiday.day == day) {
                    return holiday.name;
                }
            }
            if (month == 3 && day == springEquinox) {
                return "春分の日";
            }
            if (month == 9 && day == autumnEquinox) {
                return "秋分の日";
            }
            return null;
        }

        private boolean isSubstituteHoliday(int month, int day, List<Holiday> substituteHolidays) {
            for (Holiday holiday : substituteHolidays) {
                if (holiday.month == month && holiday.day == day) {
                    return true;
                }
            }
            return false;
        }

        private List<Holiday> getSubstituteHolidays(int year) {
            List<Holiday> substituteHolidays = new ArrayList<>();
            for (Holiday holiday : holidays) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, holiday.month - 1, holiday.day);
                if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    cal.add(Calendar.DATE, 1);
                    substituteHolidays.add(new Holiday(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
                            "振替休日"));
                }
            }
            return substituteHolidays;
        }

        private boolean isHappyMondayHoliday(int month, int day, List<Holiday> happyMondayHolidays) {
            for (Holiday holiday : happyMondayHolidays) {
                if (holiday.month == month && holiday.day == day) {
                    return true;
                }
            }
            return false;
        }

        private List<Holiday> getHappyMondayHolidays(int year) {
            List<Holiday> happyMondayHolidays = new ArrayList<>();
            happyMondayHolidays.add(new Holiday(1, calculateHappyMonday(year, 1, 2), "成人の日"));
            happyMondayHolidays.add(new Holiday(7, calculateHappyMonday(year, 7, 3), "海の日"));
            happyMondayHolidays.add(new Holiday(9, calculateHappyMonday(year, 9, 3), "敬老の日"));
            happyMondayHolidays.add(new Holiday(10, calculateHappyMonday(year, 10, 2), "体育の日"));
            return happyMondayHolidays;
        }

        private int calculateHappyMonday(int year, int month, int week) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, 1);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int offset = (Calendar.MONDAY - dayOfWeek + 7) % 7; // Find first Monday
            return 1 + offset + (week - 1) * 7;
        }
    }

    private static int calculateSpringEquinox(int year) {
        if (year < 1851)
            return 21;
        if (year <= 1899)
            return (int) (19.8277 + 0.242194 * (year - 1980) - (int) ((year - 1983) / 4));
        if (year <= 1979)
            return (int) (20.8357 + 0.242194 * (year - 1980) - (int) ((year - 1983) / 4));
        if (year <= 2099)
            return (int) (20.8431 + 0.242194 * (year - 1980) - (int) ((year - 1980) / 4));
        if (year <= 2150)
            return (int) (21.8510 + 0.242194 * (year - 1980) - (int) ((year - 1980) / 4));
        return 21;
    }

    private static int calculateAutumnEquinox(int year) {
        if (year < 1851)
            return 23;
        if (year <= 1899)
            return (int) (22.2588 + 0.242194 * (year - 1980) - (int) ((year - 1983) / 4));
        if (year <= 1979)
            return (int) (23.2588 + 0.242194 * (year - 1980) - (int) ((year - 1983) / 4));
        if (year <= 2099)
            return (int) (23.2488 + 0.242194 * (year - 1980) - (int) ((year - 1980) / 4));
        if (year <= 2150)
            return (int) (24.2488 + 0.242194 * (year - 1980) - (int) ((year - 1980) / 4));
        return 23;
    }

    private static List<Holiday> holidays = new ArrayList<>();
    static {
        holidays.add(new Holiday(1, 1, "元旦"));
        holidays.add(new Holiday(2, 11, "建国記念の日"));
        holidays.add(new Holiday(4, 29, "昭和の日"));
        holidays.add(new Holiday(5, 3, "憲法記念日"));
        holidays.add(new Holiday(5, 4, "みどりの日"));
        holidays.add(new Holiday(5, 5, "子供の日"));
        holidays.add(new Holiday(11, 3, "文化の日"));
        holidays.add(new Holiday(11, 23, "勤労感謝の日"));
        holidays.add(new Holiday(12, 23, "天皇誕生日"));
    }

    private static class Holiday {
        int month;
        int day;
        String name;

        Holiday(int month, int day, String name) {
            this.month = month;
            this.day = day;
            this.name = name;
        }
    }
}