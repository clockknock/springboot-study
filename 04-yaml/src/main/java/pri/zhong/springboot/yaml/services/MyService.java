package pri.zhong.springboot.yaml.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pri.zhong.springboot.yaml.bean.AcmeProperties;

import javax.annotation.PostConstruct;

@Service
public class MyService {

    private final AcmeProperties properties;

    @Autowired
    public MyService(AcmeProperties properties) {
        this.properties = properties;
    }

    //...

    @PostConstruct
    public void openConnection() {
//        Server server = new Server(this.properties.getRemoteAddress());
        System.out.println(this.properties.getSecurity().getUsername());
        // ...
    }

}