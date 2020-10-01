# MSA using SpringCloud

## Development Environment
`Windows 10` `JDK 11.0.6` `SpringBoot 2.3.2.RELEASE` `Maven 3.6.3` `Git 2.22.0.windows.1` `intellij`
[`Spring Cloud Hoxton.SR6`](https://spring.io/projects/spring-cloud) 에서 `SR8` 로 업그레이드 [`RabbitMQ 3.8.6`](https://www.rabbitmq.com/download.html)
[`Erlang/OTP 23.0`](https://www.erlang.org/downloads)

 

## Table of Contents

- Config Server (환경설정 외부화)
- Feign (REST Client & Circuit Breaker)
- Eureka (Service Registry & Discovery)
- Zuul (Proxy & API Gateway)
- OAuth2, JWT (Security)
- Spring Cloud Stream (EDA, 비동기 마이크로서비스 구성)
- Sleath, Papertrail, Zipkin (Logging Tracker)
- Travis CI (Build & Deploy)
- Hystrix + Turbine (Circuit Breaker & Dashboard, 여러 대의 WAS 한번에 모니터링)

---

***- Config Server (환경설정 외부화)<br />***
자세한 설명은 [Spring Cloud - Spring Cloud Config Server](https://assu10.github.io/dev/2020/08/16/spring-cloud-config-server/) 를 참고
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

---

***- Feign (REST Client & Circuit Breaker)***<br />
자세한 설명은 [Spring Cloud - Spring Cloud Feign](https://assu10.github.io/dev/2020/06/18/spring-cloud-feign/) 를 참고

---

***- Eureka (Service Registry & Discovery)***<br />
자세한 설명은 [Spring Cloud - Spring Cloud Eureka](https://assu10.github.io/dev/2020/08/26/spring-cloud-eureka/) 를 참고

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

-- 유레카 서버 구축 후 재기동
C:\> mvn clean install
C:\configserver\target>java -jar configserver-0.0.1-SNAPSHOT.jar
C:\eurekaserver\target>java -jar eurekaserver-0.0.1-SNAPSHOT.jar

-- 유레카 콘솔 확인
http://localhost:8761/

-- 유레카 서비스 등록 후 재기동
C:\eurekaserver\target>java -jar eurekaserver-0.0.1-SNAPSHOT.jar
C:\member-service\target>java -jar member-service-0.0.1-SNAPSHOT.jar
C:\event-service\target>java -Dserver.port=8071 -jar event-service-0.0.1-SNAPSHOT.jar
C:\event-service\target>java -Dserver.port=8070 -jar event-service-0.0.1-SNAPSHOT.jar

-- 유레카 서버에 등록된 레지스트리 내용 확인
http://localhost:8761/eureka/apps/

-- 특정 서비스 레지스트리 확인
http://localhost:8761/eureka/apps/event-service

-- RestTemplate 적용 후 API 호출
http://localhost:8090/member/gift/flower 

-- Feign 적용 후 API 호출
C:\> mvn clean install
C:\configserver>java configserver-0.0.1-SNAPSHOT.jar
C:\member-service\target>java -Dserver.port=8090 -jar member-service-0.0.1-SNAPSHOT.jar
C:\member-service\target>java -Dserver.port=8091 -jar member-service-0.0.1-SNAPSHOT.jar
C:\event-service\target>java event-service-0.0.1-SNAPSHOT.jar

GET http://localhost:8070/event/member/hyori
```

---

***- Zuul (Proxy & API Gateway)***<br />
자세한 설명은 [Spring Cloud - Netflix Zuul(1/2)](https://assu10.github.io/dev/2020/08/26/netflix-zuul/)와 
[Spring Cloud - Netflix Zuul(2/2)](https://assu10.github.io/dev/2020/09/05/netflix-zuul2/) 를 참고

```shell script
HOW TO RUN

-- 유레카 콘솔 접속하여 주울 등록되었는지 확인
http://localhost:8761/

-- 주울이 잘 떴는지 확인
http://localhost:5555/actuator/env

-- 주울이 라우팅하고 있는 경로 확인
http://localhost:5555/actuator/routes

-- 주울을 통해 이벤트 서비스의 REST API 호출 (feign)
http://localhost:5555/event-service/event/member/assu

-- 컨피그 저장소 설정값 변경 후 내용 전파
POST http://localhost:5555/actuator/bus-refresh 

-- 주울 수동 매핑된 경로로 라우팅되는지 확인
http://localhost:5555/evt/event/member/hyori

-- `api` 프리픽스 붙인 후 라우팅 확인
http://localhost:5555/api/evt/event/member/hyori

-- 주울을 통해 회원 서비스의 REST API 호출 (RestTemplate)
http://localhost:5555/api/mb/member/gift/flower

-- 사전/사후 필터 동작 확인 (상관관계 ID 생성 및 하위 서비스에 전파/응답 헤더에 상관관계 ID 삽입)
http://localhost:5555/api/mb/member/name/hyori

```

---
 
***- OAuth2, JWT (Security)***<br />
자세한 설명은 [Spring Cloud - OAuth2, Security(1/2)](https://assu10.github.io/dev/2020/09/12/spring-cloud-oauth2.0/) 와
[Spring Cloud - OAuth2, Security(2/2)](https://assu10.github.io/dev/2020/09/30/spring-cloud-oauth2.0-2/)를 참고

```shell script
HOW TO RUN

-- 액세스 토큰 획득
[POST] http://localhost:8901/auth/oauth/token

-- 액세스 토큰으로 사용자 정보 조회
[GET] http://localhost:8901/auth/user 

-- OAuth2 로 회원 서비스 보호 후 API 호출
[GET] http://localhost:8090/member/name/rinda

-- 권한 있는 사용자(assuAdmin) 의 액세스 토큰과 함께 PUT 메서드 API 호출
[PUT] http://localhost:8090/member/rinda

-- oauth2 전파 (이벤트 서비스에서 회원서비스 호출)
[GET] http://localhost:5555/api/evt/event/userInfo/rinda

-- JWT 인증 서버 설정 후 액세스 토큰 획득
[POST] http://localhost:8901/auth/oauth/token

-- JWT 토큰 획득 후 인증 확인
[GET] http://localhost:8090/member/gift/manok
```
---

***- Spring Cloud Stream (EDA, 비동기 마이크로서비스 구성)***<br />

---
 
***- Sleath, Papertrail, Zipkin (Logging Tracker)***<br />

---

***- Travis CI (Build & Deploy)***<br />

---

***- Hystrix + Turbine (Circuit Breaker & Dashboard, 여러 대의 WAS 한번에 모니터링)***<br />

---

## Server Diagram
TO-DO...

---

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

---

<br /><br /><br /><br />
![star--v1](https://user-images.githubusercontent.com/18479472/87845570-7a2bd800-c903-11ea-9b18-f624600a5ac7.png) thanks!