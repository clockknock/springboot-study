package pri.zhong.springboot.runner.bean;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Order(180)
public class SecondRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        System.out.println("order is 180:"+this.getClass().getName());

    }
}
