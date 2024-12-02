// hw 4.2 
// go to hw 4.1 --> https://github.com/copter7788/SoftwareExerciseB_INFO262/blob/main/HW4/MathMethods.java
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

        // Display field
        displayField.setFont(new Font("Arial", Font.BOLD, 60));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        displayField.setBackground(Color.BLACK);
        displayField.setForeground(Color.WHITE);
        displayField.setBorder(BorderFactory.createEmptyBorder(60, 10, 10, 10));
        add(displayField, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 4, 10, 10));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Labels for buttons
        String[] buttonLabels = {
            "sin", "cos", "tan", "|x|",
            "^", "√x", ")", "C",
            "7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "*",
            "0", ".", "/", "="
        };

        // Add buttons
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setFocusPainted(false);
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 4));

            // Set colors
            if (label.equals("=")) {
                button.setBackground(Color.BLUE);
                button.setForeground(Color.WHITE);
            } else if ("√xC^)sin cos tan |x|".contains(label)) {
                button.setBackground(Color.DARK_GRAY);
            } else {
                button.setBackground(new Color(50, 50, 50));
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

            // Action handler
            switch (command) {
                case "C": // Clear all
                    expression.setLength(0);
                    displayField.setText("");
                    break;
                case "=": // Solve stuff
                    try {
                        double result = evaluateExpression(expression.toString());
                        displayField.setText(expression + " = " + result);
                        expression.setLength(0);
                        expression.append(result);
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
                default: // Numbers/operators
                    expression.append(command);
                    displayField.setText(expression.toString());
                    break;
            }
        }
    }

    // Parse and calculate
    private double evaluateExpression(String exp) {
        exp = exp.replace("sqrt", "√")
                 .replace("sin", "s")
                 .replace("cos", "c")
                 .replace("tan", "t")
                 .replace("abs", "|");
        return evaluateRPN(toRPN(exp));
    }

    // Convert to postfix
    private String toRPN(String exp) {
        StringBuilder output = new StringBuilder();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                output.append(c);
            } else if ("√^sct|".indexOf(c) != -1) {
                operators.push(c);
                output.append(" ");
            } else if ("+-*/".indexOf(c) != -1) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    output.append(" ").append(operators.pop());
                }
                operators.push(c);
                output.append(" ");
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    output.append(" ").append(operators.pop());
                }
                operators.pop();
            }
        }

        while (!operators.isEmpty()) {
            output.append(" ").append(operators.pop());
        }

        return output.toString();
    }

    // Evaluate postfix
    private double evaluateRPN(String rpn) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = rpn.split(" ");

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (token.matches("-?\\d+(\\.\\d+)?")) {
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.isEmpty() ? 0 : stack.pop();
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

    // Operator priority
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