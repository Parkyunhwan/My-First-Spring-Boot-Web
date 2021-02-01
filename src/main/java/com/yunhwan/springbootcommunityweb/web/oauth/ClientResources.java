package com.yunhwan.springbootcommunityweb.web.oauth;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

/*
* 소셜 미디어 리소스 프로퍼티를 객체로 매핑해주는 ClientResources 객체
* */
public class ClientResources {

    @NestedConfigurationProperty // 소셜미디어 세곳의 프로퍼티를 각각 바인딩하므로
    private AuthorizationCodeResourceDetails client = // 각 소셜의 프로퍼티 중 'client'를 기준 하위 키/값을 매핑해주는 객체
            new AuthorizationCodeResourceDetails();

    @NestedConfigurationProperty
    private ResourceServerProperties resource = new ResourceServerProperties(); // 원래는 OAuth2 리소스 값 매핑, 여기선, userInfoUri 얻어옴

    public AuthorizationCodeResourceDetails getClient() {
        return client;
    }

    public ResourceServerProperties getResource() {
        return resource;
    }
}
