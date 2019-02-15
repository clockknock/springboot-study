## Springboot With Docker

from [spring.io](https://spring.io/guides/gs/spring-boot-docker/)

containerize:
```bash
./mvnw install dockerfile:build
```

push:
```bash
./mvnw dockerfile:push
```

run container(with default yml: `application-dev.yml`):
```bash
docker run -d --name app -p 8080:8080 -d -t mingmingzi/with-docker
```

run with environment variable:
```bash
#use application-prod.yml
docker run -d --name app -e "SPRING_PROFILES_ACTIVE=prod" \
-p 8080:8080 -d -t mingmingzi/with-docker
```

run it and overrides the `command` value:
```bash
# overrides with `--name` COMMAND
docker run -d --name app -e "SPRING_PROFILES_ACTIVE=prod" \
-p 8080:8080 -d -t mingmingzi/with-docker --command="use --name"

# overrides with `--spring.application.json` COMMAND(prefer use this)
docker run -d --name app -e "SPRING_PROFILES_ACTIVE=prod" \
-p 8080:8080 -d -t mingmingzi/with-docker --spring.application.json='{"command":"use json"}'
```

