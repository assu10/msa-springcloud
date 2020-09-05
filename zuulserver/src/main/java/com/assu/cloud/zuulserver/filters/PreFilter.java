package com.assu.cloud.zuulserver.filters;

import com.assu.cloud.zuulserver.utils.FilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 사전 필터
 *      주울로 들어오는 모든 요청을 검사하고, 요청에 상관관계 ID 가 HTTP 헤더에 있는지 판별.
 *      상관관계 ID가 없으면 생성
 *      상관관계 ID가 있으면 아무 일도 하지 않음
 */
@Component
public class PreFilter extends ZuulFilter {

    private FilterUtils filterUtils;

    public PreFilter(FilterUtils filterUtils) {
        this.filterUtils = filterUtils;
    }

    /** 해당 타입의 다른 필터와 비교해 실행되어야 하는 순서 */
    private static final int FILTER_ORDER = 1;

    /** 필터 활성화 여부 */
    private static final boolean SHOULD_FILTER = true;
    private static final Logger logger = LoggerFactory.getLogger(PreFilter.class);

    /**
     * 구축하려는 필터의 타입 지정 (사전, 라우팅, 사후)
     */
    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
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
     * 헤더에 assu-correlation-id 가 있는지 확인
     */
    private boolean isCorrelationIdPresent() {
        if (filterUtils.getCorrelationId() != null) {
            return true;
        }
        return false;
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 필터의 비즈니스 로직 구현
     *      서비스가 필터를 통과할때마다 실행되는 메서드
     *      상관관계 ID의 존재 여부 확인 후 없다면 생성하여 헤더에 설정
     */
    @Override
    public Object run() {
        if (isCorrelationIdPresent()) {
            // 헤더에 assu-correlation-id 가 있다면
            logger.debug("============ assu-correlation-id found in pre filter: {}. ", filterUtils.getCorrelationId());
        } else {
            // 헤더에 assu-correlation-id 가 없다면 상관관계 ID 생성하여 RequestContext 의 addZuulRequestHeader 로 추가
            filterUtils.setCorrelationId(generateCorrelationId());
            logger.debug("============ assu-correlation-id generated in pre filter: {}.", filterUtils.getCorrelationId());
        }

        RequestContext ctx = RequestContext.getCurrentContext();
        logger.debug("============ Processing incoming request for {}.",  ctx.getRequest().getRequestURI());

        return null;
    }
}
