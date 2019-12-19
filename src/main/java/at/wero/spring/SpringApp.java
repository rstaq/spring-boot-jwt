package at.wero.spring;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringApp {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringApp.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
