package com.assu.cloud.memberservice.controller;

import com.assu.cloud.memberservice.client.EventRestTemplateClient;
import com.assu.cloud.memberservice.config.CustomConfig;
import com.assu.cloud.memberservice.event.source.SimpleSourceBean;
import com.assu.cloud.memberservice.model.Member;
import com.assu.cloud.memberservice.utils.CustomContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.Random;

@RestController
@RequestMapping("/member")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final CustomConfig customConfig;
    private final EventRestTemplateClient eventRestTemplateClient;
    private final SimpleSourceBean simpleSourceBean;

    public MemberController(CustomConfig customConfig, EventRestTemplateClient eventRestTemplateClient, SimpleSourceBean simpleSourceBean) {
        this.customConfig = customConfig;
        this.eventRestTemplateClient = eventRestTemplateClient;
        this.simpleSourceBean = simpleSourceBean;
    }

    @GetMapping(value = "name/{nick}")
    public String getYourName(ServletRequest req, @PathVariable("nick") String nick) {
        logger.info("[MEMBER] name/{nick} logging...nick is {}.", nick);
        return "[MEMBER] Your name is " + customConfig.getYourName() + " / nickname is " + nick + " / port is " + req.getServerPort();
    }

    /**
     * RestTemplate 를 이용하여 이벤트 서비스의 REST API 호출
     */
    @GetMapping(value = "gift/{name}")
    public String gift(ServletRequest req, @PathVariable("name") String name) {
        return "[MEMBER] " + eventRestTemplateClient.gift(name) + " / port is " + req.getServerPort();
    }

    /**
     * ADMIN 권한 소유자만 PUT METHOD API 호출 가능하도록 설정 후 테스트
     */
    @PutMapping("{name}")
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    public String member(@PathVariable("name") String name) {
        return "[MEMBER-DELETE] " + name + " is deleted.";
    }

    /**
     * 이벤트 서비스에서 OAuth2 로 호출 테스트
     */
    @GetMapping("userInfo/{name}")
    public String userInfo(@PathVariable("name") String name) {
        return "[MEMBER] " + name;
    }

    /**
     * 단순 메시지 발행
     */
    @PostMapping("/{userId}")
    public void saveUserId(@PathVariable("userId") String userId) {
        // DB 에 save 작업..
        simpleSourceBean.publishMemberChange("SAVE", userId);
    }

    /**
     * 이벤트 서비스에서 캐시 용도로 회원 데이터 조회
     */
    @GetMapping("{userId}")
    public Member userInfoCache(@PathVariable("userId") String userId) {
        logger.debug("====== 회원 저장 서비스 호출!");

        // DB 를 조회하여 회원 데이터 조회 (간편성을 위해 아래와 같이 리턴함)
        Member member = new Member();
        member.setId(userId);
        member.setName("rinda");
        return member;
    }

    /**
     * 이벤트 서비스에서 캐시 제거를 위한 메서드
     */
    @DeleteMapping("userInfo/{userId}")
    public void deleteUserInfoCache(@PathVariable("userId") String userId) {
        logger.debug("====== 회원 삭제 후 DELETE 메시지 발생");

        // DB 에 삭제 작업  (간편성을 위해 DB 작업은 생략)
        simpleSourceBean.publishMemberChange("DELETE", userId);
    }

    /**
     * Hystrix 기본 테스트 (RestTemplate 를 이용하여 이벤트 서비스의 REST API 호출)
     */
    @HystrixCommand     // 모두 기본값으로 셋팅한다는 의미
    @GetMapping(value = "hys/{name}")
    public String hys(ServletRequest req, @PathVariable("name") String name) {
        logger.debug("LicenseService.getLicensesByOrg  Correlation id: {}", CustomContextHolder.getContext().getCorrelationId());
        //randomlyRunLong();
        sleep();
        return "[MEMBER] " + eventRestTemplateClient.gift(name) + " / port is " + req.getServerPort();
    }

    /**
     * Circuit Breaker 타임아웃 설정 (RestTemplate 를 이용하여 이벤트 서비스의 REST API 호출)
     */
    /*@HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",
                                     value="5000")})   // 회로차단기의 타임아웃 시간을 5초로 설정*/
    @HystrixCommand(fallbackMethod = "timeoutFallback")
    @GetMapping(value = "timeout/{name}")
    public String timeout(ServletRequest req, @PathVariable("name") String name) {
        return "[MEMBER] " + eventRestTemplateClient.gift(name) + " / port is " + req.getServerPort();
    }

    /**
     * timeout 메서드의 폴백 메서드
     */
    public String timeoutFallback(ServletRequest req, @PathVariable("name") String name) {
        return "This is timeoutFallback test.";
    }

    @GetMapping(value = "bulkheadMain/{name}")
    public String bulkheadMain(ServletRequest req, @PathVariable("name") String name) {
        String eventApi = eventRestTemplateClient.gift(name);
        return "[MEMBER] " + eventApi;
    }

    /**
     * eventThreadPool 을 사용하면서 sleep() 이 있는 이벤트 서비스를 호출하는 함수
     */
    @HystrixCommand( //fallbackMethod = "timeoutFallback")
            threadPoolKey = "eventThreadPool",
            threadPoolProperties =
                    {@HystrixProperty(name = "coreSize", value = "30"),         // 스레드 풀의 스레드 갯수 (디폴트 10)
                     @HystrixProperty(name = "maxQueueSize", value = "10")},    // 스레드 풀 앞에 배치할 큐와 큐에 넣을 요청 수 (디폴트 -1)
            commandProperties = {
                    // 히스트릭스가 호출 차단을 고려하는데 필요한 시간인 10초(metrics.rollingStats.timeInMilliseconds) 동안 연속 호출 횟수 (디폴트 20)
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
                    // 서킷 브레이커가 열린 후 requestVolumeThreshold 값만큼 호출한 후 타임아웃, 예외, HTTP 500 반환등으로 실패해야 하는 호출 비율 (디폴트 50)
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
                    // 서킷 브레이커가 열린 후 서비스의 회복 상태를 확인할 때까지 대기할 시간 간격. 즉, 서킷 브레이커가 열렸을 때 얼마나 지속될지...(디폴트 5000)
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                    // 서비스 호출 문제를 모니터할 시간 간격. 즉 서킷 브레이커가 열리기 위한 조건을 체크할 시간. (디폴트 10초)
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                    // 설정한 시간 간격동안 통계를 수집할 횟수 (이 버킷수는 모니터 시간 간격에 균등하게 분할되어야 함
                    // 여기선 15초 시간 간격을 사용하고, 3초 길이의 5개 버킷에 통계 데이터 수집
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")}
    )
    @GetMapping(value = "bulkheadEvtSleep/{name}")
    public String bulkheadEvtSleep(@PathVariable("name") String name) {
        String eventApi = eventRestTemplateClient.gift(name);
        return "[MEMBER] " + eventApi;
    }

    /**
     * eventThreadPool 을 사용하지만 sleep() 이 없는 이벤트 서비스를 호출하는 함수
     * 바로 위 함수에서 서킷 브레이커가 열려도 아래 함수는 정상 동작함 (스레드 풀 키를 이런 식으로 공유해서 사용할 수 없는 것 같음)
     */
    @HystrixCommand( //fallbackMethod = "timeoutFallback")
            threadPoolKey = "eventThreadPool",
            threadPoolProperties =
                    {@HystrixProperty(name = "coreSize", value = "30"),         // 스레드 풀의 스레드 갯수 (디폴트 10)
                     @HystrixProperty(name = "maxQueueSize", value = "10")},    // 스레드 풀 앞에 배치할 큐와 큐에 넣을 요청 수 (디폴트 -1)
            commandProperties = {
                    // 히스트릭스가 호출 차단을 고려하는데 필요한 시간인 10초(metrics.rollingStats.timeInMilliseconds) 동안 연속 호출 횟수 (디폴트 20)
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
                    // 서킷 브레이커가 열린 후 requestVolumeThreshold 값만큼 호출한 후 타임아웃, 예외, HTTP 500 반환등으로 실패해야 하는 호출 비율 (디폴트 50)
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    // 서킷 브레이커가 열린 후 서비스의 회복 상태를 확인할 때까지 대기할 시간 간격. 즉, 서킷 브레이커가 열렸을 때 얼마나 지속될지...(디폴트 5000)
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                    // 서비스 호출 문제를 모니터할 시간 간격. 즉 서킷 브레이커가 열리기 위한 조건을 체크할 시간. (디폴트 10초)
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                    // 설정한 시간 간격동안 통계를 수집할 횟수 (이 버킷수는 모니터 시간 간격에 균등하게 분할되어야 함
                    // 여기선 15초 시간 간격을 사용하고, 3초 길이의 5개 버킷에 통계 데이터 수집
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")}
    )
    @GetMapping(value = "bulkheadEvtNotSleepPool/{name}")
    public String bulkheadEvtPool(ServletRequest req, @PathVariable("name") String name) {
        String eventApi = eventRestTemplateClient.gift2(name);
        return "[MEMBER] " + eventApi;
    }

    @GetMapping(value = "bulkheadEvtNotSleepNotPool/{name}")
    public String bulkheadEvtNotPool(ServletRequest req, @PathVariable("name") String name) {
        String eventApi = eventRestTemplateClient.gift2(name);
        return "[MEMBER] " + eventApi;
    }

    /**
     * 3번 중 1번은 sleep()
     */
    private void randomlyRunLong() {
        logger.debug("randomlyRunLong");
        Random rand = new Random();
        int randomNum = rand.nextInt((3-1)+1)+1;

        if (randomNum == 3) {
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(7000);        // 3,000 ms (3초), 기본적으로 히스트릭스는 1초 후에 호출을 타임아웃함
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
