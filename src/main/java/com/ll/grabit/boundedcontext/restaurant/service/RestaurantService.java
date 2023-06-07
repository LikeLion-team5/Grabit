package com.ll.grabit.boundedcontext.restaurant.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.grabit.base.exception.NotFoundDataException;
import com.ll.grabit.base.s3.S3Uploader;
import com.ll.grabit.boundedcontext.address.dto.AddressSearchDto;
import com.ll.grabit.boundedcontext.menu.dto.MenuRegisterDto;
import com.ll.grabit.boundedcontext.menu.entity.Menu;
import com.ll.grabit.boundedcontext.menu.repository.MenuRepository;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantRegisterDto;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantUpdateDto;
import com.ll.grabit.boundedcontext.address.entity.Address;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurantimage.entity.RestaurantImage;
import com.ll.grabit.boundedcontext.address.repository.AddressRepository;
import com.ll.grabit.boundedcontext.restaurantimage.repository.RestaurantImageRepository;
import com.ll.grabit.boundedcontext.restaurant.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    private final RestaurantImageRepository restaurantImageRepository;
    private final S3Uploader s3Uploader;

    private final MenuRepository menuRepository;

    @Value("${cloud.ncp.s3.dir}")
    private String dir;

    private final EntityManager em;


    public Restaurant save(RestaurantRegisterDto restaurantRegisterDto, Address address, MultipartFile multipartFiles) throws IOException {
        //주소 뽑아내기
//        Optional<Address> findAddress = getFindAddress(restaurantRegisterDto);


        //오픈 시간, 마감시간 LocalTime 으로 뽑아내기
        LocalTime startTime = extractedLocalTime(restaurantRegisterDto.getStartTime());
        LocalTime endTime = extractedLocalTime(restaurantRegisterDto.getEndTime());

        //DTO -> Entity
        Restaurant restaurant = restaurantRegisterDto.toEntity(address, startTime, endTime);

        //string 메뉴 리스트 -> 메뉴리스트 전환 -> 메뉴 저장
        ObjectMapper objectMapper = new ObjectMapper();
        if(restaurantRegisterDto.getMenuRegisterDtoList() != null){
            List<MenuRegisterDto> menuRegisterDtoList = objectMapper.readValue(restaurantRegisterDto.getMenuRegisterDtoList(), new TypeReference<List<MenuRegisterDto>>(){});
            for (MenuRegisterDto menuRegisterDto : menuRegisterDtoList) {
                Menu menu = menuRegisterDto.toEntity();
                menu.setRestaurant(restaurant);
                menuRepository.save(menu);
            }
        }

        //식당 이미지 저장
        if(multipartFiles != null && !multipartFiles.isEmpty()){
            RestaurantImage image = s3Uploader.uploadFiles(multipartFiles, dir);
            image.setRestaurant(restaurant);
            restaurantImageRepository.save(image);
        }

        //식당 저장
        return restaurantRepository.save(restaurant);
    }

    public Optional<Address> findAddress(RestaurantRegisterDto restaurantRegisterDto) {
        Optional<Address> findAddress = addressRepository.findByAddress1AndAddress2AndAddress3(restaurantRegisterDto.getAddress1(),
                restaurantRegisterDto.getAddress2(), restaurantRegisterDto.getAddress3());
        return findAddress;
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

    public void update(Long id, RestaurantUpdateDto restaurantUpdateDto, MultipartFile multipartFile) throws IOException {
        Restaurant findRestaurant = findOne(id);
        Address address = addressRepository.findByAddress1AndAddress2AndAddress3(restaurantUpdateDto.getAddress1(),
                        restaurantUpdateDto.getAddress2(), restaurantUpdateDto.getAddress3())
                .orElseThrow(
                        () -> new NotFoundDataException("해당하는 Address 를 찾을 수 없습니다.")
                );
        LocalTime startTime = extractedLocalTime(restaurantUpdateDto.getStartTime());
        LocalTime endTime = extractedLocalTime(restaurantUpdateDto.getEndTime());

        RestaurantImage restaurantImage = findRestaurant.getRestaurantImage();
        if (restaurantImage == null) {
            //이미지 등록
            if (multipartFile != null && !multipartFile.isEmpty()) {
                RestaurantImage image = s3Uploader.uploadFiles(multipartFile, dir);
                image.setRestaurant(findRestaurant);
                restaurantImageRepository.save(image);
            }
        } else {

            //이미지 교체
            if (multipartFile != null && !multipartFile.isEmpty()) {
                restaurantImageRepository.delete(restaurantImage); //기존 이미지 삭제

                em.flush(); //강제 플러시 시켜서 외래키 중복 에러 제거

                //교체한 이미지 저장
                RestaurantImage updateImage = s3Uploader.updateFile(restaurantImage.getStoredFileName(), multipartFile);
                updateImage.setRestaurant(findRestaurant);
                restaurantImageRepository.save(updateImage); // 새로운 이미지 등록
            }
            //이미지 삭제
            else {
                s3Uploader.deleteS3(restaurantImage.getStoredFileName());
                restaurantImage.setRestaurant(null);
                restaurantImageRepository.delete(restaurantImage);
            }
        }

        //식당 업데이트
        findRestaurant.update(restaurantUpdateDto, address, startTime, endTime);
    }

    public void delete(Long id) {
        Restaurant findRestaurant= findOne(id);
        RestaurantImage restaurantImage = findRestaurant.getRestaurantImage();
        if(restaurantImage != null)
            s3Uploader.deleteS3(restaurantImage.getStoredFileName());

        restaurantRepository.deleteById(id);
    }

    public Page<Restaurant> search(AddressSearchDto addressSearchDto, Pageable pageable) {
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

    public List<String> findAddress1() {
        return addressRepository.address1List();
    }

    public Page<Restaurant> getRestaurantList(Pageable pageable) {
        Page<Restaurant> result = restaurantRepository.findAll(pageable);
        return result;
    }

    public Page<Restaurant> getRestaurantList(Pageable pageable, List<Address> addresses) {
        return restaurantRepository.findByAddressIn(addresses, pageable);
    }
}
