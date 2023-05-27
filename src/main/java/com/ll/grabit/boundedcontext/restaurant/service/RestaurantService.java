package com.ll.grabit.boundedcontext.restaurant.service;

import com.ll.grabit.boundedcontext.restaurant.repository.AddressRepository;
import com.ll.grabit.boundedcontext.restaurant.repository.RestaurantRepository;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantRegisterDto;
import com.ll.grabit.boundedcontext.restaurant.entity.Address;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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
}
