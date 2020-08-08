# MSA using SpringCloud

## Development Environment
`Windows 10`, `JDK 11.0.6`, `SpringBoot 2.3.2`, `Maven 3.6.3`, `Git 2.22.0.windows.1`, `intellij`

## Table of Contents
- Config Server
- Feign (REST Client)
- Ribbon (Load Balancer)
- Eureka (Service Discovery)
- Zuul (Proxy API Gateway)
- OAuth2, JWT (Security)
- Sleath, Papertrail, Zipkin (Tracker)
- Travis CI (Deploy)

## Server Diagram
TO-DO...

## How to run
```shell script
-- 각 서비스 디렉터리안에 있는 메이븐 pom.xml 파일을 실행하고 로컬에서 도커 이미지 빌드
mvn clean package docker:build

-- 서비스 확인
http://localhost:8080/actuator/
http://localhost:8888/licensingservice/default

```

<br /><br /><br /><br />
![star--v1](https://user-images.githubusercontent.com/18479472/87845570-7a2bd800-c903-11ea-9b18-f624600a5ac7.png) thanks!