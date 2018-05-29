package Calculator;

import javafx.util.Pair;
import java.util.HashMap;
import java.util.Stack;


public class MyCalculator {
    public MyCalculator() {}

    private ParseHelper parseHelper = new ParseHelper();

    private boolean isDelimiter(char c) { //check that the symbol is delimiter
        return c == ' ' || c == '\n' || c == '\t';
    }

    private boolean isSimpleOperation(char c) { // check that the symbol is operator
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private boolean isDigit(char c) { //check that the symbol is digit or point
        return (c >= '0' && c <= '9' || c == '.');
    }

    private int priority(char operation) { // return priority of operation
        switch (operation) {
            case '+':
                return 1;
            case '-':
                return 1;
            case '*':
                return 2;
            case '/':
                return 2;
            case '~':
                return 3;
            case '#':
                return 3;
            default:
                return -1;
        }
    }

    private double getValueFromString(String str) throws ParsingException { // convert string >> double
        double value;
        try {
            value = Double.parseDouble(str);
        } catch (Exception e){
            throw new ParsingException("Double parse error");
        }
        return value;
    }

    private void makeSimpleOperation(Stack<Double> numbers, char operation) { //do 1 operation from stack
        if (operation == '#' || operation == '~') {
            double argument = numbers.pop();
            switch (operation) {
                case '#':
                    numbers.push(argument);
                    break;
                case '~':
                    numbers.push(-argument);
                    break;
                default:
                    break;
            }
        } else {
            double arg2 = numbers.pop();
            double arg1 = numbers.pop();
            switch (operation) {
                case '+':
                    numbers.push(arg1 + arg2);
                    break;
                case '-':
                    numbers.push(arg1 - arg2);
                    break;
                case '*':
                    numbers.push(arg1 * arg2);
                    break;
                case '/':
                    numbers.push(arg1 / arg2);
                    break;
                default:
                    break;
            }
        }
    }

    private double calculateFunction(String operation, Double arg1, Double arg2){
        if (operation.equals("лог2(")){
            return Math.log(arg1) / Math.log(2);
        }
        if (operation.equals("син(")){
            return Math.sin(arg1);
        }
        if (operation.equals("кос(")){
            return Math.cos(arg1);
        }
        if (operation.equals("окр(")){
            return Math.round(arg1);
        }
        if (operation.equals("кор(")){
            return Math.sqrt(arg1);
        }
        if (operation.equals("мод(")){
            return Math.abs(arg1);
        }
        if (operation.equals("тг(")){
            return Math.tan(arg1);
        }
        if (operation.equals("зн(")){
            return Math.signum(arg1);
        }
        if (operation.equals("мин(")){
            return Math.min(arg1, arg2);
        }
        if (operation.equals("мкс(")){
            return Math.max(arg1, arg2);
        }
        if (operation.equals("лог(")){
            return Math.log(arg1) / Math.log(arg2);
        }
        if (operation.equals("ст(")){
            return Math.pow(arg1, arg2);
        }
        return 0.0;
    }

    public double calculateExpression(String expression) throws ParsingException {
        boolean unaryExpected = true; //wait unary operation
        int numbersAfterLastOperation = 0; //how many numbers were after last operation
        int bracketBalance = 0;

        Stack<Double> numbers = new Stack<>(); // stack of values
        Stack<Character> operations = new Stack<>(); //stack of operations

        for (int i = 0; i < expression.length(); ++i) {

            if (isDelimiter(expression.charAt(i))) {
                continue;
            }

            Pair<String, Integer> operation = parseHelper.parseOperation(expression, i);

            if (operation.getValue() == 1){
                Pair<String, Integer> argument = parseHelper.getOneArgument(expression, i + operation.getKey().length());
                Double argumentValue = calculate(argument.getKey());
                numbers.add(calculateFunction(operation.getKey(), argumentValue, 0.0));
                i = argument.getValue(); // shift
            }
            if (operation.getValue() == 2){
                Pair<Pair<String, String>, Integer> arguments = parseHelper.getTwoArguments(expression, i + operation.getKey().length());
                Double argument1Value = calculate(arguments.getKey().getKey());
                Double argument2Value = calculate(arguments.getKey().getValue());
                numbers.add(calculateFunction(operation.getKey(), argument1Value, argument2Value));
                i = arguments.getValue(); // shift
            }
            if (operation.getValue() != 0){
                unaryExpected = false;
                numbersAfterLastOperation = 1;
                continue;
            }

            if (expression.charAt(i) == '(') {
                operations.push(expression.charAt(i));
                numbersAfterLastOperation = 0;
                unaryExpected = true;
                ++bracketBalance;
                continue;
            }

            if (expression.charAt(i) == ')') {
                if (numbersAfterLastOperation == 0) { // check that operation is not last meaning symbol in brackets
                    throw new ParsingException("illegal expression");
                }
                if (bracketBalance == 0) {     //checking bracket balance
                    throw new ParsingException("balance error");
                }
                while (operations.peek() != '(') {
                    makeSimpleOperation(numbers, operations.pop());
                }
                operations.pop();
                unaryExpected = false;
                --bracketBalance;
                continue;
            }

            if (isSimpleOperation(expression.charAt(i))) {
                char currentOperation = expression.charAt(i);
                if (unaryExpected) {
                    switch (currentOperation) {
                        case '+':
                            currentOperation = '#'; //unaryPlus
                            break;
                        case '-':
                            currentOperation = '~'; //unaryMinus
                            break;
                        default:
                            throw new ParsingException("Illegal sequence");
                    }
                } else if (numbersAfterLastOperation == 0) { //check that we have not two unary operations
                    throw new ParsingException("double operation");
                }

                while (!operations.empty() && !numbers.empty() && (priority(operations.peek()) >= priority(currentOperation))) {
                    makeSimpleOperation(numbers, operations.pop());
                }
                operations.push(currentOperation);
                unaryExpected = false;
                numbersAfterLastOperation = 0;
                continue;
            }

            if (isDigit(expression.charAt(i))) {
                String number = "";
                number += expression.charAt(i++);
                while (i < expression.length() && isDigit(expression.charAt(i))) {
                    number += expression.charAt(i++);
                }
                --i;
                numbers.push(getValueFromString(number));
                unaryExpected = false;
                numbersAfterLastOperation = 1;
                continue;
            }
            throw new ParsingException("Illegal symbol"); // if we did not meet familiar symbol
        }

        if (bracketBalance != 0) { //checking bracket balance
            throw new ParsingException("balance error");
        }
        while (!operations.empty()) {
            makeSimpleOperation(numbers, operations.pop());
        }
        if (numbers.size() != 1) { //check illegal expression
            throw new ParsingException("illegal expression");
        }
        return numbers.peek();
    }

    public double calculate(String expression, HashMap<String,Double> additionalVariables) throws ParsingException{
        if (expression == null || expression.length() == 0) {
            throw new ParsingException("empty expression");
        }
        expression = parseHelper.funcToCyrillic(expression);
        if (additionalVariables != null)
            expression = parseHelper.changeVariablesToValues(expression, additionalVariables);
        return calculateExpression(expression);
    }

    public double calculate(String expression) throws ParsingException{
        return calculate(expression, null);
    }
}
