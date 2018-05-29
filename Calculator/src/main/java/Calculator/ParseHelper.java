package Calculator;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ParseHelper {
    private HashMap<String, String> cyrillicOperations = new HashMap<>();
    private Collection<String> unaryOperations = new ArrayList<>();
    private Collection<String> binaryOperations = new ArrayList<>();

    public ParseHelper() {
        cyrillicOperations.put("cos(", "кос(");
        cyrillicOperations.put("sin(", "син(");
        cyrillicOperations.put("tg(", "тг(");
        cyrillicOperations.put("sqrt(", "кор(");
        cyrillicOperations.put("abs(", "мод(");
        cyrillicOperations.put("sign(", "зн(");
        cyrillicOperations.put("log2(", "лг(");
        cyrillicOperations.put("rnd(", "окр(");
        cyrillicOperations.put("pow(", "ст(");
        cyrillicOperations.put("log(", "лог(");
        cyrillicOperations.put("max(", "мкс(");
        cyrillicOperations.put("min(", "мин(");
        unaryOperations.add("кос(");
        unaryOperations.add("син(");
        unaryOperations.add("тг(");
        unaryOperations.add("кор(");
        unaryOperations.add("мод(");
        unaryOperations.add("зн(");
        unaryOperations.add("лг(");
        unaryOperations.add("окр(");
        binaryOperations.add("ст(");
        binaryOperations.add("лог(");
        binaryOperations.add("мкс(");
        binaryOperations.add("мин(");
    }

    public String funcToCyrillic(String exp){
        StringBuilder processedExpression = new StringBuilder();
        boolean found;
        for (int i = 0; i < exp.length(); ++i) {
            found = false;
            for (String a : cyrillicOperations.keySet())
                if (i + a.length() <= exp.length() && exp.substring(i, i + a.length()).equals(a)) {
                    processedExpression.append(cyrillicOperations.get(a));
                    i += a.length() - 1;
                    found = true;
                    break;
                }

            if (!found)
                processedExpression.append(exp.charAt(i));
        }
        return processedExpression.toString();
    }

    public String changeVariablesToValues(String expression, HashMap<String, Double> variables){
        StringBuilder expressionWithValues = new StringBuilder();
        for(int i = 0; i < expression.length(); ++i){
            int maxLength = 0;
            Double value = 0.0;

            for(String var : variables.keySet()){
                int length = var.length();
                if(i+length <= expression.length() && expression.substring(i,i+length).equals(var) && length>maxLength){
                    value = variables.get(var);
                    maxLength = length;
                }
            }

            if(maxLength != 0){
                i+=maxLength-1;
                expressionWithValues.append(value.toString());
            }else{
                expressionWithValues.append(expression.charAt(i));
            }
        }
        return expressionWithValues.toString();
    }

    public Pair<Pair<String, String>, Integer> getTwoArguments(String exp, int position) throws ParsingException {
        StringBuilder argument1 = new StringBuilder();
        StringBuilder argument2 = new StringBuilder();
        int brackets = 0;
        int j;
        for (j = position; j < exp.length(); ++j) {
            if (exp.charAt(j) == '(') {
                ++brackets;
            }
            if (exp.charAt(j) == ')') {
                --brackets;
            }
            if (brackets == 0 && exp.charAt(j) == ',') {
                break;
            } else {
                argument1.append(exp.charAt(j));
            }
        }
        int h;
        for (h = j + 1; h < exp.length(); ++h) {
            if (exp.charAt(h) == '(') {
                ++brackets;
            }
            if (exp.charAt(h) == ')') {
                --brackets;
            }
            if (brackets == -1) {
                break;
            } else {
                argument2.append(exp.charAt(h));
            }
            if (h == exp.length() - 1 && brackets >= 0) {
                throw new ParsingException("illegal bracket balance");
            }
        }
        return new Pair<>(new Pair<>(argument1.toString(), argument2.toString()), h);
    }

    public Pair<String, Integer> getOneArgument(String exp, int position) throws ParsingException {
        StringBuilder argument = new StringBuilder();
        int brackets = 0;
        int j;
        for (j = position; j < exp.length(); ++j) {
            if (exp.charAt(j) == '(') {
                ++brackets;
            }
            if (exp.charAt(j) == ')') {
                --brackets;
            }
            if (brackets == -1) {
                break;
            } else {
                argument.append(exp.charAt(j));
            }
            if (j == exp.length() - 1 && brackets >= 0) {
                throw new ParsingException("illegal bracket balance");
            }
        }
        return new Pair<>(argument.toString(), j);
    }

    public Pair<String, Integer> parseOperation(String expression, int currentPosition){
        for (String operation : unaryOperations){
            int end = currentPosition + operation.length();
            if (end <= expression.length() && expression.substring(currentPosition, end).equals(operation))
                return new Pair<>(operation, 1);
        }
        for (String operation : binaryOperations){
            int end = currentPosition + operation.length();
            if (end <= expression.length() && expression.substring(currentPosition, end).equals(operation))
                return new Pair<>(operation, 2);
        }
        return new Pair<>("", 0);
    }
}
