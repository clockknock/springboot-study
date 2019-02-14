package pri.zhong.springboot.yaml;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pri.zhong.springboot.yaml.bean.AppSystemProperties;
import pri.zhong.springboot.yaml.services.MyService;

import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private AppSystemProperties appSystemProperties;
    @Autowired
    private MyService myService;

    @Test
    public void contextLoads() {
        Duration readTimeout = appSystemProperties.getReadTimeout();
        System.out.println("readTimeout:"+readTimeout);
        Duration sessionTimeout = appSystemProperties.getSessionTimeout();
        System.out.println("sessionTimeout:"+sessionTimeout);
    }

}

