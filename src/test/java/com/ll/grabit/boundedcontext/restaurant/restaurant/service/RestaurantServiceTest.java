package com.ll.grabit.boundedcontext.restaurant.restaurant.service;

import com.ll.grabit.boundedcontext.restaurant.restaurant.dto.RestaurantRegisterDto;
import com.ll.grabit.boundedcontext.restaurant.restaurant.entity.Address;
import com.ll.grabit.boundedcontext.restaurant.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.restaurant.entity.RestaurantType;
import com.ll.grabit.boundedcontext.restaurant.restaurant.repository.AddressRepository;
import com.ll.grabit.boundedcontext.restaurant.restaurant.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RestaurantServiceTest {


    @Autowired
    private RestaurantService restaurantService;

        @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;


    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("식당 등록 테스트")
    void register() {
        RestaurantRegisterDto dto = new RestaurantRegisterDto();
        dto.setRestaurantName("test 식당");
        dto.setDescription("test 식당 소개입니다.");
        dto.setAddress1("서울특별시");
        dto.setAddress2("성북구");
        dto.setAddress3("종암동");
        dto.setDetail_address("고려대학교 앞 빌딩");
        dto.setType("Korean");
        dto.setStartTime("9:30");
        dto.setEndTime("22:30");
        dto.setPerTimeMaxReservationCount(3);


        //when
        Restaurant saveRes = restaurantService.save(dto);
        Address address = saveRes.getAddress();

        em.flush();
        em.clear();

        //then
        Assertions.assertThat(address).isNotNull();

        //저장되었는지 확인
        Restaurant findRes = restaurantRepository.findById(saveRes.getRestaurantId()).get();
        Assertions.assertThat(findRes.getRestaurantId()).isEqualTo(saveRes.getRestaurantId());
    }

}