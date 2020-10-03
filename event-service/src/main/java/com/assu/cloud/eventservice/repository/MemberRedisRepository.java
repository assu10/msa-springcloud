package com.assu.cloud.eventservice.repository;

import com.assu.cloud.eventservice.model.Member;

/**
 * 레디스에 액세스해야 하는 클래스에 주입된 인터페이스
 */
public interface MemberRedisRepository {
    void saveMember(Member member);
    void updateMember(Member member);
    void deleteMember(String userId);
    Member findMember(String userId);
}
