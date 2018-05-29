package RestCalculator;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class BillingDatabaseConfiguration {
    @Bean
    public DataSource billingDataSource(
            @Value("${ru.RestCalculator.jdbcUrl:jdbc:h2:./test1}") String jdbcUrl,
            @Value("${ru.RestCalculator.username:}") String username,
            @Value("${ru.RestCalculator.password:}") String password
    ) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(org.h2.Driver.class.getName());
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }
}
