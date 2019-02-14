

### 01. 排除auto-configuration的类

如果你发现auto-configuration classes 配置了一个你不想使用的类,可以在`@EnableAutoConfiguration`中禁止它:

```java
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.jdbc.*;
import org.springframework.context.annotation.*;

@Configuration
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class MyConfiguration {
}
```

If the class is not on the classpath, you can use the `excludeName` attribute of the annotation and specify the fully qualified name instead. Finally, you can also control the list of auto-configuration classes to exclude by using the `spring.autoconfigure.exclude` property.

如果class不在classpath中, 你可以使用annotation的`excludeName` 并指定它的全称来代替. 最后, 你也可以通过使用`spring.autoconfigure.exclude`属性来控制被auto-configuration排除的classes.



### 02. auto-restart trigger file

当使用`spring-boot-devtools`时, 你想在特定的时候让程序重启(比如一个文件更新后), 可以使用`spring.devtools.restart.trigger-file`来配置对应文件.



### 03. spring-boot-devtools全局设置

你可以在`$HOME`文件夹下配置一个名为` .spring-boot-devtools.properties` 的文件作为全局设置,在你的机器上使用devtools的所有Spring Boot应用程序都会应用该文件的属性.例如:

**~/.spring-boot-devtools.properties**:

```properties
spring.devtools.reload.trigger-file=.reloadtrigger
```



> 在`.spring-boot-devtools.properties`文件中激活的属性不会影响到[profile-specific configuration files](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-profile-specific-properties)



### 04. Restart Classloader

Spring Boot提供的Restart技术使用了两种类加载器. 没改变的类由 *base* classloader加载(例如第三方的jar包).你正在开发的类由 *restart* classloader加载, 当应用程序重新启动时, *restart* classloader 会被废弃然后创建一个新的. 因为 *base* classloader 已经可用并且加载好了, 所以这样启动会比 "cold starts"快很多.



### 05. 日志级别

所有被支持的日志系统都能在Spring `Environment`中通过`logging.level.<logger-name>=<level>`进行配置(例如:`application.properties`), `level`可以是 TRACE, DEBUG, INFO, WARN, ERROR, FATAL, 或 OFF 中的一个. `rrot` logger可以使用 `logging.level.root`来配置.下面的示例展示了`application.properties`中的一些重要logging设置:

```properties
logging.level.root=WARN
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=ERROR
```

> org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener



### 06. PropertySource order

Spring Boot为了设计出合理的overriding of values, 使用了非常特别的`PropertySource`顺序. Properties的优先级由高到低如下所示:

1. 位于`$HOME`目录的的[Devtools global settings properties](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-devtools-globalsettings)(`~/.spring-boot-devtools.properties` when devtools is active).
2. 位于单元测试上的[`@TestPropertySource`](https://docs.spring.io/spring/docs/5.1.4.RELEASE/javadoc-api/org/springframework/test/context/TestPropertySource.html)  注解.
3. 单元测试的`properties` attribute.  适用于 [`@SpringBootTest`](https://docs.spring.io/spring-boot/docs/2.1.2.RELEASE/api/org/springframework/boot/test/context/SpringBootTest.html) 和 [test annotations for testing a particular slice of your application](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-testing-spring-boot-applications-testing-autoconfigured-tests).
4. 命令行参数.
5. 来自于`SPRING_APPLICATION_JSON`的properties.
6. `ServletConfig` 的初始化参数.
7. `ServletContext`的初始化参数.
8. 来自于`java:comp/env`的 JNDI attributes.
9. Java System properties(`System.getProperties()`).
10. OS环境变量.
11. 一个只在`random.*`拥有properties的 `RandomValuePropertySource`.
12. 打包好的jar包外的[Profile-specific application properties](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-profile-specific-properties)(`application-{profile}.properties` 和 YAML variants).
13. 打包好的jar包内的[Profile-specific application properties](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-profile-specific-properties)(`application-{profile}.properties` 和 YAML variants).
14. 打包好的jar包外的Application properties(`application.properties` and YAML variants).
15. 打包好的jar包内的Application properties(`application.properties` and YAML variants).
16. 在`@Configuration` 类上的[`@PropertySource`](https://docs.spring.io/spring/docs/5.1.4.RELEASE/javadoc-api/org/springframework/context/annotation/PropertySource.html) 注解.
17. 默认properties(通过`SpringApplication.setDefaultProperties`指定).



为了提供一个具体的例子,假设你正在编写一个将要使用`name` property的 `@Component`, 如下面的例子:

```java
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;

@Component
public class MyBean {

    @Value("${name}")
    private String name;

    // ...

}
```

在你的application classpath(例如,在jar包里面),你可以有`application.properties`文件来提供一个`name`的默认值. 当该程序运行在一个新的环境, 可以在jar包外配置一个新的`application.properties`文件来复写`name`. 为了一次独立的测试, 你可以指定命令来加载.(例如:`java -jar app.jar --name="Spring"`).




> `SPRING_APPLICATION_JSON` properties可以通过命令行来提供环境变量.例如,在shell中使用如下命令:
>
> `$ SPRING_APPLICATION_JSON='{"acme":{"name":"test"}}' java -jar myapp.jar`
>
> 在上面的例子中, 在Spring `Environment` 中会有`acme.name=test`. 你也可以在System property中以`spring.application.json`的方式提供JSON, 例如:
>
> `$ java -Dspring.application.json='{"name":"test"}' -jar myapp.jar`. 
>
> 你也可以通过命令行参数的形式提供JSON, 例如:
>
> `$ java -jar myapp.jar --spring.application.json='{"name":"test"}'` 
>
> 你也可以以 JNDI变量的方式提供JSON,例如:`java:comp/env/spring.application.json`.



### 07. 访问命令行参数

默认情况下, `SpringApplication`会将所有命令行参数转换为`property`并将它们添加到Spring `Environment`(也就是说以`—`开始的参数,例如`--server.port=9000`).如前面提到的,命令行参数会优先于任何其他property sources被应用.如果你不想命令行的properties被添加到`Environment`, 你可以通过`SpringApplication.setAddCommandLineProperties(false)`来禁用.



### 08. 应用程序 Propety Files

`SpringApplication`根据`application.properties`文件的位置来加载properties,优先级由高到低如下所示:

1. 当前目录的的`/config`子目录
2. 当前目录
3. classpath的`/config`
4. classpath根目录

根据以上优先级, 优先级高的会覆盖掉优先级低的.





### 09. Relaxed Binding

yaml properties files的属性注入对属性名没那么严格,规则如下例所示:

```java
@ConfigurationProperties(prefix="acme.my-project.person")
public class OwnerProperties {

    private String firstName;

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
```



**Table 24.1. relaxed binding**

| Property                            | Note                                                         |
| ----------------------------------- | ------------------------------------------------------------ |
| `acme.my-project.person.first-name` | Kebab case, 建议用于`.properties`和`.yml`文件中.             |
| `acme.myProject.person.firstName`   | Standard camel case syntax.                                  |
| `acme.my_project.person.first_name` | Underscore notation,可用于`.properties`和`.yml`文件中的另一替换方案. |
| `ACME_MYPROJECT_PERSON_FIRSTNAME`   | Upper case format, 推荐用于使用系统环境变量时.               |

> 注解中的`prefix`值必须使用 kebab case.



**Table 24.2. relaxed binding rules per property source**

| Property Source       | Simple                                                       | List                                                         |
| --------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Properties Files      | Camel case, kebab case, or underscore notation               | 标准list语法,使用`[]`或逗号分隔符                            |
| YAML Files            | Camel case, kebab case, or underscore notation               | 标准 YAML list syntax 或逗号分隔符                           |
| Environment Variables | 使用下划线作为定界符的大写规则. `_` 不应该作为属性名的一部分 | 数值应该被下划线包围,例如:`MY_ACME_1_OTHER = my.acme[1].other` |
| System properties     | Camel case, kebab case, or underscore notation               | 标准list语法,使用`[]`或逗号分隔符                            |

> 如果可以,推荐使用lower-case kebab来存储属性,例如:`my.property-name=acme`. 



当绑定`Map`类型的属性时,如果`key`包含非数字,小写字母或`-`时,你应该使用括号将其括起. 如果`key`没被`[]`括起, 那么它们将会被移除. 例如:

```yml
acme:
  map:
    "[/key1]": value1
    "[/key2]": value2
    /key3: value3
```

绑定的`Map`对象的key会是`/key1`, `/key2` 以及 `key3` ,第三个key的`/`被移除了.



### 10. 标准YAML syntax

使用`-`来分隔的list:

```yaml
acme:
  list:
    - name: my name
      description: my description
    - name: another name
      description: another description
```



map:

```yaml
acme:
  map:
    key1:
      name: dev name 1
    key2:
      name: dev name 2
      description: dev description 2
```



### 11. @ConfigurationProperties Validation

不管classes是否有Spring的`@Validated`注解, Spring Boot都会尝试去验证有`@ConfigurationProperties`的classes. 也可以直接使用 JSR-303 `javax.validation` 到你的configuration class上. 但请确保 JSR-303 的实现类在你的classpath并将其加到你的字段上.

```java
@ConfigurationProperties(prefix="acme")
@Validated
public class AcmeProperties {

    @NotNull
    private InetAddress remoteAddress;

    // ... getters and setters

}
```



> 您还可以通过使用`@Validated`注释创建配置属性的`@Bean`方法来触发验证.

虽然内嵌属性也会在绑定的时候被验证, 但最佳实践是在这些相关字段上添加`@Valid`注解. 这能确保即使没有找到内嵌属性也能触发验证:

```java
@ConfigurationProperties(prefix="acme")
@Validated
public class AcmeProperties {

    @NotNull
    private InetAddress remoteAddress;

    @Valid
    private final Security security = new Security();

    // ... getters and setters

    public static class Security {

        @NotEmpty
        public String username;

        // ... getters and setters

    }

}
```

> 你也可以添加一个自定义的Spring `Validator` -- 创建一个名称为 `configurationPropertiesValidator` 的bean definition method. 该 `@Bean` method应该被声明为`static`. configuration properties validator会在应用程序生命周期的非常前面被创建, 将该`Bean` method 声明为静态的能让它在实例化`@Configuration` class 前就被创建出来. 这样做能避免所有由于过早实例化产生的问题. [property validation sample](https://github.com/spring-projects/spring-boot/tree/v2.1.2.RELEASE/spring-boot-samples/spring-boot-sample-property-validation) 这一案例演示了如何进行配置.



### 12. 添加 active profiles

`spring.profiles.active`属性可以指定哪一个profile正在被使用, 并且可以通过使用命令行来做**切换**.

有时, 如果你有多个想要激活的profiles, 将它们**添加**到active profiles 比替换它们更合适. `spring.profiles.include` 属性能用来添加 active profiles. 如果你想通过Java API的方式来做,可以参考

[setAdditionalProfiles](https://docs.spring.io/spring-boot/docs/2.1.2.RELEASE/api/org/springframework/boot/SpringApplication.html#setAdditionalProfiles-java.lang.String...-). 例如,当你使用` --spring.profiles.active=prod`切换到`prod`环境时,`proddb` 和`prodmq` profiles 也被激活了:

```yaml
---
my.property: fromyamlfile
---
spring.profiles: prod
spring.profiles.include:
  - proddb
  - prodmq
```



### 13. 配置CORS

通过一个自定义的`addCorsMappings(CorsRegistry)` method来注册`WebMvcConfigurer`:

```java
@Configuration
public class MyConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                .allowedOrigins("http://domain2.com")
            	.allowedMethods("PUT", "DELETE")
            	.allowedHeaders("header1", "header2", "header3")
            	.exposedHeaders("header1", "header2")
            	.allowCredentials(true).maxAge(3600);

        // Add more mappings...
            }
        };
    }
}
```





