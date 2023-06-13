package com.ll.grabit.boundedcontext.restaurant.controller;

import com.ll.grabit.boundedcontext.address.dto.AddressSearchDto;
import com.ll.grabit.boundedcontext.address.entity.Address;
import com.ll.grabit.boundedcontext.address.service.AddressService;
import com.ll.grabit.boundedcontext.menu.entity.Menu;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;

import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantRegisterDto;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantUpdateDto;

import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;


@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    private final AddressService addressService;

    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    @GetMapping("/register")
    public String restaurantRegister(Model model) {
        model.addAttribute("restaurantRegisterDto", new RestaurantRegisterDto());
        List<String> address1List = restaurantService.findAddress1();
        model.addAttribute("address1List", address1List);
        return "usr/restaurant/restaurant_register";
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> restaurantRegister(@ModelAttribute @Valid RestaurantRegisterDto restaurantRegisterDto,
                                                BindingResult result,
                                                @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Map<String, String> errors = new HashMap<>();
        //입력 필드 검증
        if (result.hasErrors()) {
            result.getFieldErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });

            return new ResponseEntity<>(errors, HttpStatusCode.valueOf(HTTPResponse.SC_BAD_REQUEST));
        }

        //주소123 을 가지고 Address 조회
        String address1 = restaurantRegisterDto.getAddress1();
        String address2 = restaurantRegisterDto.getAddress2();
        String address3 = restaurantRegisterDto.getAddress3();
        if(address3.isEmpty() || address3 == null)
            address3 = "";

        //주소123 을 가지고 Address 조회
        Address address = null;
        Optional<Address> findAddress = restaurantService.findAddress(address1,address2,address3);
        if (!findAddress.isPresent()) {
            errors.put("addressGlobalError", "올바른 형식의 주소를 입력해주세요.");
            return new ResponseEntity<>(errors, HttpStatusCode.valueOf(HTTPResponse.SC_BAD_REQUEST));
        }
        address = findAddress.get();
        restaurantService.save(restaurantRegisterDto, address, file);

        return new ResponseEntity<>("등록 성공", HttpStatusCode.valueOf(HTTPResponse.SC_OK));
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    @GetMapping("/{restaurantId}/edit")
    public String update(@PathVariable("restaurantId") Long id, RestaurantUpdateDto restaurantUpdateDto,
                         Model model) {
        //데이터 조회
        Restaurant findRestaurant = restaurantService.findOne(id);

        //데이터 세팅
        restaurantUpdateDto.setRestaurantName(findRestaurant.getRestaurantName());
        restaurantUpdateDto.setDescription(findRestaurant.getDescription());
        restaurantUpdateDto.setType(findRestaurant.getType().toString());
        restaurantUpdateDto.setAddress1(findRestaurant.getAddress().getAddress1());
        restaurantUpdateDto.setAddress2(findRestaurant.getAddress().getAddress2());
        restaurantUpdateDto.setAddress3(findRestaurant.getAddress().getAddress3());
        restaurantUpdateDto.setDetail_address(findRestaurant.getDetail_address());
        restaurantUpdateDto.setStartTime(String.valueOf(findRestaurant.getOpeningTime().getHour()));
        restaurantUpdateDto.setEndTime(String.valueOf(findRestaurant.getClosingTime().getHour()));
        restaurantUpdateDto.setPerTimeMaxReservationCount(findRestaurant.getPerTimeMaxReservationCount());

        //주소 데이터 세팅
        List<String> address1List = restaurantService.findAddress1();
        model.addAttribute("address1List", address1List);

        //메뉴 데이터
        List<Menu> menuList = findRestaurant.getMenuList();
        model.addAttribute("menuList", menuList);

        //식당 이미지 데이터
        model.addAttribute("image", findRestaurant.getRestaurantImage());

        model.addAttribute("restaurantId", id);


        return "usr/restaurant/restaurant_update";
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    @PostMapping("/{restaurantId}/edit")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable("restaurantId") Long id,
                                    @ModelAttribute @Valid RestaurantUpdateDto restaurantUpdateDto,
                                    BindingResult result,
                                    MultipartFile file) throws IOException {
        Map<String, String> errors = new HashMap<>();
        //입력 필드 검증
        if (result.hasErrors()) {
            result.getFieldErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });

            return new ResponseEntity<>(errors, HttpStatusCode.valueOf(HTTPResponse.SC_BAD_REQUEST));
        }

        //주소123 을 가지고 Address 조회
        String address1 = restaurantUpdateDto.getAddress1();
        String address2 = restaurantUpdateDto.getAddress2();
        String address3 = restaurantUpdateDto.getAddress3();
        if (address3.isEmpty() || address3 == null)
            address3 = "";

        //주소123 을 가지고 Address 조회
        Address address = null;
        Optional<Address> findAddress = restaurantService.findAddress(address1, address2, address3);
        if (!findAddress.isPresent()) {
            errors.put("addressGlobalError", "올바른 형식의 주소를 입력해주세요.");
            return new ResponseEntity<>(errors, HttpStatusCode.valueOf(HTTPResponse.SC_BAD_REQUEST));
        }
        address = findAddress.get();

        restaurantService.update(id, restaurantUpdateDto, file);


        return new ResponseEntity<>("등록 성공", HttpStatusCode.valueOf(HTTPResponse.SC_OK));

    }

    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    @PostMapping("/{restaurantId}/image/delete")
    @ResponseBody
    public ResponseEntity<?> deletImage(@PathVariable("restaurantId") Long id) {
        restaurantService.deleteImage(id);
        return new ResponseEntity<>("삭제 성공", HttpStatusCode.valueOf(HTTPResponse.SC_OK));
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('admin')")
    @PostMapping("/{restaurantId}/delete")
    public String delete(@PathVariable("restaurantId") Long id){
        restaurantService.delete(id);
        return "redirect:/";
    }


    //메인 페이지
    @GetMapping("/search")
    public String search(@ModelAttribute AddressSearchDto addressSearchDto,
                         @PageableDefault(page = 0, size = 8, sort = "restaurantId", direction = Sort.Direction.ASC) Pageable pageable,
                                   Model model){
        //대주소 리스트
        List<String> address1List = addressService.getAddress1List();
        model.addAttribute("address1List", address1List);

        Page<Restaurant> restaurantList = restaurantService.search(addressSearchDto, pageable);
        model.addAttribute("restaurantList", restaurantList);

        model.addAttribute("maxPage", 10);

        return "usr/home/main";

    }



    //식당 클릭 시, 식당 1건 조회
    @GetMapping("/restaurantInfo/{restaurantId}")
    public String searchOne(@PathVariable Long restaurantId, Model model){
        //식당 정보
        Restaurant findRestaurant = restaurantService.findOne(restaurantId);
        model.addAttribute("restaurant", findRestaurant);

        //예약 가능 시간
        int openTime = findRestaurant.getOpeningTime().getHour();
        int closeTime = findRestaurant.getClosingTime().getHour();
        List<String> reservationTimeList = getReservationTimeList(openTime, closeTime);
        model.addAttribute("reservationTimeList", reservationTimeList);

        return "/usr/restaurant/restaurantInfo";
    }

    @GetMapping("/restaurantInfo")
    public String showRestaurantInfo() {
        return "usr/restaurant/restaurantInfo";
    }

    private List<String> getReservationTimeList(int openTime, int closeTime){
        List<String> reservationTimeList = new ArrayList<>();
        if(openTime < closeTime){
            //09:00 ~ 21:00
            for(int i = openTime; i < closeTime; i++)
                reservationTimeList.add(i+":00");
        }else{
            //21:00 ~ 05:00
            for(int i = 0; i < closeTime; i++)
                reservationTimeList.add(String.format("%02d:00", i));

            for(int i = openTime; i < 24; i++)
                reservationTimeList.add(i+":00");

        }

        return reservationTimeList;
    }

}
