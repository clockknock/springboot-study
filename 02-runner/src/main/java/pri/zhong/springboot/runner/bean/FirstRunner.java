package pri.zhong.springboot.runner.bean;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Order(100)
public class FirstRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        System.out.println("order is 100:"+this.getClass().getName());
        List<String> nonOptionArgs = args.getNonOptionArgs();
        for (String nonOptionArg : nonOptionArgs) {
            System.out.println("nonOptionArg:" + nonOptionArg);
        }

        Set<String> optionNames = args.getOptionNames();
        for (String optionName : optionNames) {
            System.out.println("optionName:" + optionName);

            List<String> values = args.getOptionValues(optionName);
            for (String value : values) {
                System.out.println("\t value:" + value);
            }
        }

    }
}
