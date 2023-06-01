package com.ll.grabit.boundedcontext.restaurant.restaurant.service;

import com.ll.grabit.boundedcontext.restaurant.dto.AddressSearchDto;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantRegisterDto;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantUpdateDto;
import com.ll.grabit.boundedcontext.restaurant.entity.Address;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.repository.AddressRepository;
import com.ll.grabit.boundedcontext.restaurant.repository.RestaurantRepository;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
    void register() throws IOException {
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
        Restaurant saveRes = restaurantService.save(dto, null);
        Address address = saveRes.getAddress();

        em.flush();
        em.clear();

        //then
        assertThat(address).isNotNull();

        //저장되었는지 확인
        Restaurant findRes = restaurantRepository.findById(saveRes.getRestaurantId()).get();
        assertThat(findRes.getRestaurantId()).isEqualTo(saveRes.getRestaurantId());
    }

    @Test
    @DisplayName("식당 수정 테스트")
    void update(){
        //given
        Restaurant beforeRes = restaurantService.findOne(1L);

        //when
        RestaurantUpdateDto updateDto = new RestaurantUpdateDto();
        updateDto.setRestaurantName("식당수정");
        updateDto.setDescription("식당소개수정");
        updateDto.setType("Korean");
        updateDto.setAddress1("서울특별시");
        updateDto.setAddress2("도봉구");
        updateDto.setAddress3("창동");
        updateDto.setDetail_address("창동역 1번출구 바로 앞 건물");
        updateDto.setStartTime("09:30");
        updateDto.setEndTime("23:30");
        updateDto.setPerTimeMaxReservationCount(3);
        restaurantService.update(1L,updateDto);

        //then
        Restaurant afterRes = restaurantService.findOne(1L);

        assertThat(afterRes.getRestaurantName()).isEqualTo("식당수정");
        assertThat(afterRes.getDescription()).isEqualTo("식당소개수정");
        assertThat(afterRes.getAddress().getAddress1()).isEqualTo("서울특별시");
        assertThat(afterRes.getAddress().getAddress2()).isEqualTo("도봉구");
        assertThat(afterRes.getAddress().getAddress3()).isEqualTo("창동");
        assertThat(afterRes.getDetail_address()).isEqualTo("창동역 1번출구 바로 앞 건물");

    }

    @Test
    @DisplayName("식당 삭제 테스트")
    void delete(){
        long beforeCnt = restaurantRepository.count();

        restaurantService.delete(1L);

        long afterCnt = restaurantRepository.count();

        assertThat(beforeCnt).isNotEqualTo(afterCnt);
        assertThat(beforeCnt-1).isEqualTo(afterCnt);
    }

    @Test
    @DisplayName("대주소로 식당 검색")
    void search01(){

        AddressSearchDto addressSearchDto = new AddressSearchDto();
        addressSearchDto.setAddress1("서울특별시");
        addressSearchDto.setAddress2("");
        addressSearchDto.setAddress3("");

        PageRequest pageRequest = PageRequest.of(0, 8);
        Page<Restaurant> search = restaurantService.search(addressSearchDto, pageRequest);

        assertThat(search.getNumberOfElements()).isEqualTo(8);
        assertThat(search.getTotalElements()).isEqualTo(60);
        assertThat(search.getTotalPages()).isEqualTo(8);
        assertThat(search.getNumberOfElements()).isEqualTo(8);
    }
    @Test
    @DisplayName("대주소 + 중주소로 식당 검색")
    void search02(){
        AddressSearchDto addressSearchDto = new AddressSearchDto();
        addressSearchDto.setAddress1("서울특별시");
        addressSearchDto.setAddress2("도봉구");
        addressSearchDto.setAddress3("");

        PageRequest pageRequest = PageRequest.of(0, 8);
        Page<Restaurant> search = restaurantService.search(addressSearchDto, pageRequest);

        assertThat(search.getNumberOfElements()).isEqualTo(8);
        assertThat(search.getTotalElements()).isEqualTo(20);
        assertThat(search.getTotalPages()).isEqualTo(3);
        assertThat(search.getNumberOfElements()).isEqualTo(8);
    }
    @Test
    @DisplayName("대주소 + 중주소 + 소주소로 식당 검색")
    void search03(){
        AddressSearchDto addressSearchDto = new AddressSearchDto();
        addressSearchDto.setAddress1("서울특별시");
        addressSearchDto.setAddress2("도봉구");
        addressSearchDto.setAddress3("쌍문동");

        PageRequest pageRequest = PageRequest.of(0, 8);
        Page<Restaurant> search = restaurantService.search(addressSearchDto, pageRequest);

        assertThat(search.getNumberOfElements()).isEqualTo(8);
        assertThat(search.getTotalElements()).isEqualTo(10);
        assertThat(search.getTotalPages()).isEqualTo(2);
        assertThat(search.getNumberOfElements()).isEqualTo(8);

        addressSearchDto.setAddress3("창동");
        search = restaurantService.search(addressSearchDto, pageRequest);
        assertThat(search.getNumberOfElements()).isEqualTo(8);
        assertThat(search.getTotalElements()).isEqualTo(10);
        assertThat(search.getTotalPages()).isEqualTo(2);
        assertThat(search.getNumberOfElements()).isEqualTo(8);
    }

}