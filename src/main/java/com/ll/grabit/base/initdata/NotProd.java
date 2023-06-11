package com.ll.grabit.base.initdata;

import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.address.entity.Address;
import com.ll.grabit.boundedcontext.member.form.MemberCreateDto;
import com.ll.grabit.boundedcontext.member.service.MemberService;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantRegisterDto;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationRequestDto;
import com.ll.grabit.boundedcontext.reservation.service.ReservationService;
import com.ll.grabit.boundedcontext.review.service.ReviewService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;


@Configuration
@Profile({"dev","test"})
public class NotProd {
    @Bean
    public CommandLineRunner initData(
            MemberService memberService,
            RestaurantService restaurantService,
            ReservationService reservationService,
            ReviewService reviewService
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
          
            Member member1 = memberService.join(memberCreateDto1).getData();
            Member member2 = memberService.join(memberCreateDto2).getData();
            Member member3 = memberService.join(memberCreateDto3).getData();
            Member member4 = memberService.join(memberCreateDto4).getData();

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
            Optional<Address> address = restaurantService.findAddress(
                    restaurantDto.getAddress1(), restaurantDto.getAddress2(), restaurantDto.getAddress3()
            );
            for(int i=0; i<20; i++) {
                restaurantService.save(restaurantDto,address.get(), multipartFile);
            }

            Restaurant restaurant1 = restaurantService.save(restaurantDto, address.get(), multipartFile);
            Restaurant restaurant2 = restaurantService.save(restaurantDto, address.get(), multipartFile);
            Restaurant restaurant3 = restaurantService.save(restaurantDto, address.get(), multipartFile);

            restaurantDto.setAddress2("도봉구");
            restaurantDto.setAddress3("창동");
            restaurantDto.setDetail_address("창동역 앞 건물 1층");
            restaurantDto.setStartTime("09:00");
            restaurantDto.setEndTime("22:00");
            address = restaurantService.findAddress(
                    restaurantDto.getAddress1(), restaurantDto.getAddress2(), restaurantDto.getAddress3()
            );
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto, address.get(), multipartFile);
            }
            restaurantDto.setAddress3("쌍문동");
            restaurantDto.setDetail_address("쌍문역 앞 건물 1층");
            address = restaurantService.findAddress(
                    restaurantDto.getAddress1(), restaurantDto.getAddress2(), restaurantDto.getAddress3()
            );
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto, address.get(), multipartFile);
            }

            restaurantDto.setAddress2("노원구");
            restaurantDto.setAddress3("월계동");
            restaurantDto.setDetail_address("월계역 앞 건물 1층");
            address = restaurantService.findAddress(
                    restaurantDto.getAddress1(), restaurantDto.getAddress2(), restaurantDto.getAddress3()
            );
            for(int i=0; i<20; i++) {
                restaurantService.save(restaurantDto, address.get(), multipartFile);
            }

            //인천에 있는 식당 데이터
            restaurantDto.setAddress1("인천광역시");
            restaurantDto.setAddress2("연수구");
            restaurantDto.setAddress3("옥련동");
            restaurantDto.setDetail_address("욕련동 아파트 101동 1015호");
            address = restaurantService.findAddress(
                    restaurantDto.getAddress1(), restaurantDto.getAddress2(), restaurantDto.getAddress3()
            );
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto,address.get(), multipartFile);
            }
            restaurantDto.setAddress2("중구");
            restaurantDto.setAddress3("해안동");
            restaurantDto.setDetail_address("욕련동 아파트 101동 1015호");
            address = restaurantService.findAddress(
                    restaurantDto.getAddress1(), restaurantDto.getAddress2(), restaurantDto.getAddress3()
            );
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto,address.get(), multipartFile);
            }

            //울산에 있는 식당 데이터
            restaurantDto.setAddress1("울산광역시");
            restaurantDto.setAddress2("남구");
            restaurantDto.setAddress3("남화동");
            restaurantDto.setDetail_address("남화동 아파트 101동 1015호");
            address = restaurantService.findAddress(
                    restaurantDto.getAddress1(), restaurantDto.getAddress2(), restaurantDto.getAddress3()
            );
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto,address.get(), multipartFile);
            }
            restaurantDto.setAddress2("동구");
            restaurantDto.setAddress3("동부동");
            restaurantDto.setDetail_address("동부동 아파트 101동 1015호");
            address = restaurantService.findAddress(
                    restaurantDto.getAddress1(), restaurantDto.getAddress2(), restaurantDto.getAddress3()
            );
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto, address.get(), multipartFile);
            }

            //부천에 있는 식당 데이터
            restaurantDto.setAddress1("부천시");
            restaurantDto.setAddress2("원미동");
            restaurantDto.setAddress3("");
            restaurantDto.setDetail_address("원미동 아파트 101동 1015호");
            address = restaurantService.findAddress(
                    restaurantDto.getAddress1(), restaurantDto.getAddress2(), restaurantDto.getAddress3()
            );
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto, address.get(), multipartFile);
            }
            restaurantDto.setAddress2("오정동");
            restaurantDto.setDetail_address("오정동 아파트 101동 1015호");
            address = restaurantService.findAddress(
                    restaurantDto.getAddress1(), restaurantDto.getAddress2(), restaurantDto.getAddress3()
            );
            for(int i=0; i<10; i++) {
                restaurantService.save(restaurantDto, address.get(), multipartFile);
            }

            ReservationRequestDto reservationRequestDto1 = new ReservationRequestDto();
            reservationRequestDto1.setMemberId(
              
              .getId());
            reservationRequestDto1.setRestaurantId(restaurant2.getRestaurantId());
            reservationRequestDto1.setName("예약자1");
            reservationRequestDto1.setPhone("01012345678");
            reservationRequestDto1.setDate(LocalDate.parse("2023-06-20"));
            reservationRequestDto1.setReservationTime(LocalTime.parse("18:00"));
            reservationRequestDto1.setPartySize(3);
            Long reservationId1 = reservationService.createReservation(reservationRequestDto1);

            ReservationRequestDto reservationRequestDto2 = new ReservationRequestDto();
            reservationRequestDto2.setMemberId(member1.getId());
            reservationRequestDto2.setRestaurantId(restaurant2.getRestaurantId());
            reservationRequestDto2.setName("예약자2");
            reservationRequestDto2.setPhone("01098765432");
            reservationRequestDto2.setDate(LocalDate.parse("2023-06-21"));
            reservationRequestDto2.setReservationTime(LocalTime.parse("14:00"));
            reservationRequestDto2.setPartySize(2);
            Long reservationId2 = reservationService.createReservation(reservationRequestDto2);

            reservationService.confirmReservation(reservationId2);

            ReservationRequestDto reservationRequestDto3 = new ReservationRequestDto();
            reservationRequestDto3.setMemberId(member1.getId());
            reservationRequestDto3.setRestaurantId(restaurant3.getRestaurantId());
            reservationRequestDto3.setName("예약자3");
            reservationRequestDto3.setPhone("01081345432");
            reservationRequestDto3.setDate(LocalDate.parse("2023-06-22"));
            reservationRequestDto3.setReservationTime(LocalTime.parse("13:00"));
            reservationRequestDto3.setPartySize(4);
            Long reservationId3 = reservationService.createReservation(reservationRequestDto3);

            reservationService.confirmReservation(reservationId3);
            reservationService.completeReservation(reservationId3);





            reviewService.addReview("맛있었습니다!!", 3, restaurant1.getRestaurantId(), member1.getId());
            reviewService.addReview("맛없어요!!", 1, restaurant1.getRestaurantId(), member2.getId());
            reviewService.addReview("적당해요!!", 3, restaurant1.getRestaurantId(), member3.getId());

            reviewService.addReview("맛있었습니다!!", 5, restaurant2.getRestaurantId(), member1.getId());
            reviewService.addReview("맛없어요!!", 1, restaurant2.getRestaurantId(), member2.getId());
            reviewService.addReview("적당해요!!", 3, restaurant2.getRestaurantId(), member3.getId());

            reviewService.addReview("맛있었습니다!!", 5, restaurant3.getRestaurantId(), member1.getId());
            reviewService.addReview("맛없어요!!", 1, restaurant3.getRestaurantId(), member2.getId());
            reviewService.addReview("적당해요!!", 3, restaurant3.getRestaurantId(), member3.getId());
        };
    }
}
