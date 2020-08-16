# MSA using SpringCloud

## Development Environment
`Windows 10` `JDK 11.0.6` `SpringBoot 2.3.2.RELEASE` `Maven 3.6.3` `Git 2.22.0.windows.1` `intellij`
[`Spring Cloud Hoxton.RELEASE`](https://spring.io/projects/spring-cloud) [`RabbitMQ 3.8.6`](https://www.rabbitmq.com/download.html)
[`Erlang/OTP 23.0`](https://www.erlang.org/downloads)

 

## Table of Contents
- Config Server (환경설정 외부화)<br />
자세한 설명은 [여기](https://bravenamme.github.io/2020/08/16/spring-cloud-com.assu.cloud.eventservice.config-server/) 를 참고
> [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/) <br />
> [AMQP doc](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-amqp) <br />
> [JCE jar download](https://www.oracle.com/java/technologies/javase-jce-all-downloads.html)
>
```shell script
HOW TO RUN

-- rabbitMQ 서버 실행
C:\rabbitmq_server-3.8.6\sbin>rabbitmq-service.bat start

-- rabbitMQ 관리자 UI 확인
http://localhost:15672/

-- 현재 프로젝트 실행
mvn spring-boot:run

-- 컨피스 서버 구동 확인
http://localhost:8889/actuator

-- 컨피그 서버 JSON Payload 확인
http://localhost:8889/member-service/default/
http://localhost:8889/member-service/dev

-- 현재 실행중인 환경정보 확인
http://localhost:8090/actuator/env

-- port 재설정하여 서비스 띄우기
C:\> mvn clean install
C:\configserver\target>java -jar configserver-0.0.1-SNAPSHOT.jar
C:\member-service\target>java -Dserver.port=8090 -jar member-service-0.0.1-SNAPSHOT.jar
C:\member-service\target>java -Dserver.port=8091 -jar member-service-0.0.1-SNAPSHOT.jar

-- actuator bus 종단점 호출하여 설정정보 변경 전파
POST http://localhost:8090/actuator/bus-refresh

-- 변경된 설정정보 확인
GET http://localhost:8090/member/name?nick=JU
GET http://localhost:8091/member/name?nick=JU

-- 컨피그 서버 암호화 키로 암호화/복호화된 패스워드 확인
POST http://localhost:8889/encrypt

POST http://localhost:8889/decrypt

```
- Feign (REST Client & Circuit Breaker)
- Ribbon (Load Balancer)
- Eureka (Service Registry & Discovery)
```shell script
HOW TO RUN

-- rabbitMQ 서버 실행
C:\rabbitmq_server-3.8.6\sbin>rabbitmq-service.bat start

-- port 재설정하여 서비스 띄우기
C:\> mvn clean install
C:\configserver\target>java -jar configserver-0.0.1-SNAPSHOT.jar
C:\member-service\target>java -Dserver.port=8090 -jar member-service-0.0.1-SNAPSHOT.jar
C:\member-service\target>java -Dserver.port=8091 -jar member-service-0.0.1-SNAPSHOT.jar
C:\event-service\target>java -Dserver.port=8070 -jar event-service-0.0.1-SNAPSHOT.jar
C:\event-service\target>java -Dserver.port=8071 -jar event-service-0.0.1-SNAPSHOT.jar

-- actuator bus 종단점 호출하여 설정정보 변경 전파
POST http://localhost:8090/actuator/bus-refresh

-- 변경된 설정정보 확인
GET http://localhost:8090/member/name?nick=JU
GET http://localhost:8071/event/name?nick=JU

```
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

-- rabbitMQ 플러그인 활성화
C:\rabbitmq_server-3.8.6\sbin>rabbitmq-plugins enable rabbitmq_management

-- rabbitMQ 서비스 중지
C:\rabbitmq_server-3.8.6\sbin>rabbitmq-service.bat stop

-- rabbitMQ 서비스 설치
C:\rabbitmq_server-3.8.6\sbin>rabbitmq-service.bat install

-- rabbitMQ 서비스 재기동
C:\rabbitmq_server-3.8.6\sbin>rabbitmq-service.bat start

-- 각각 터미널 창에서 서비스 띄우기
java -jar /target/fares-1.0.jar

-- 서비스 확인
http://localhost:8080/actuator/
http://localhost:8888/licensingservice/default

```

<br /><br /><br /><br />
![star--v1](https://user-images.githubusercontent.com/18479472/87845570-7a2bd800-c903-11ea-9b18-f624600a5ac7.png) thanks!