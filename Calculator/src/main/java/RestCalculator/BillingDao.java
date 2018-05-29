package RestCalculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;

@Repository
public class BillingDao {
    private static final Logger LOG = LoggerFactory.getLogger(BillingDao.class);

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource, false);
        initSchema();
    }

    public void initSchema() {
        LOG.trace("Initializing schema");
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS billing");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS billing.users "
                + "(username VARCHAR PRIMARY KEY, password VARCHAR, enabled BOOLEAN)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS billing.variables "
                + "(username VARCHAR, name VARCHAR, value DOUBLE)");
        addUser("user","1234",true);
    }

    public User getUser(String username) throws EmptyResultDataAccessException {
        LOG.trace("Querying for user " + username);
        return jdbcTemplate.queryForObject(
                "SELECT username, password, enabled FROM billing.users WHERE username = ?",
                new Object[] {username}, (resultSet, i) -> new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("enabled")));
    }

    boolean addUser(String username, String password, boolean enabled) {
        try {
            getUser(username);
            return false;
        } catch (EmptyResultDataAccessException e) {
            jdbcTemplate.update("INSERT INTO billing.users VALUES (?,?,?)",
                    new Object[] {username, password, enabled});
            return true;
        }
    }

    public Variable getVariable(String username, String name) {
        return jdbcTemplate.queryForObject(
                "SELECT username, name, value FROM billing.variables WHERE username = ? AND name = ?",
                new Object[] {username, name}, (resultSet, i) -> new Variable(
                        resultSet.getString("username"),
                        resultSet.getString("name"),
                        resultSet.getDouble("value")));
    }

    public void addVariable(String username, String name, Double value){
        try{
            jdbcTemplate.update("DELETE FROM billing.variables WHERE username = ? AND name = ?",
                    new Object[]{username, name});
            jdbcTemplate.update("INSERT INTO billing.variables VALUES (?,?,?)",
                    new Object[]{username, name, value});
        }catch (EmptyResultDataAccessException e){
            jdbcTemplate.update("INSERT INTO billing.variables VALUES (?,?,?)",
                    new Object[]{username, name, value});
        }
    }

    HashMap<String, Double> getMapOfVariables(String username) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT username, name, value FROM billing.variables WHERE username = ?",
                    new Object[] {username}, (resultSet, i) -> {
                        HashMap<String, Double> vars = new HashMap<>();
                        while (true) {
                            vars.put(resultSet.getString("name"),
                                    resultSet.getDouble("value"));
                            if (!resultSet.next()) {
                                break;
                            }
                        }
                        return  vars;
                    });
        } catch (EmptyResultDataAccessException e) {
            HashMap<String, Double> vars = new HashMap<>();
            return vars;
        }
    }

    boolean deleteVariable(String username, String name) {
        try {
            jdbcTemplate.update("DELETE FROM billing.variables WHERE username = ? AND name = ?",
                    new Object[] {username, name});
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
