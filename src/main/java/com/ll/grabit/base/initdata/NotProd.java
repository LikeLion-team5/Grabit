package com.ll.grabit.base.initdata;


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
            memberService.join("user1","1234");
            memberService.join("user2","1234");
            memberService.join("user3","1234");

            //서울에 있는 식당 데이터
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

            restaurantDto.setAddress2("도봉구");
            restaurantDto.setAddress3("창동");
            restaurantDto.setDetail_address("창동역 앞 건물 1층");
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto);
            }
            restaurantDto.setAddress3("쌍문동");
            restaurantDto.setDetail_address("쌍문역 앞 건물 1층");
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto);
            }

            restaurantDto.setAddress2("노원구");
            restaurantDto.setAddress3("월계동");
            restaurantDto.setDetail_address("월계역 앞 건물 1층");
            for(int i=0; i<20; i++) {
                restaurantService.save(restaurantDto);
            }

            //인천에 있는 식당 데이터
            restaurantDto.setAddress1("인천광역시");
            restaurantDto.setAddress2("연수구");
            restaurantDto.setAddress3("옥련동");
            restaurantDto.setDetail_address("욕련동 아파트 101동 1015호");
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto);
            }
            restaurantDto.setAddress2("중구");
            restaurantDto.setAddress3("해안동");
            restaurantDto.setDetail_address("욕련동 아파트 101동 1015호");
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto);
            }

            //울산에 있는 식당 데이터
            restaurantDto.setAddress1("울산광역시");
            restaurantDto.setAddress2("남구");
            restaurantDto.setAddress3("남화동");
            restaurantDto.setDetail_address("남화동 아파트 101동 1015호");
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto);
            }
            restaurantDto.setAddress2("동구");
            restaurantDto.setAddress3("동부동");
            restaurantDto.setDetail_address("동부동 아파트 101동 1015호");
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto);
            }

            //부천에 있는 식당 데이터
            restaurantDto.setAddress1("부천시");
            restaurantDto.setAddress2("원미동");
            restaurantDto.setAddress3("");
            restaurantDto.setDetail_address("원미동 아파트 101동 1015호");
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto);
            }
            restaurantDto.setAddress2("오정동");
            restaurantDto.setDetail_address("오정동 아파트 101동 1015호");
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto);
            }
        };
    }
}
