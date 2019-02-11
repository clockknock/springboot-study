package pri.zhong.springboot.propertyfiles.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class PropertyReader implements ApplicationRunner {
    @Value("${greeting}")
    private String greeting;


    @Override
    public void run(ApplicationArguments args)   {
        System.out.println("greeting:"+greeting);
    }
}
