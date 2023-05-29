package com.ll.grabit.base.initdata;


import com.ll.grabit.boundedcontext.member.form.MemberCreateDto;
import com.ll.grabit.boundedcontext.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev","test"})
public class NotProd {
    @Bean
    public CommandLineRunner initData(
            MemberService memberService
    ) {
        return args -> {
            MemberCreateDto memberCreateDto1 = MemberCreateDto.builder()
                    .username("user1")
                    .password("1234")
                    .confirmPassword("1234")
                    .email("user1@example.com")
                    .nickname("유저1")
                    .phone("01012345678")
                    .build();

            MemberCreateDto memberCreateDto2 = MemberCreateDto.builder()
                    .username("user2")
                    .password("1234")
                    .confirmPassword("1234")
                    .email("user2@example.com")
                    .nickname("유저2")
                    .phone("01012345678")
                    .build();

            MemberCreateDto memberCreateDto3 = MemberCreateDto.builder()
                    .username("user3")
                    .password("1234")
                    .confirmPassword("1234")
                    .email("user3@example.com")
                    .nickname("유저3")
                    .phone("01012345678")
                    .build();

            memberService.join(memberCreateDto1);
            memberService.join(memberCreateDto2);
            memberService.join(memberCreateDto3);
        };
    }
}
