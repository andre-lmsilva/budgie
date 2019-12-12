package io.geekmind.budgie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Start up the application.
 *
 * @author Andre Silva
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

}
