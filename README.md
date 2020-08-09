# MSA using SpringCloud

## Development Environment
`Windows 10` `JDK 11.0.6` `SpringBoot 2.3.2.RELEASE` `Maven 3.6.3` `Git 2.22.0.windows.1` `intellij`
[`Spring Cloud Hoxton.RELEASE`](https://spring.io/projects/spring-cloud) [`RabbitMQ 3.8.6`](https://www.rabbitmq.com/download.html)
[`Erlang/OTP 23.0`](https://www.erlang.org/downloads)

 

## Table of Contents
- Config Server (환경설정 외부화)
> [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)<br />
> [AMQP doc](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-amqp)
- Feign (REST Client & Circuit Breaker)
- Ribbon (Load Balancer)
- Eureka (Service Registry & Discovery)
- Zuul (Proxy & API Gateway)
- OAuth2, JWT (Security)
- Sleath, Papertrail, Zipkin (Logging Tracker)
- Travis CI (Build & Deploy)
- Spring Cloud Messaging (비동기 마이크로서비스 구성)
- Hystrix + Turbine (Circuit Breaker & Dashboard, 여러 대의 WAS 한번에 모니터링)

## Server Diagram
TO-DO...

## How to run
```shell script
-- 현재 프로젝트 바로 실행하기
mvn spring-boot:run

-- parent-pom 이 위치한 디렉터리안에 있는 메이븐 pom.xml 파일을 실행
mvn clean package

-- rabbitMQ 서버 실행
rabbitmq_server-3.5.6.sbin$ ./rabbitmq-server

-- 각각 터미널 창에서 서비스 띄우기
java -jar /target/fares-1.0.jar

-- 서비스 확인
http://localhost:8080/actuator/
http://localhost:8888/licensingservice/default

```

<br /><br /><br /><br />
![star--v1](https://user-images.githubusercontent.com/18479472/87845570-7a2bd800-c903-11ea-9b18-f624600a5ac7.png) thanks!