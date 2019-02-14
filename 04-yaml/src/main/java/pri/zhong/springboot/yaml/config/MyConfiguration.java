package pri.zhong.springboot.yaml.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pri.zhong.springboot.yaml.bean.AcmeProperties;
import pri.zhong.springboot.yaml.bean.AppSystemProperties;

@Configuration
@EnableConfigurationProperties({AcmeProperties.class, AppSystemProperties.class})
public class MyConfiguration {
}
