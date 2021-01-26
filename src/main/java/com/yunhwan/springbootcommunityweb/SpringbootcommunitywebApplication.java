package com.yunhwan.springbootcommunityweb;

import com.yunhwan.springbootcommunityweb.web.domain.Board;
import com.yunhwan.springbootcommunityweb.web.domain.BoardType;
import com.yunhwan.springbootcommunityweb.web.domain.User;
import com.yunhwan.springbootcommunityweb.web.repository.BoardRepository;
import com.yunhwan.springbootcommunityweb.web.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
public class SpringbootcommunitywebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootcommunitywebApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(UserRepository userRepository, BoardRepository boardRepository)
                                                                                    throws Exception {
        return (args) -> {
            User user = userRepository.save(User.builder()
                    .name("havi")
                    .password("test")
                    .email("havi@gmail.com")
                    .createdDate(LocalDateTime.now())
                    .build());

            IntStream.rangeClosed(1, 200).forEach(index ->
                    boardRepository.save(Board.builder()
                    .title("게시글"+index)
                    .subtitle("순서"+index)
                    .content("콘텐츠")
                    .boardType(BoardType.free)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .user(user)
                    .build()));
        };
    }
}
