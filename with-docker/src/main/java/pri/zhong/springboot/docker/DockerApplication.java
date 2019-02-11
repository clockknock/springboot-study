package pri.zhong.springboot.docker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DockerApplication {

    @Value("${env}")
    private String env;
    @Value("${command}")
    private String command;

    @RequestMapping("/")
    public String home() {

        return "env: " + env + ", command: " + command;
    }

    public static void main(String[] args) {
        SpringApplication.run(DockerApplication.class, args);
    }

}