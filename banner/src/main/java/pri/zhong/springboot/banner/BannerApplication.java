package pri.zhong.springboot.banner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BannerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BannerApplication.class);
        app.run(args);
    }

}

