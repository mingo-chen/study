package cm.study.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Starter {

    private static Logger ILOG = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}
