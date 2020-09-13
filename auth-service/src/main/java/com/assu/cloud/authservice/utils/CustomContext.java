package com.assu.cloud.authservice.utils;

import org.springframework.stereotype.Component;

/**
 * 서비스가 쉽게 액세스할 수 있는 HTTP 헤더를 만들어 저장하는 클래스
 * HTTP 요청에서 추출한 값을 보관하는 POJO
 */
@Component
public class CustomContext {
    public static final String CORRELATION_ID = "assu-correlation-id";

    private String correlationId = new String();

    // 그 외 필요한 항목 넣을 수 있음 (인증 토큰 등...)

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
