## 04-yaml(Type-safe Configuration Properties)
[Type-safe Configuration Properties](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-typesafe-configuration-properties)



使用`@Value("${property}")`注解这种方式来注入配置属性在一些情况会很难使用, 特别是你有多个properties文件或你的data是有继承关系的,  Spring Boot 为你的应用程序提供了另一种强类型安全的bean管理验证方案,使用`application.yml`来自动装配POJO:

#### 1.在`application.yml`中定义POJO的参数

```yaml
acme:
  remote-address: 192.168.1.1
  enabled: true
  security:
    username: admin
    roles:
    - USER
    - ADMIN
```



#### 2.使用`@ConfigurationProperties`注解来定义java类

为对应的properties添加setter:

```java

@ConfigurationProperties(prefix = "acme")
public class AcmeProperties {

    private boolean enabled;

    private InetAddress remoteAddress;

    private final Security security = new Security();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public InetAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(InetAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public Security getSecurity() {
        return security;
    }

    public static class Security {

        private String username;

        private String password;

        private List<String> roles = new ArrayList<>(Collections.singleton("USER"));

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

    }
}
```

> getter和setter通常是必需的，因为绑定是通过标准的Java Beans属性描述符，就像在Spring MVC中一样。在下列情况下可以省略setter：Maps，只要它们被初始化，就需要一个getter但不一定是setter，因为它们可以被binder改变。可以通过索引访问集合和数组（通常使用YAML）或使用单个逗号分隔值（properties）。在后一种情况下，必须设置一个setter。我们建议始终为这些类型添加setter。如果初始化集合，请确保它不是不可变的（如前面的示例所示）。如果嵌套的POJO属性已初始化（如前面示例中的`Security`字段），则不需要setter。如果您希望binder使用其默认构造函数动态创建实例，则需要一个setter。有些人使用Project Lombok自动添加getter和setter。确保Lombok不为此类型生成任何特定构造函数，因为容器会自动使用它来实例化对象。最后，仅考虑标准Java Bean属性，并且不支持对静态属性的绑定。



#### 3.注册 properties classes

将properties classes列入 `@EnableConfigurationProperties`注解中,如下所示:

```java
@Configuration
@EnableConfigurationProperties(AcmeProperties.class)
public class MyConfiguration {
}
```



#### 4. 自动注入properties class

为了使用`@ConfigurationProperties` 的beans; 你可以将他们以熟悉的方式注入其他bean:

```java
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
        Server server = new Server(this.properties.getRemoteAddress());
        // ...
    }

}
```

> Using `@ConfigurationProperties` also lets you generate metadata files that can be used by IDEs to offer auto-completion for your own keys. See the [Appendix B, *Configuration Metadata*](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#configuration-metadata) appendix for details.





#### PS:

在第2步中, 我们已经知道并确认`AcmeProperties`已经是bean了, 那么可以通过对其添加`@Component`注解来省略`MyConfiguration`类, 如下例所示:

```java
@Component
@ConfigurationProperties(prefix="acme")
public class AcmeProperties {

    // ... see the preceding example

}
```



