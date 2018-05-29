package RestCalculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Variable {
    private static final Logger LOG = LoggerFactory.getLogger(Variable.class);

    private String username;
    private String name;
    private Double value;

    public Variable(String username, String name, Double value){
        this.username = username;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return (name + " = " + value.toString());
    }
}
