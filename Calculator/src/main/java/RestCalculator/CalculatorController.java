package RestCalculator;


import Calculator.MyCalculator;
import Calculator.ParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CalculatorController {
    private static final Logger LOG = LoggerFactory.getLogger(CalculatorController.class);

    private MyCalculator calculator = new MyCalculator();

    @Autowired
    private BillingDao billingDao;

    @RequestMapping(path = "/greeting", method = RequestMethod.GET, produces = "text/plain")
    public String echo(){
        return "Hello, user\n";
    }

    @RequestMapping(path = "newuser/{newUserName}", method = RequestMethod.PUT,
            consumes = "text/plain", produces = "text/plain")
    public String register(@PathVariable String newUserName, @RequestBody String password){
        LOG.debug("User:[" + newUserName + "," + password + "] registered");
        if(billingDao.addUser(newUserName,password,true)){
            LOG.trace("Done");
            return "New user " + newUserName + " have been registered\n";
        } else{
            LOG.trace("Error");
            return "This name is used\n";
        }
    }

    @RequestMapping(path = "/eval", method = RequestMethod.POST, consumes = "text/plain", produces = "text/plain")
    public String eval(Authentication authentication, @RequestBody String expression) throws ParsingException {
        LOG.debug("Evaluation request: [" + expression + "]");
        String currentUserName = authentication.getName();
        HashMap<String, Double> userVar = billingDao.getMapOfVariables(currentUserName);
        double result = calculator.calculate(expression, userVar);
        LOG.trace("Result: " + result);
        return Double.toString(result) + "\n";
    }

    @RequestMapping(path = "/var/{varName}", method = RequestMethod.GET, produces = "text/plain")
    public String getVariable(Authentication authentication, @PathVariable String varName){
        String currentUserName = authentication.getName();
        Variable result = billingDao.getVariable(currentUserName, varName);
        return result.toString()+"\n";
    }

    @RequestMapping(path = "/var/{varName}", method = RequestMethod.DELETE, produces = "text/plain")
    public String deleteVariable(Authentication authentication, @PathVariable String varName){
        String currentUserName = authentication.getName();
        if(billingDao.deleteVariable(currentUserName,varName)){
            return "variable " + varName + " is deleted\n";
        }else{
            return "variable " + varName + " not exists\n";
        }
    }

    @RequestMapping(path = "/var/{varName}", method = RequestMethod.PUT,
        consumes = "text/plain", produces = "text/plain")
    public String addVar(Authentication authentication, @PathVariable String varName,
            @RequestBody String value){
        String currentUserName =authentication.getName();
        billingDao.addVariable(currentUserName,varName,Double.parseDouble(value));
        return "variable " + varName + " = " + value + " added\n";
    }

    @RequestMapping(path = "/var", method = RequestMethod.GET, produces = "text/plain")
    public String getAllVars(Authentication authentication){
        String currentUserName = authentication.getName();
        HashMap<String, Double> vars = billingDao.getMapOfVariables(currentUserName);
        StringBuilder result = new StringBuilder();
        for(Map.Entry<String, Double> i: vars.entrySet()){
            result.append(i.getKey());
            result.append(" = ");
            result.append(i.getValue());
            result.append("\n");
        }
        return vars.toString()+"\n";
    }
}
