package com.yunhwan.springbootcommunityweb.web.oauth;

import com.yunhwan.springbootcommunityweb.web.domain.enums.SocialType;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.List;
import java.util.Map;

public class UserTokenService extends UserInfoTokenServices {
    public UserTokenService(ClientResources resources, SocialType socialType) {
        super(resources.getResource().getUserInfoUri(), resources.getClient().getClientId()); // uri와 clientID 정보 보냄 -> 각각의 소셜미디어 정보 주
        setAuthoritiesExtractor(new OAuth2AuthoritiesExtractor(socialType)); // 받은 권한 등록
    }

    public static class OAuth2AuthoritiesExtractor implements AuthoritiesExtractor {
        private String socialType;

        public OAuth2AuthoritiesExtractor(SocialType socialType) {
            this.socialType = socialType.getRoleType();
        }

        @Override // 권한을 리스트 형태로 반환.
        public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
            return AuthorityUtils.createAuthorityList(this.socialType);
        }
    }

}
