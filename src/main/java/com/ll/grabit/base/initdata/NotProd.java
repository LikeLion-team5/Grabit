package com.ll.grabit.base.initdata;



import com.ll.grabit.boundedcontext.address.entity.Address;

import com.ll.grabit.boundedcontext.member.form.MemberCreateDto;
import com.ll.grabit.boundedcontext.member.service.MemberService;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantRegisterDto;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;


import java.util.Optional;


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

            MemberCreateDto memberCreateDto4 = MemberCreateDto.builder()
                    .username("admin")
                    .password("1234")
                    .confirmPassword("1234")
                    .email("admin@example.com")
                    .nickname("관리자")
                    .phone("01012345678")
                    .build();

            memberService.join(memberCreateDto1);
            memberService.join(memberCreateDto2);
            memberService.join(memberCreateDto3);
            memberService.join(memberCreateDto4);

            MultipartFile multipartFile = null;

            //서울에 있는 식당 데이터
            RestaurantRegisterDto restaurantDto = new RestaurantRegisterDto();
            restaurantDto.setRestaurantName("식당 이름");
            restaurantDto.setDescription("식당 소개");
            restaurantDto.setType("Korean");
            restaurantDto.setAddress1("서울특별시");
            restaurantDto.setAddress2("성북구");
            restaurantDto.setAddress3("돈암동");
            restaurantDto.setDetail_address("고려대학교 앞 건물 1층");
            restaurantDto.setStartTime("21:00");
            restaurantDto.setEndTime("05:00");
            restaurantDto.setPerTimeMaxReservationCount(3);
            Optional<Address> address = restaurantService.findAddress(restaurantDto);
            for(int i=0; i<20; i++) {
                restaurantService.save(restaurantDto,address.get(), multipartFile);
            }

            restaurantDto.setAddress2("도봉구");
            restaurantDto.setAddress3("창동");
            restaurantDto.setDetail_address("창동역 앞 건물 1층");
            restaurantDto.setStartTime("09:00");
            restaurantDto.setEndTime("22:00");
            address= restaurantService.findAddress(restaurantDto);
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto, address.get(), multipartFile);
            }
            restaurantDto.setAddress3("쌍문동");
            restaurantDto.setDetail_address("쌍문역 앞 건물 1층");
            address= restaurantService.findAddress(restaurantDto);
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto, address.get(), multipartFile);
            }

            restaurantDto.setAddress2("노원구");
            restaurantDto.setAddress3("월계동");
            restaurantDto.setDetail_address("월계역 앞 건물 1층");
            address= restaurantService.findAddress(restaurantDto);
            for(int i=0; i<20; i++) {
                restaurantService.save(restaurantDto, address.get(), multipartFile);
            }

            //인천에 있는 식당 데이터
            restaurantDto.setAddress1("인천광역시");
            restaurantDto.setAddress2("연수구");
            restaurantDto.setAddress3("옥련동");
            restaurantDto.setDetail_address("욕련동 아파트 101동 1015호");
            address= restaurantService.findAddress(restaurantDto);
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto,address.get(), multipartFile);
            }
            restaurantDto.setAddress2("중구");
            restaurantDto.setAddress3("해안동");
            restaurantDto.setDetail_address("욕련동 아파트 101동 1015호");
            address= restaurantService.findAddress(restaurantDto);
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto,address.get(), multipartFile);
            }

            //울산에 있는 식당 데이터
            restaurantDto.setAddress1("울산광역시");
            restaurantDto.setAddress2("남구");
            restaurantDto.setAddress3("남화동");
            restaurantDto.setDetail_address("남화동 아파트 101동 1015호");
            address= restaurantService.findAddress(restaurantDto);
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto,address.get(), multipartFile);
            }
            restaurantDto.setAddress2("동구");
            restaurantDto.setAddress3("동부동");
            restaurantDto.setDetail_address("동부동 아파트 101동 1015호");
            address= restaurantService.findAddress(restaurantDto);
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto, address.get(), multipartFile);
            }

            //부천에 있는 식당 데이터
            restaurantDto.setAddress1("부천시");
            restaurantDto.setAddress2("원미동");
            restaurantDto.setAddress3("");
            restaurantDto.setDetail_address("원미동 아파트 101동 1015호");
            address= restaurantService.findAddress(restaurantDto);
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto, address.get(), multipartFile);
            }
            restaurantDto.setAddress2("오정동");
            restaurantDto.setDetail_address("오정동 아파트 101동 1015호");
            address= restaurantService.findAddress(restaurantDto);
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto, address.get(), multipartFile);
            }
        };
    }
}
