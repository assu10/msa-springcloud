package com.assu.cloud.eventservice.repository;

import com.assu.cloud.eventservice.model.Member;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * 부트스트랩 클래스에서 정의한 RedisTemplate 빈을 사용하여 레디스 서버와 통신
 */
@Repository
public class MemberRedisRepositoryImpl implements MemberRedisRepository {

    private static final String HASH_NAME = "member";       // 회원 데이터가 저장되는 레디스 서버의 해시명
    private final RedisTemplate<String, Member> redisTemplate;
    private HashOperations hashOperations;      // HashOperation 클래스는 레디스 서버에 데이터 작업을 수행하는 스프링 헬퍼 메서드의 집합

    public MemberRedisRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        hashOperations = redisTemplate.opsForHash();

        // 키와 값을 명시적으로 직렬화해주지 않으면 default serializer 로 JdkSerializationRedisSerializer 를 사용하는데
        // 그러면 \xac\xed\x00\x05t\x00\x06member 이런 식으로 저장됨
       redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    }

    @Override
    public void saveMember(Member member) {
        hashOperations.put(HASH_NAME, member.getId(), member);
    }

    @Override
    public void updateMember(Member member) {
        hashOperations.put(HASH_NAME, member.getId(), member);
    }

    @Override
    public void deleteMember(String userId) {
        hashOperations.delete(HASH_NAME, userId);
    }

    @Override
    public Member findMember(String userId) {
        return (Member) hashOperations.get(HASH_NAME, userId);
    }
}
