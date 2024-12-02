public class MathMethods {
    // Method for the first calculation: √(x^2 + y^2)
    public static double calculateFirst(double x, double y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    // Method for the second calculation: x(1 + |sin(x)| + |cos(x)|)
    public static double calculateSecond(double x) {
        return x * (1 + Math.abs(Math.sin(x)) + Math.abs(Math.cos(x)));
    }

    public static void main(String[] args) {
        // Given x = 5, y = 4
        double x = 5;
        double y = 4;

        // Calculate results using the methods
        double result1 = calculateFirst(x, y);
        double result2 = calculateSecond(x);

        // Print the results
        System.out.println("Result of first calculation: " + result1);
        System.out.println("Result of second calculation: " + result2);
    }
}