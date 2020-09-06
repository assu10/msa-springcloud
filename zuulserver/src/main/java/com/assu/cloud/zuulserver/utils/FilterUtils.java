package com.assu.cloud.zuulserver.utils;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

/**
 * 필터에서 사용되는 공통 기능
 */
@Component
public class FilterUtils {
    public static final String CORRELATION_ID = "assu-correlation-id";
    public static final String PRE_FILTER_TYPE = "pre";
    public static final String POST_FILTER_TYPE = "post";
    public static final String ROUTING_FILTER_TYPE = "route";

    /**
     * HTTP 헤더에서 assu-correlation-id 조회
     */
    public String getCorrelationId() {
        RequestContext ctx = RequestContext.getCurrentContext();

        if (ctx.getRequest().getHeader(CORRELATION_ID) != null) {
            // assu-correlation-id 가 이미 설정되어 있다면 해당값 리턴
            System.out.println("이미 있음 :" + ctx.getRequest().getHeader(CORRELATION_ID) + "++");
            return ctx.getRequest().getHeader(CORRELATION_ID);
        } else {
            // 헤더에 없다면 ZuulRequestHeaders 확인
            // 주울은 유입되는 요청에 직접 HTTP 요청 헤더를 추가하거나 수정하지 않음
            System.out.println("없어서 만듬 :" + ctx.getZuulRequestHeaders().get(CORRELATION_ID) + "++");
            return ctx.getZuulRequestHeaders().get(CORRELATION_ID);
        }
    }

    /**
     * HTTP 요청 헤더에 상관관계 ID 추가
     *      이 때 RequestContext 에 addZuulRequestHeader() 메서드로 추가해야 함
     *
     *      이 메서드는 주울 서버의 필터를 지나는 동안 추가되는 별도의 HTTP 헤더 맵을 관리하는데
     *      ZuulRequestHeader 맵에 보관된 데이터는 주울 서버가 대상 서비스를 호출할 때 합쳐짐
     * @param correlationId 
     */
    public void setCorrelationId(String correlationId) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(CORRELATION_ID, correlationId);
    }
}
