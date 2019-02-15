## Springboot With Docker

from [spring.io](https://spring.io/guides/gs/spring-boot-docker/)

使用mvn使其容器化:
```bash
./mvnw install dockerfile:build
```

push到docker hub:
```bash
./mvnw dockerfile:push
```

运行容器(使用默认的`application-dev.yml`):
```bash
docker run -d --name app -p 8080:8080 -d -t mingmingzi/with-docker
```

指定环境变量运行容器:
```bash
#指定了环境变量然后使用 application-prod.yml 来运行app
docker run -d --name app -e "SPRING_PROFILES_ACTIVE=prod" \
-p 8080:8080 -d -t mingmingzi/with-docker
```

通过cmd指定环境变量来运行app:
```bash
# 使用`--name`来指定
docker run -d --name app -e "SPRING_PROFILES_ACTIVE=prod" \
-p 8080:8080 -d -t mingmingzi/with-docker --command="use --name"

# 使用`--spring.application.json` 来指定(推荐使用这个)
docker run -d --name app -e "SPRING_PROFILES_ACTIVE=prod" \
-p 8080:8080 -d -t mingmingzi/with-docker --spring.application.json='{"command":"use json"}'
```

使用`docker-compose.yml`运行,并指定环境变量和接在entry point 后的command:
```yaml
version: "3"
services:

  with-docker:
    image: mingmingzi/with-docker
    expose:
      - "8080"
    environment:
     - SPRING_PROFILES_ACTIVE=prod
    command: --spring.application.json='{"command":"use json"}'


```