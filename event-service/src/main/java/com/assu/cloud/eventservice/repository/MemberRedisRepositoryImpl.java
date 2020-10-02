package com.assu.cloud.eventservice.repository;

import com.assu.cloud.eventservice.model.Member;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class MemberRedisRepositoryImpl implements MemberRedisRepository {

    private static final String HASH_NAME = "member";       // 회원 데이터가 저장되는 레디스 서버의 해시명
    private RedisTemplate<String, Member> redisTemplate;
    private HashOperations hashOperations;      // HashOperation 클래스는 레디스 서버에 데이터 작업을 수행하는 스프링 헬퍼 메서드의 집합

    public MemberRedisRepositoryImpl() {
        super();
    }

    public MemberRedisRepositoryImpl(RedisTemplate<String, Member> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        hashOperations = redisTemplate.opsForHash();
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
