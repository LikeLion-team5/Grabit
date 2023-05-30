package com.ll.grabit.base.initdata;


import com.ll.grabit.boundedcontext.member.form.MemberCreateDto;
import com.ll.grabit.boundedcontext.member.service.MemberService;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantRegisterDto;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev","test"})
public class NotProd {
    @Bean
    public CommandLineRunner initData(
            MemberService memberService,
            RestaurantService restaurantService
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

            RestaurantRegisterDto restaurantDto = new RestaurantRegisterDto();
            restaurantDto.setRestaurantName("식당 이름");
            restaurantDto.setDescription("식당 소개");
            restaurantDto.setType("Korean");
            restaurantDto.setAddress1("서울특별시");
            restaurantDto.setAddress2("성북구");
            restaurantDto.setAddress3("돈암동");
            restaurantDto.setDetail_address("고려대학교 앞 건물 1층");
            restaurantDto.setStartTime("09:30");
            restaurantDto.setEndTime("23:30");
            restaurantDto.setPerTimeMaxReservationCount(3);
            for(int i=0; i<20; i++) {
                restaurantService.save(restaurantDto);
            }
        };
    }
}
