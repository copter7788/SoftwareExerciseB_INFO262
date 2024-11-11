import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Human {
    protected String name;
    protected int age;
    protected double weight;
    protected double height;

    public Human(String name, int age, double weight, double height) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    public void print() {
        System.out.println("Name: " + name + ", Age: " + age + ", Weight: " + weight + "kg, Height: " + height + "cm");
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

    public Student(String name, int age, double weight, double height, int year) {
        super(name, age, weight, height);
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

        int sw = 1;
        
        if (sw == 1) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n========================== Turn on add DATA mode ==========================");
                System.out.println("\nEnter type of human (Human/Student) or 'exit' to stop: ");
                String type = scanner.nextLine();
                
                if (type.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting data entry...");
                    break;
                }

                System.out.println("Enter name: ");
                String name = scanner.nextLine();

                System.out.println("Enter age: ");
                int age = scanner.nextInt();

                System.out.println("Enter weight: ");
                double weight = scanner.nextDouble();

                System.out.println("Enter height: ");
                double height = scanner.nextDouble();
                scanner.nextLine();

                Human newHuman = null;

                if (type.equalsIgnoreCase("Student")) {
                    System.out.println("Enter year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();
                    newHuman = new Student(name, age, weight, height, year);
                } else if (type.equalsIgnoreCase("Human")) {
                    newHuman = new Human(name, age, weight, height);
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

    public static List<Human> loadHumansFromFile(String filename) {
        List<Human> humans = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String type = data[0];
                String name = data[1];
                int age = Integer.parseInt(data[2]);
                double weight = Double.parseDouble(data[3]);
                double height = Double.parseDouble(data[4]);
                
                if (type.equals("Human")) {
                    humans.add(new Human(name, age, weight, height));
                } else if (type.equals("Student")) {
                    int year = Integer.parseInt(data[5]);
                    humans.add(new Student(name, age, weight, height, year));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return humans;
    }

    public static void addHumanToFile(String filename, Human human) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            if (human instanceof Student) {
                Student student = (Student) human;
                bw.write("Student," + student.name + "," + student.getAge() + "," + student.weight + "," + student.height + "," + student.getYear());
            } else {
                bw.write("Human," + human.name + "," + human.getAge() + "," + human.weight + "," + human.height);
            }
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}