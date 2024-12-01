import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Calculator extends JFrame {
    private final JTextField displayField = new JTextField();
    private final StringBuilder expression = new StringBuilder();

    public Calculator() {
        setTitle("Calculator");
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Display field setup
        displayField.setFont(new Font("Arial", Font.BOLD, 60));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        displayField.setBackground(Color.BLACK);
        displayField.setForeground(Color.WHITE);
        displayField.setBorder(BorderFactory.createEmptyBorder(60, 10, 10, 10));
        add(displayField, BorderLayout.NORTH);

        // Buttons panel setup
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 4, 10, 10));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Button labels
        String[] buttonLabels = {
            "sin", "cos", "tan", "|x|",
            "^", "√x", ")", "C",
            "7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "*",
            "0", ".", "/", "="
        };

        // Add buttons to panel
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setFocusPainted(false);
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 4));

            // Set background color for buttons but it's not working for some reason :(
            if (label.equals("=")) {
                button.setBackground(Color.BLUE); // Blue for "="
                button.setForeground(Color.WHITE); // White text
            } else if (label.equals("√x") || label.equals("C") || label.equals("^") || label.equals(")") ||
                       label.equals("sin") || label.equals("cos") || label.equals("tan") || label.equals("|x|")) {
                button.setBackground(Color.DARK_GRAY); // Dark gray for special buttons
            } else {
                button.setBackground(new Color(50, 50, 50)); // Default gray for other buttons
            }

            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = ((JButton) e.getSource()).getText();

            switch (command) {
                case "C": // Clear
                    expression.setLength(0);
                    displayField.setText("");
                    break;
                case "=": // Evaluate
                    try {
                        double result = evaluateExpression(expression.toString());
                        displayField.setText(expression + " = " + result);
                        expression.setLength(0);
                        expression.append(result); // Store result for further calculations
                    } catch (Exception ex) {
                        displayField.setText("Error");
                        expression.setLength(0);
                    }
                    break;
                case "√x":
                    expression.append(" sqrt(");
                    displayField.setText(expression.toString());
                    break;
                case "^":
                case ")":
                    expression.append(command);
                    displayField.setText(expression.toString());
                    break;
                case "sin":
                    expression.append(" sin(");
                    displayField.setText(expression.toString());
                    break;
                case "cos":
                    expression.append(" cos(");
                    displayField.setText(expression.toString());
                    break;
                case "tan":
                    expression.append(" tan(");
                    displayField.setText(expression.toString());
                    break;
                case "|x|":
                    expression.append(" abs(");
                    displayField.setText(expression.toString());
                    break;
                default: // Numbers, operators, and decimal point
                    expression.append(command);
                    displayField.setText(expression.toString());
                    break;
            }
        }
    }

    // Evaluate mathematical expressions using Shunting-yard and Reverse Polish Notation (RPN)
    private double evaluateExpression(String exp) {
        exp = exp.replace("sqrt", "√")
                 .replace("sin", "s")
                 .replace("cos", "c")
                 .replace("tan", "t")
                 .replace("abs", "|"); // Simplify for easier parsing
        return evaluateRPN(toRPN(exp));
    }

    private String toRPN(String exp) {
        StringBuilder output = new StringBuilder();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                // Add numbers to output
                output.append(c);
            } else if ("√^sct|".indexOf(c) != -1) {
                operators.push(c);
                output.append(" ");
            } else if ("+-*/".indexOf(c) != -1) {
                // Pop higher precedence operators
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    output.append(" ").append(operators.pop());
                }
                operators.push(c);
                output.append(" ");
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                // Pop until '('
                while (!operators.isEmpty() && operators.peek() != '(') {
                    output.append(" ").append(operators.pop());
                }
                operators.pop(); // Remove '('
            }
        }

        // Pop remaining operators
        while (!operators.isEmpty()) {
            output.append(" ").append(operators.pop());
        }

        return output.toString();
    }

    private double evaluateRPN(String rpn) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = rpn.split(" ");

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (token.matches("-?\\d+(\\.\\d+)?")) { // Numbers
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.isEmpty() ? 0 : stack.pop(); // Unary operators like sqrt
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/": stack.push(a / b); break;
                    case "^": stack.push(Math.pow(a, b)); break;
                    case "√": stack.push(Math.sqrt(b)); break;
                    case "s": stack.push(Math.sin(Math.toRadians(b))); break;
                    case "c": stack.push(Math.cos(Math.toRadians(b))); break;
                    case "t": stack.push(Math.tan(Math.toRadians(b))); break;
                    case "|": stack.push(Math.abs(b)); break;
                }
            }
        }
        return stack.pop();
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+': case '-': return 1;
            case '*': case '/': return 2;
            case '^': case '√': case 's': case 'c': case 't': case '|': return 3;
            default: return 0;
        }
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setVisible(true);
    }
}