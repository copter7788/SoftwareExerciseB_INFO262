package file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class EventProcessor implements ActionListener {
    private GUI ownerGUI; // เชื่อมโยงกับ GUI
    private String messageFile = "message.txt"; // ไฟล์ข้อความ
    private int sizeOfMessage = 0; // จำนวนข้อความ
    private String[] message; // ข้อความในไฟล์
    private int indexOfMessage = 0; // ข้อความปัจจุบัน
    private Thread continuousThread; // Thread สำหรับโหมด連続
    private Thread ranThread; // Thread สำหรับโหมด連続


    public EventProcessor(GUI gui) {
        ownerGUI = gui;
    }

    public void initProcessing() {
        readMessage();
        if (message != null && message.length > 0) {
            ownerGUI.setMessage(message[0]); // ตั้งค่าข้อความเริ่มต้น
        } else {
            message = new String[] { "デフォルトメッセージがありません。" }; // กรณีไม่มีข้อความ
            ownerGUI.setMessage(message[0]);
        }
    }

    private void readMessage() {
        ArrayList<String> messageList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(messageFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                messageList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sizeOfMessage = messageList.size();
        message = messageList.toArray(new String[0]);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        String command = evt.getActionCommand();
        switch (command) {
            case "次のメッセージ":
                stopMode();
                if (sizeOfMessage > 0) {
                    indexOfMessage = (indexOfMessage + 1) % sizeOfMessage;
                    ownerGUI.setMessage(message[indexOfMessage]);
                }
                break;

            case "前のメッセージ":
                stopMode();
                if (sizeOfMessage > 0) {
                    indexOfMessage = (indexOfMessage - 1 + sizeOfMessage) % sizeOfMessage;
                    ownerGUI.setMessage(message[indexOfMessage]);
                }
                break;

            case "開始":
                stopMode();
                ownerGUI.start();
                break;

            case "停止":
                stopMode();
                ownerGUI.stop();
                break;

            case "速く":
                ownerGUI.setSpeed(ownerGUI.getSpeed() + 1); //  เพิ่มค่าspeed
                break;

            case "遅く":
                ownerGUI.setSpeed(ownerGUI.getSpeed() - 1000); // ลดค่า speed
                break;

            case "連続":
                stopRanMode();
                toggleContinuousMode(); // เปิด/ปิดโหมด連続
                break;

            case "ランダム":
                stopContinuousMode();
                toggleRanMode(); // เปิด/ปิดโหมด連続
                break;

            case "admin{next}":
                if (sizeOfMessage > 0) {
                    indexOfMessage = (indexOfMessage + 1) % sizeOfMessage;
                    ownerGUI.setMessage(message[indexOfMessage]);
                }
                break;

            case "admin{random}":
                if (sizeOfMessage > 0) {
                    // สุ่มข้อความแบบหลีกเลี่ยงข้อความเดิม
                    int randomIndex;
                    do {
                        randomIndex = (int) (Math.random() * sizeOfMessage);
                    } while (randomIndex == indexOfMessage); // หลีกเลี่ยงข้อความเดิม
                    indexOfMessage = randomIndex;
                    ownerGUI.setMessage(message[indexOfMessage]); // ตั้งข้อความใหม่
                }
                break;

        }
    }

// stop mode
    private void stopMode() {
        stopContinuousMode();
        stopRanMode();
        }

// ContinuousMode
    private void toggleContinuousMode() {
        if (ownerGUI.isContinuousMode()) {
            stopContinuousMode();
        } else {
            startContinuousMode();
        }
    }

    private void startContinuousMode() {
        System.err.println("startContinuousMode"); // debug
        ownerGUI.setContinuousMode(true); // เปิดโหมด連続
        continuousThread = new Thread(() -> {
            while (ownerGUI.isContinuousMode() && ownerGUI.isRunning()) {
                try {
                    // ปรับความหน่วงตาม speed หรือ fps
                    int delay = Math.max(1, 1000 / ownerGUI.getSpeed());
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // จัดการ InterruptedException
                }
            }
        });
        continuousThread.start();
    }

    private void stopContinuousMode() {
        ownerGUI.setContinuousMode(false); // ปิดโหมด連続
        if (continuousThread != null && continuousThread.isAlive()) {
            continuousThread.interrupt(); // หยุด Thread
        }
        continuousThread = null;
    }


// RandomMode
private void toggleRanMode() {
    if (ownerGUI.isRanMode()) {
        stopRanMode();
    } else {
        startRanMode();
    }
}

private void startRanMode() {
    System.err.println("startRanMode"); // debug
    ownerGUI.setRanMode(true); // เปิดโหมด連続
    ranThread = new Thread(() -> {
        while (ownerGUI.isRanMode() && ownerGUI.isRunning()) {
            try {
                // ปรับความหน่วงตาม speed หรือ fps
                int delay = Math.max(1, 1000 / ownerGUI.getSpeed());
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // จัดการ InterruptedException
            }
        }
    });
    ranThread.start();
}

private void stopRanMode() {
    ownerGUI.setRanMode(false); // ปิดโหมดรันดอม
    if (ranThread != null && ranThread.isAlive()) {
        ranThread.interrupt(); // หยุด Thread
    }
    ranThread = null;
}
}