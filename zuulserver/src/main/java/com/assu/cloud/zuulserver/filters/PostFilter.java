package com.assu.cloud.zuulserver.filters;

import com.assu.cloud.zuulserver.utils.FilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 사후 필터
 *      서비스 호출자에게 다시 전달될 HTTP 응답 헤더에 상관관계 ID 를 삽입하여 사용자 트랜잭션과 연관된 로깅을 연결 지음
 */
@Component
public class PostFilter extends ZuulFilter {
    /** 해당 타입의 다른 필터와 비교해 실행되어야 하는 순서 */
    private static final int FILTER_ORDER = 1;

    /** 필터 활성화 여부 */
    private static final boolean SHOULD_FILTER = true;
    private static final Logger logger = LoggerFactory.getLogger(PostFilter.class);

    private final FilterUtils filterUtils;

    public PostFilter(FilterUtils filterUtils) {
        this.filterUtils = filterUtils;
    }

    /**
     * 구축하려는 필터의 타입 지정 (사전, 라우팅, 사후)
     */
    @Override
    public String filterType() {
        return FilterUtils.POST_FILTER_TYPE;
    }

    /**
     * 해당 타입의 다른 필터와 비교해 실행되어야 하는 순서
     */
    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    /**
     * 필터 활성화 여부
     */
    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    /**
     * 필터의 비즈니스 로직 구현
     *      원래 HTTP 요청에서 전달된 상관관계 ID 를 가져와 응답에 삽입
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

        logger.debug("============ Adding the correlation id to the outbound headers. {}", filterUtils.getCorrelationId());
        // 원래 HTTP 요청에서 전달된 상관관계 ID 를 가져와 응답에 삽입
        ctx.getResponse().addHeader(FilterUtils.CORRELATION_ID, filterUtils.getCorrelationId());
        logger.debug("============ Completing outgoing request for {}.", ctx.getRequest().getRequestURI());

        return null;
    }
}