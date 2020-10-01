package com.assu.cloud.authservice.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * RestTemplate 인스턴스에서 실행되는 모든 HTTP 기반 서비스 발신 요청에 상관관계 ID 삽입 + 토큰
 */
public class CustomContextInterceptor implements ClientHttpRequestInterceptor {
    /**
     * RestTemplate 로 실제 HTTP 서비스 호출 전 intercept 메서드 호출
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        HttpHeaders headers = httpRequest.getHeaders();

        headers.add(CustomContext.CORRELATION_ID, CustomContextHolder.getContext().getCorrelationId());

        // 그 외 필요한 항목 넣을 수 있음 (인증 토큰 등...)
        headers.add(CustomContext.AUTH_TOKEN, CustomContextHolder.getContext().getAuthToken());     // HTTP 헤더에 인증 토큰 추가

        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
