package com.assu.cloud.authservice.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

/**
 * JWTTokenStoreConfig 에서 서명하고 생성한 JWT 토큰을 OAuth2 인증 서버로 연결
 *
 * OAuth2 인증 서버에 등록될 애플리케이션 정의
 *      AuthorizationServerConfigurerAdapter: 스프링 시큐리티 핵심부, 핵심 인증 및 인가 기능 수행하는 기본 메커니즘 제공
 */
@Configuration
public class JWTOAuth2Config extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final TokenStore tokenStore;
    private final DefaultTokenServices defaultTokenServices;
    private final JwtAccessTokenConverter jwtAccessTokenConverter;
    private final JWTTokenEnhancer jwtTokenEnhancer;


    public JWTOAuth2Config(AuthenticationManager authenticationManager, @Qualifier("userDetailsServiceBean") UserDetailsService userDetailsService,
                           TokenStore tokenStore, DefaultTokenServices defaultTokenServices,
                           JwtAccessTokenConverter jwtAccessTokenConverter, JWTTokenEnhancer jwtTokenEnhancer) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenStore = tokenStore;
        this.defaultTokenServices = defaultTokenServices;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
        this.jwtTokenEnhancer = jwtTokenEnhancer;
    }

    /**
     * AuthorizationServerConfigurerAdapter 안에서 사용될 여러 컴포넌트 정의
     * 여기선 스프링에 토큰 스토어, 액세스 토큰 컨버터, 토큰 엔헨서, 기본 인증 관리자와 사용자 상세 서비스를 이용한다고 선언
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 스프링 OAuth 의 TokenEnhancerChain 를 등록하면 여러 TokenEnhancer 후킹 가능
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer, jwtAccessTokenConverter));

        endpoints.tokenStore(tokenStore)                             // JWT, JWTTokenStoreConfig 에서 정의한 토큰 저장소
                .accessTokenConverter(jwtAccessTokenConverter)       // JWT, 스프링 시큐리티 OAuth2 가 JWT 사용하도록 연결
                .tokenEnhancer(tokenEnhancerChain)                   // JWT, endpoints 에 tokenEnhancerChain 연결
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    /**
     * 인증 서버에 등록될 클라이언트 정의
     * 즉, OAuth2 서비스로 보호되는서비스에 접근할 수 있는 클라이언트 애플리케이션 등록
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()      // 애플리케이션 정보를 위한 저장소 (인메모리 / JDBC)
                .withClient("assuapp")      // assuapp 애플리케이션이 토큰을 받기 위해 인증 서버 호출 시 제시할 시크릿과 애플리케이션명
                .secret(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("12345"))
                .authorizedGrantTypes("refresh_token", "password", "client_credentials")    // OAuth2 에서 지원하는 인가 그랜트 타입, 여기선 패스워드/클라이언트 자격증명 그랜트타입
                .scopes("webclient", "mobileclient");       // 토큰 요청 시 애플리케이션의 수행 경계 정의
    }
}
