package RestCalculator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    @Bean
    public EmbeddedServletContainerCustomizer customizer(@Value("${ru.RestCalculator.httpPort:8080}") int port) {
        return container -> container.setPort(port);
    }
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}
