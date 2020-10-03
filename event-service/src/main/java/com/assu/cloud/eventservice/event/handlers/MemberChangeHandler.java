package com.assu.cloud.eventservice.event.handlers;

import com.assu.cloud.eventservice.event.CustomChannels;
import com.assu.cloud.eventservice.event.model.MemberChangeModel;
import com.assu.cloud.eventservice.repository.MemberRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * 사용자 정의 채널을 사용하여 메시지 수신
 * 이 애플리케이션을 메시지 브로커와 바인딩하도록 스프링 클라우드 스트림 설정
 */
@EnableBinding(CustomChannels.class)    // CustomChannels.class 로 지정 시 해당 서비스가 CustomChannels 클래스에 정의된 채널들을 이용해 메시지 브로커와 통신
public class MemberChangeHandler {

    private static final Logger logger = LoggerFactory.getLogger(MemberChangeHandler.class);
    private final MemberRedisRepository memberRedisRepository;

    public MemberChangeHandler(MemberRedisRepository memberRedisRepository) {
        this.memberRedisRepository = memberRedisRepository;
    }

    /**
     * 메시지가 입력 채널에서 수신될 때마다 이 메서드 실행
     */
    @StreamListener("inboundMemberChanges")     // Sink.INPUT 대신 사용자 정의 채널명인 inboundMemberChanges 전달
    public void loggerSink(MemberChangeModel mbChange) {
        logger.info("======= Received a message of type {}", mbChange.getType());
        switch (mbChange.getAction()) {
            case "GET":
                logger.debug("Received a GET event from the member service for userId {}", mbChange.getUserId());
                break;
            case "SAVE":
                logger.debug("Received a SAVE event from the member service for userId {}", mbChange.getUserId());
                break;
            case "UPDATE":
                logger.debug("Received a UPDATE event from the member service for userId {}", mbChange.getUserId());
                memberRedisRepository.deleteMember(mbChange.getUserId());       // 캐시 무효화
                break;
            case "DELETE":
                logger.debug("Received a DELETE event from the member service for userId {}", mbChange.getUserId());
                memberRedisRepository.deleteMember(mbChange.getUserId());
                break;
            default:
                logger.debug("Received an UNKNOWN event from the member service for userId {}", mbChange.getType());
                break;
        }
    }
}
