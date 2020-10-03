package com.assu.cloud.eventservice.client;

import com.assu.cloud.eventservice.config.CustomConfig;
import com.assu.cloud.eventservice.model.Member;
import com.assu.cloud.eventservice.repository.MemberRedisRepository;
import com.assu.cloud.eventservice.utils.CustomContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 회원 데이터 필요 시 회원 서비스 호출 전 레디스 캐시 먼저 확인
 */
@Component
public class MemberCacheRestTemplateClient {

    private static final Logger logger = LoggerFactory.getLogger(MemberCacheRestTemplateClient.class);

    private final RestTemplate restTemplate;
    private final MemberRedisRepository memberRedisRepository;
    private final CustomConfig customConfig;

    public MemberCacheRestTemplateClient(RestTemplate restTemplate, MemberRedisRepository memberRedisRepository,  CustomConfig customConfig) {
        this.restTemplate = restTemplate;
        this.memberRedisRepository = memberRedisRepository;
        this.customConfig = customConfig;
    }

    String URL_PREFIX = "/api/mb/member/";      // 회원 서비스의 주울 라우팅경로와 회원 클래스 주소

    /**
     * 회원 아이디로 레디스에 저장된 Member 클래스 조회
     */
    private Member checkRedisCache(String userId) {
        try {
            return memberRedisRepository.findMember(userId);
        } catch (Exception e) {
            logger.error("======= Error encountered while trying to retrieve member {} check Redis Cache., Exception {}", userId, e);
            return null;
        }
    }

    /**
     * 레디스 캐시에 데이터 저장
     */
    private void cacheMemberObject(Member member) {
        try {
            memberRedisRepository.saveMember(member);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("======= Unable to cache member {} in Redis. Exception {}", member.getId(), e);
        }
    }

    public Member getMember(String userId) {

        Member member = checkRedisCache(userId);

        // 레디스에 데이터가 없다면 원본 데이터에서 데이터를 조회하기 위해 회원 서비스 호출
        if (member != null) {
            logger.debug("======= Successfully retrieved an Member {} from the redis cache: {}", userId, member);
            return member;
        }

        logger.debug("======= Unable to locate member from the redis cache: {}", userId);

        ResponseEntity<Member> restExchange =
                restTemplate.exchange(
                        "http://" + customConfig.getServiceIdZuul() + URL_PREFIX + "{userId}",   // http://localhost:5555/api/mb/member/userInfo/rinda
                        HttpMethod.GET,
                        null,
                        Member.class,
                        userId
                );

        // 캐시 레코드 저장
        member = restExchange.getBody();

        // 조회한 객체를 캐시에 저장
        if (member != null) {
            cacheMemberObject(member);
        }

        return member;
    }
}
