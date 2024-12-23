package file;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class GUI extends JFrame implements Runnable {
    private int fps = 120; // ค่า FPS 
    private boolean isContinuousMode = false; // โหมดเล่นต่อเนื่อง
    private boolean isRanMode = false; // โหมดเล่นต่อเนื่อง
    private JPanel contentPane;
    private JPanel controlPanel;
    private JButton startButton, stopButton, previousButton, nextButton, fasterButton, slowerButton, continuousButton, randomButton;
    private DisplayPanel displayPanel;

    private String message = "デフォルトメッセージがありません。"; // ข้อความเริ่มต้น
    private Font font;
    private int widthOfMessage;
    private int pointer;
    private int speed = 3;
    private Thread thread;

    private final int widthOfDisplayPanel = 900;
    private final int heightOfDisplayPanel = 50;

    public GUI() {
        setTitle("Simple Message Tape");
        setSize(new Dimension(widthOfDisplayPanel, heightOfDisplayPanel + 75));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ตั้งค่าแผงควบคุม (Control Panel)
        controlPanel = new JPanel();
        startButton = new JButton("開始");
        stopButton = new JButton("停止");
        previousButton = new JButton("前のメッセージ");
        nextButton = new JButton("次のメッセージ");
        fasterButton = new JButton("速く");
        slowerButton = new JButton("遅く");
        continuousButton = new JButton("連続");
        randomButton = new JButton("ランダム");

        // เพิ่มปุ่มไปที่ Control Panel
        controlPanel.add(startButton);
        controlPanel.add(previousButton);
        controlPanel.add(nextButton);
        controlPanel.add(stopButton);
        controlPanel.add(fasterButton);
        controlPanel.add(slowerButton);
        controlPanel.add(continuousButton);
        controlPanel.add(randomButton);

        // ตั้งค่าแผงแสดงผล (Display Panel)
        displayPanel = new DisplayPanel();

        // ตั้งค่ากรอบหน้าต่าง (Content Pane)
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(controlPanel, BorderLayout.NORTH);
        contentPane.add(displayPanel, BorderLayout.CENTER);

        // ตั้งค่าฟอนต์
        font = new Font("MS ゴシック", Font.BOLD, heightOfDisplayPanel * 4 / 5);
    }

    public void initGUI(EventProcessor eventProcessor) {
        startButton.addActionListener(eventProcessor);
        stopButton.addActionListener(eventProcessor);
        previousButton.addActionListener(eventProcessor);
        nextButton.addActionListener(eventProcessor);
        fasterButton.addActionListener(eventProcessor);
        slowerButton.addActionListener(eventProcessor);
        continuousButton.addActionListener(eventProcessor);
        randomButton.addActionListener(eventProcessor);
    }

    public void setMessage(String mes) {
        this.message = mes != null ? mes : "メッセージがありません。";
        widthOfMessage = getFontMetrics(font).stringWidth(message);
        pointer = widthOfDisplayPanel; // รีเซ็ต pointer ทุกครั้งที่เปลี่ยนข้อความ
    }

    public void setSpeed(int newSpeed) {
        this.speed = Math.max(1, newSpeed); // ความเร็วขั้นต่ำ = 1
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setFPS(int newFPS) {
        this.fps = Math.max(1, newFPS); // ค่า FPS ขั้นต่ำ = 1
    }

    public int getFPS() {
        return this.fps;
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        thread = null;
    }

    @Override
    public void run() {
        while (thread != null) {
            try {
                Thread.sleep(1000 / fps); // ปรับความหน่วงตาม FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isContinuousMode) {
                pointer -= speed;
                if (pointer < -widthOfMessage) {
                    pointer = widthOfDisplayPanel;
                    nextMessage(); // เปลี่ยนข้อความในโหมด連続
                }
            }
            else if (isRanMode) {
                pointer -= speed;
                if (pointer < -widthOfMessage) {
                    pointer = widthOfDisplayPanel;
                    ranMessage(); // เปลี่ยนข้อความในโหมดรันดอม
                }
            }
            else {
                pointer -= speed;
                if (pointer < -widthOfMessage) {
                    pointer = widthOfDisplayPanel;
                }
                }

            displayPanel.repaint();
        }
    }

    private void nextMessage() {
        ActionListener[] listeners = nextButton.getActionListeners();
        if (listeners.length > 0) {
            ActionEvent event = new ActionEvent(nextButton, ActionEvent.ACTION_PERFORMED, "admin{next}");
            listeners[0].actionPerformed(event);
        }
    }

    private void ranMessage() {
        ActionListener[] listeners = nextButton.getActionListeners();
        if (listeners.length > 0) {
            ActionEvent event = new ActionEvent(nextButton, ActionEvent.ACTION_PERFORMED, "admin{random}");
            listeners[0].actionPerformed(event);
        }
    }

    public boolean isRunning() {
        return thread != null;
    }

    public boolean isContinuousMode() {
        return isContinuousMode;
    }

    public void setContinuousMode(boolean mode) {
        isContinuousMode = mode;
    }
    public boolean isRanMode() {
        return isRanMode;
    }

    public void setRanMode(boolean mode) {
        isRanMode = mode;
    }

    public String getCurrentMessage() {
        return this.message; // คืนค่าข้อความปัจจุบัน
    }

    // DisplayPanel เป็นคลาสย่อยที่แสดงข้อความเลื่อน
    private class DisplayPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, widthOfDisplayPanel, heightOfDisplayPanel);

            if (message != null && !message.isEmpty()) { // ตรวจสอบว่า message ไม่ null
                g.setFont(font);
                g.setColor(Color.RED);
                g.drawString(message, pointer, heightOfDisplayPanel * 4 / 5);
            }
        }
    }
}