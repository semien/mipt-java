package Calculator;

public class Main {
    public static void main(String[]args) throws ParsingException {
        MyCalculator calc = new MyCalculator();
        System.out.println(calc.calculate("1 - 2"));
    }
}
