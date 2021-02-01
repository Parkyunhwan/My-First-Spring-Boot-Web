package com.yunhwan.springbootcommunityweb.web.config;

import com.yunhwan.springbootcommunityweb.web.domain.enums.SocialType;
import com.yunhwan.springbootcommunityweb.web.oauth.ClientResources;
import com.yunhwan.springbootcommunityweb.web.oauth.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.xml.OAuth2ClientContextFactoryBean;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import java.util.ArrayList;
import java.util.List;

import static com.yunhwan.springbootcommunityweb.web.domain.enums.SocialType.*;


/*
* 각 소셜미디어 리소스 정보를 빈으로 등록
* @ConfigurationProperties를 이용해 application.xxx설정 키 값을 묶어서 빈으로 등록할 수 있다.
* */
@Configuration
@EnableWebSecurity // '웹에서 시큐리티 기능을 사용하겠다' -> 스프링부트가 자동설정 해줌.
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http.authorizeRequests()// HttpServletRequest 기반 설정
                .antMatchers("/", "/login/**", "/css/**", "/images/**", "/js/**",
                        "/console/**").permitAll() // 요청 패턴을 리스트 형식으로 설정하며 누구나 접근할 수 있도록 허용합니다.
                .anyRequest().authenticated() // 설정한 요청 이외의 리퀘스트 요청(anyRequest) -> 인증된 사용자만 요청 가능
                .and()

                .headers().frameOptions().disable() // 응답에 해당하는 header설정, 최적화 옵션 끄기
                .and()

                .exceptionHandling() //예외 핸들링
                // EntryPoint는 인증의 진입 지점이며 인증되지 않은 사용자가 접근 시 '/login'으로 이동.
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .and()

                .formLogin()
                .successForwardUrl("/board/list") // 로그인에 성공하면 설정된 경로로 포워딩 됩니다.
                .and()

                .logout() // 로그아웃에 대한 설정을 진행
                .logoutUrl("/logout") // 로그아웃을 진행할 URL
                .logoutSuccessUrl("/") // 로그아웃 성공 시 포워딩 될 URL
                .deleteCookies("JSESSIONID") // 로그아웃 성공했을 때 삭제될 쿠키값
                .invalidateHttpSession(true) // 설정된 세션의 무효화
                .and()

                .addFilterBefore(filter, CsrfFilter.class) // 첫번째 인자보다 먼저 시작될 필터를 등록 => 문자 인코딩 필터 보다 CSRF 필터를 먼저 실행
                .addFilterBefore(oauth2Filter(), BasicAuthenticationFilter.class)
                .csrf().disable();
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration( // 올바른 순서로 필터가 작동하게함
            OAuth2ClientContextFilter filter) { // OAuth2ClientContextFilter -> OAuth 클라이언트용 시큐리티 필터
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    // 각 소셜 미디어 필터를 '리스트 형식'으로 한꺼번에 설정하여 반환
    private Filter oauth2Filter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(oauth2Filter(facebook(), "/login/facebook", FACEBOOK));
        filters.add(oauth2Filter(google(), "/login/google", GOOGLE));
        filters.add(oauth2Filter(kakao(), "/login/kakao", KAKAO));
        filter.setFilters(filters);
        return filter;
    }

    // 소셜 미디어 타입을 받아서 필터 설정
    private Filter oauth2Filter(ClientResources client, String path,
                                SocialType socialType) {
        OAuth2ClientAuthenticationProcessingFilter filter =
                new OAuth2ClientAuthenticationProcessingFilter(path); // 1. 인증이 수행될 경로를 넣어 OAuth2 클라이언트용 인증 처리 필터 생성
        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(),
                oAuth2ClientContext); // 권한 서버와 통신하기 위해 템플릿 생성 -> 이를 성공시키기 위해선 'client프로퍼티' & 'OAuth2ClientContext'가 필요.
        filter.setRestTemplate(template);
        filter.setTokenServices(new UserTokenService(client, socialType)); // User권한 최적화 생성 (UserTokenService)
        filter.setAuthenticationSuccessHandler((request, response, authentication) // 성공 시 필터에 리다이렉트될 URL
                -> response.sendRedirect("/" + socialType.getValue() +
                "/complete"));
        filter.setAuthenticationFailureHandler(((request, response, exception) -> // 실패 시 필터에 리다이렉트될 URL
                response.sendRedirect("/error")));
        return filter;
    }

    @Bean
    @ConfigurationProperties("facebook")
    public ClientResources facebook() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("google")
    public ClientResources google() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("kakao")
    public ClientResources kakao() {
        return new ClientResources();
    }

}
