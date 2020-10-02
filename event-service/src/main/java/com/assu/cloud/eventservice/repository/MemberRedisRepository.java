package com.assu.cloud.eventservice.repository;

import com.assu.cloud.eventservice.model.Member;

public interface MemberRedisRepository {
    void saveMember(Member member);
    void updateMember(Member member);
    void deleteMember(String userId);
    Member findMember(String userId);
}
