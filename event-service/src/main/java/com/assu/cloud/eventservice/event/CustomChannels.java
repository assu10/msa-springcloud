package com.assu.cloud.eventservice.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 사용자 정의 input 채널 (SINK.INPUT 같은...), Consumer
 */
public interface CustomChannels {

    @Input("inboundMemberChanges")      // @Input 은 채널 이름을 정의하는 메서드 레벨 애너테이션
    SubscribableChannel members();      // @Input 애너테이션으로 노출된 채널은 모두 SubscribableChannel 클래스를 반환해야 함
}
