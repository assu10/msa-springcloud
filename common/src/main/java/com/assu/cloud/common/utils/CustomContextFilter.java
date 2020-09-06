package com.assu.cloud.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 유입되는 HTTP 요청을 가로채서 필요한 헤더값을 CustomContext 에 매핑
 * 
 * REST 서비스에 대한 모든 HTTP 요청을 가로채서 컨텍스트 정보(상관관계 ID 등)를 추출해 CustomContext 클래스에 매핑하는 HTTP 서블릿 필터
 * REST 서비스 호출 시 코드에서 CustomContext 액세스가 필요할 때마다 ThreadLocal 변수에서 검색해 읽어올 수 있음
 */
@Component
public class CustomContextFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CustomContextFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        // HTTP 호출 헤더에서 상관관계 ID 를 검색하여 CustomContextHolder 의 CustomContext 클래스에 설정
        CustomContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader(CustomContext.CORRELATION_ID));
        // 그 외 필요한 항목 넣을 수 있음 (인증 토큰 등...)

        logger.debug("상관관계 ID {} 로 실행된 동적 라우팅", CustomContextHolder.getContext().getCorrelationId());
        System.out.println("상관관계 ID 로 실행된 동적 라우팅" + CustomContextHolder.getContext().getCorrelationId());

        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}
