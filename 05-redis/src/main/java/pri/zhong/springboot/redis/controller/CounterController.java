package pri.zhong.springboot.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pri.zhong.springboot.redis.bean.Counter;

@RequestMapping("/counter")
@RestController
public class CounterController {


    @Autowired
    private Counter counter;

    @GetMapping("/")
    public String visit(){
        String count = counter.get();
        if(StringUtils.isEmpty(count)){
            return "1";
        }
        counter.add();
        return count;
    }

}
