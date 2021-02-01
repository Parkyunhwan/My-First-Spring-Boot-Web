package com.yunhwan.springbootcommunityweb.web.controller;

import com.yunhwan.springbootcommunityweb.web.annotation.SocialUser;
import com.yunhwan.springbootcommunityweb.web.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    /*
    * 성공 시 리다이렉트 경로.
    * */
    @GetMapping(value = "/{facebook|google|kakao}/complete")
    public String loginComplete(@SocialUser User user) {
    //public String loginComplete(HttpSession session) {

        /* 이 코드는 불필요한 로직이 많다.
        소셜마다 인증 로직이 상이하므로 특정한 파라미터 형식을 취하면 병렬적으로 User객체에 인증된 정보를 얻어올 수 있게 AOP를 이용해보자.

        OAuth2Authentication authentication = (OAuth2Authentication)
                SecurityContextHolder.getContext().getAuthentication(); // SecurityContextHolder에 인증된 정보를 OAuth2Authentication에 받아옴.
        Map<String, String> map = (HashMap<String, String>)
                authentication.getUserAuthentication().getDetails(); // 리소스 서버에서 받아온 정보를 Map 타입으로 받아서 보려고 함.
        // 세션의 속성 세터를 이용해 유저 객체를 빌더 패턴으로 생성해 유저 속성에 설정(저장)
        session.setAttribute("user", User.builder()
                .name(map.get("name"))
                .email(map.get("email"))
                .principal(map.get("id"))
                .createdDate(LocalDateTime.now())
                .build()
        );
        */

        return "redirect:/board/list";
    }
}
