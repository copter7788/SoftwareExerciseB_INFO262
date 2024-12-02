// hw 4.1
public class MathMethods {
    public static double calculateFirst(double x, double y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public static double calculateSecond(double x) {
        return x * (1 + Math.abs(Math.sin(x)) + Math.abs(Math.cos(x)));
    }

    public static void main(String[] args) {
        double x = 5;
        double y = 4;

        double result1 = calculateFirst(x, y);
        double result2 = calculateSecond(x);

        System.out.println("Result of first calculation: " + result1);
        System.out.println("Result of second calculation: " + result2);
    }
}