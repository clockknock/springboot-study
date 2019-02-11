# springboot-study

跟着 [Reference Guide](https://docs.spring.io/spring-boot/docs/2.1.2.RELEASE/reference/htmlsingle/)
查漏补缺的学习项目

### 1. 排除auto-configuration的类

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



### 2. auto-restart trigger file

当使用`spring-boot-devtools`时, 你想在特定的时候让程序重启(比如一个文件更新后), 可以使用`spring.devtools.restart.trigger-file`来配置对应文件.



### 3. 全局设置

你可以在`$HOME`文件夹下配置一个名为` .spring-boot-devtools.properties` 的文件作为全局设置,在你的机器上使用devtools的所有Spring Boot应用程序都会应用该文件的属性.例如:

**~/.spring-boot-devtools.properties**:

```properties
spring.devtools.reload.trigger-file=.reloadtrigger
```

| ![[Note]](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/images/note.png) |
| ------------------------------------------------------------ |
| 在`.spring-boot-devtools.properties`文件中激活的属性不会影响到[profile-specific configuration files](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-profile-specific-properties) |



### 4. Restart Classloader

Spring Boot提供的Restart技术使用了两种类加载器. 没改变的类由 *base* classloader加载(例如第三方的jar包).你正在开发的类由 *restart* classloader加载, 当应用程序重新启动时, *restart* classloader 会被废弃然后创建一个新的. 因为 *base* classloader 已经可用并且加载好了, 所以这样启动会比 "cold starts"快很多.





### 5. 日志级别

所有被支持的日志系统都能在Spring `Environment`中通过`logging.level.<logger-name>=<level>`进行配置(例如:`application.properties`), `level`可以是 TRACE, DEBUG, INFO, WARN, ERROR, FATAL, 或 OFF 中的一个. `rrot` logger可以使用 `logging.level.root`来配置.下面的示例展示了`application.properties`中的一些重要logging设置:

```properties
logging.level.root=WARN
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=ERROR
```

> org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener



### 6. PropertySource order

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

| ![[Tip]](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/images/tip.png) |
| ------------------------------------------------------------ |
| 可以在命令行上使用环境变量提供`SPRING_APPLICATION_JSON` properties.例如,在shell中使用如下命令: `$ SPRING_APPLICATION_JSON='{"acme":{"name":"test"}}' java -jar myapp.jar`  . 在上面的例子中, 在Spring `Environment` 中会有`acme.name=test`. 你也可以在System property中以`spring.application.json`的方式提供JSON, 例如:`$ java -Dspring.application.json='{"name":"test"}' -jar myapp.jar`. 你也可以通过命令行参数的形式提供JSON, 例如:`$ java -jar myapp.jar --spring.application.json='{"name":"test"}'` . 你也可以以 JNDI变量的方式提供JSON,例如:`java:comp/env/spring.application.json`. |

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
>  你也可以以 JNDI变量的方式提供JSON,例如:`java:comp/env/spring.application.json`.



### 7. 访问命令行参数

默认情况下, `SpringApplication`会将所有命令行参数转换为`property`并将它们添加到Spring `Environment`(也就是说以`—`开始的参数,例如`--server.port=9000`).如前面提到的,命令行参数会优先于任何其他property sources被应用.如果你不想命令行的properties被添加到`Environment`, 你可以通过`SpringApplication.setAddCommandLineProperties(false)`来禁用.



















