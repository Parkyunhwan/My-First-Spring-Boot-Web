package com.yunhwan.springbootcommunityweb.web.domain;

import com.yunhwan.springbootcommunityweb.web.domain.enums.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table
public class User implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String principal;

    @Column
    @Enumerated(EnumType.STRING) // 소셜타입을 유저 테이블의 속성을 볼수 있다. String이므로 문자열자체가 DB에 저장됨
    private SocialType socialType;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime updatedDate;

    @Builder
    public User(String name, String password, String email, String principal, SocialType socialType
                ,LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.name= name;
        this.password = password;
        this.email = email;
        this.principal = principal;
        this.socialType = socialType;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
