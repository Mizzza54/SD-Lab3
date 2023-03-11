package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Gerasimov
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public StockDao stockDao() {
        return new StockDao(
                new HashMap<>(
                        Map.of(
                                "ifmo", new Stock("ifmo", 100, 200),
                                "life", new Stock("life", 200, 300),
                                "sd", new Stock("sd", 400, 500)
                        )
                )
        );
    }
}
