package com.assu.cloud.memberservice.utils;

import org.springframework.util.Assert;

/**
 * ThreadLocal 저장소에 CustomContext 를 저장하는 클래스
 *      * ThreadLocal 변수: 사용자 요청을 처리하는 해당 스레드에서 호출되는 모든 메서드에서 액세스 가능한 변수
 *
 * CustomContext 가 ThreadLocal 저장소에 저장되면 요청으로 실행된 모든 코드에서 CustomContextHolder 의 CustomContext 객체 사용 가능
 */
public class CustomContextHolder {

    /** 정적 ThreadLocal 변수에 저장되는 CustomContext */
    private static final ThreadLocal<CustomContext> customContext = new ThreadLocal<>();

    /**
     * CustomContext 객체를 사용하기 위해 조회해오는 메서드
     */
    public static final CustomContext getContext() {
        CustomContext ctx = customContext.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            customContext.set(ctx);
        }
        return customContext.get();
    }

    public static final void setContext(CustomContext ctx) {
        Assert.notNull(ctx, "CustomContext is null.");
        customContext.set(ctx);
    }

    public static final CustomContext createEmptyContext() {
        return new CustomContext();
    }
}
