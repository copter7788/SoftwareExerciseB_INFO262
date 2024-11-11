import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Human {
    protected int age;
    protected double weight;
    protected double height;

    public Human(int age, double weight, double height) {
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    public void print() {
        System.out.println("Age: " + age + ", Weight: " + weight + "kg, Height: " + height + "cm");
    }

    public void incrementAge() {
        age += 1;
    }

    public int getAge() {
        return age;
    }
}

class Student extends Human {
    private int year;

    public Student(int age, double weight, double height, int year) {
        super(age, weight, height);
        this.year = year;
    }

    @Override
    public void incrementAge() {
        super.incrementAge();
        year += 1;
    }

    @Override
    public void print() {
        super.print();
        System.out.println("Year: " + year);
    }

    public int getYear() {
        return year;
    }
}

public class HumanHierarchy {
    
    public static void main(String[] args) {
        List<Human> humans = loadHumansFromFile("data.txt");

        System.out.println("\nCurrent status:");
        for (Human human : humans) {
            human.print();
        }

        System.out.println("\nAfter one year:");
        for (Human human : humans) {
            human.incrementAge();
            human.print();
        }

        // กำหนดตัวแปร sw เพื่อควบคุมการเพิ่มข้อมูล
        int sw = 1; // ตั้งค่าเป็น 1 เพื่อเปิดใช้งานการเพิ่มข้อมูล (ตั้งค่าเป็น 0 เพื่อปิดการเพิ่มข้อมูล)
        
        if (sw == 1) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                // รับข้อมูลจากผู้ใช้
                System.out.println("\n========================== Turn on add DATA mode ==========================");
                System.out.println("\nEnter type of human (Human/Student) or 'exit' to stop: ");
                String type = scanner.nextLine();
                
                // ออกจากลูปถ้าผู้ใช้พิมพ์ "exit"
                if (type.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting data entry...");
                    break;
                }

                System.out.println("Enter age: ");
                int age = scanner.nextInt();

                System.out.println("Enter weight: ");
                double weight = scanner.nextDouble();

                System.out.println("Enter height: ");
                double height = scanner.nextDouble();
                scanner.nextLine();  // Clear the newline

                Human newHuman = null;

                if (type.equalsIgnoreCase("Student")) {
                    System.out.println("Enter year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();  // Clear the newline
                    newHuman = new Student(age, weight, height, year);
                } else if (type.equalsIgnoreCase("Human")) {
                    newHuman = new Human(age, weight, height);
                }

                if (newHuman != null) {
                    addHumanToFile("data.txt", newHuman);
                    System.out.println("New data has been added to the file.");
                } else {
                    System.out.println("Invalid type entered.");
                }
            }
            
            scanner.close();
        } else {
            System.out.println("Data addition is disabled (sw = 0).");
        }
    }

    // ฟังก์ชันสำหรับอ่านข้อมูลจากไฟล์
    public static List<Human> loadHumansFromFile(String filename) {
        List<Human> humans = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String type = data[0];
                int age = Integer.parseInt(data[1]);
                double weight = Double.parseDouble(data[2]);
                double height = Double.parseDouble(data[3]);
                
                if (type.equals("Human")) {
                    humans.add(new Human(age, weight, height));
                } else if (type.equals("Student")) {
                    int year = Integer.parseInt(data[4]);
                    humans.add(new Student(age, weight, height, year));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return humans;
    }

    // ฟังก์ชันสำหรับบันทึกข้อมูลใหม่ลงในไฟล์
    public static void addHumanToFile(String filename, Human human) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            if (human instanceof Student) {
                Student student = (Student) human;
                bw.write("Student," + student.getAge() + "," + student.weight + "," + student.height + "," + student.getYear());
            } else {
                bw.write("Human," + human.getAge() + "," + human.weight + "," + human.height);
            }
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}