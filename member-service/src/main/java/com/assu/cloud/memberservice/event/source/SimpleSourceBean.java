package com.assu.cloud.memberservice.event.source;

import com.assu.cloud.memberservice.event.model.MemberChangeModel;
import com.assu.cloud.memberservice.utils.CustomContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 메시지 브로커에 메시지 발행
 */
@Component
public class SimpleSourceBean {
    private final Source source;

    private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);

    /**
     * 스프링 클라우드 스트림은 서비스가 사용할 소스 인터페이스 구현을 주입
     */
    public SimpleSourceBean(Source source) {
        this.source = source;
    }

    /**
     * 메시지 발행
     */
    public void publishMemberChange(String action, String userId) {
        logger.debug("======= Sending kafka message {} for User Id : {}", action, userId);
        // com.assu.cloud.memberservice.event.model.MemberChangeModel
        logger.debug("======= MemberChangeModel.class.getTypeName() : {}", MemberChangeModel.class.getTypeName());

        // 발행될 메시지는 자바 POJO
        MemberChangeModel change = new MemberChangeModel(MemberChangeModel.class.getTypeName(),
                                                        action,
                                                        userId,
                                                        CustomContext.getCorrelationId());
        // 메시지를 보낼 준비가 되면 Source 클래스에 정의된 채널에서 send() 메서드 사용
        source.output().send(MessageBuilder.withPayload(change).build());
    }
}
