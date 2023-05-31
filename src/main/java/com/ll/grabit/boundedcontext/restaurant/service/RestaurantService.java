package com.ll.grabit.boundedcontext.restaurant.service;

import com.ll.grabit.base.exception.NotFoundDataException;
import com.ll.grabit.boundedcontext.restaurant.dto.AddressSearchDto;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantRegisterDto;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantUpdateDto;
import com.ll.grabit.boundedcontext.restaurant.entity.Address;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.repository.AddressRepository;
import com.ll.grabit.boundedcontext.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional

public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;


    public Restaurant save(RestaurantRegisterDto restaurantRegisterDto) {
        //주소 뽑아내기
        Optional<Address> findAddress = addressRepository.findByAddress1AndAddress2AndAddress3(restaurantRegisterDto.getAddress1(),
                restaurantRegisterDto.getAddress2(), restaurantRegisterDto.getAddress3());

        //오픈 시간, 마감시간 LocalTime 으로 뽑아내기
        LocalTime startTime = extractedLocalTime(restaurantRegisterDto.getStartTime());
        LocalTime endTime = extractedLocalTime(restaurantRegisterDto.getEndTime());

        //DTO -> Entity
        Restaurant restaurant = restaurantRegisterDto.toEntity(findAddress.get(), startTime, endTime);

        //식당 저장
        Restaurant saveRestaurant = restaurantRepository.save(restaurant);
        return saveRestaurant;
    }

    private static LocalTime extractedLocalTime(String time) {
        String[] split = time.split(":");
        LocalTime localTime = LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        return localTime;
    }


    public Restaurant findOne(Long id) {
        Optional<Restaurant> findRestaurant = restaurantRepository.findById(id);
        Restaurant restaurant = findRestaurant.orElseThrow(
                () -> new NotFoundDataException("Invalid access: No restaurant found with id : " + id));
        return findRestaurant.get();
    }

    public void update(Long id, RestaurantUpdateDto restaurantUpdateDto) {
        Restaurant findRestaurant = findOne(id);
        Address address = addressRepository.findByAddress1AndAddress2AndAddress3(restaurantUpdateDto.getAddress1(),
                        restaurantUpdateDto.getAddress2(), restaurantUpdateDto.getAddress3())
                .orElseThrow(
                        () -> new NotFoundDataException("해당하는 Address 를 찾을 수 없습니다.")
                );
        LocalTime startTime = extractedLocalTime(restaurantUpdateDto.getStartTime());
        LocalTime endTime = extractedLocalTime(restaurantUpdateDto.getEndTime());
        findRestaurant.update(restaurantUpdateDto, address, startTime, endTime);
    }

    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }

    public Page<Restaurant> search(
      Dto addressSearchDto, Pageable pageable) {
        String address1 = addressSearchDto.getAddress1();
        String address2 = addressSearchDto.getAddress2();
        String address3 = addressSearchDto.getAddress3();

        //주소 필터링
        List<Address> addressList = filteredAddress(address1, address2, address3);

        //주소에 해당하는 식당 리스트 뽑아오기
        Page<Restaurant> restaurantList = null;
        if(addressList.size() == 0){
            restaurantList = restaurantRepository.findAll(pageable);
        }else{
            restaurantList = restaurantRepository.findByAddressIn(addressList, pageable);
        }

        return restaurantList;
    }

    private List<Address> filteredAddress(String address1, String address2, String address3) {
        List<Address> addressList = new ArrayList<>();
        if (address3 != null && !address3.isEmpty()) {
            addressList = addressRepository.findAddress3(address1, address2, address3);
        }
        else if (address2 != null && !address2.isEmpty()) {
            addressList = addressRepository.findAddress2(address1, address2);
        }
        else if(address1 != null && !address1.isEmpty()) {
            addressList = addressRepository.findAddress1(address1);
        }

        return addressList;
    }
}
