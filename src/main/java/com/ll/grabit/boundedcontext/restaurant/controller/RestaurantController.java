package com.ll.grabit.boundedcontext.restaurant.controller;

import com.ll.grabit.boundedcontext.address.dto.AddressSearchDto;
import com.ll.grabit.boundedcontext.address.entity.Address;
import com.ll.grabit.boundedcontext.address.service.AddressService;
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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    @GetMapping("/register")
    public String restaurantRegister(Model model) {
        model.addAttribute("restaurantRegisterDto", new RestaurantRegisterDto());
        List<String> address1List = restaurantService.findAddress1();
        model.addAttribute("address1List", address1List);
        return "/tmp/tmp_reg_design";
    }

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
        Address address = null;
        if (restaurantRegisterDto.getAddress1() != "" && restaurantRegisterDto.getAddress2() != "" && restaurantRegisterDto.getAddress3().isEmpty()) {
            restaurantRegisterDto.setAddress3("");
            Optional<Address> findAddress = restaurantService.findAddress(restaurantRegisterDto);
            if (!findAddress.isPresent()) {
                errors.put("addressGlobalError", "올바른 형식의 주소를 입력해주세요.");
                return new ResponseEntity<>(errors, HttpStatusCode.valueOf(HTTPResponse.SC_BAD_REQUEST));
            }
            address = findAddress.get();
        }
        restaurantService.save(restaurantRegisterDto, address, file);

        return new ResponseEntity<>("등록 성공", HttpStatusCode.valueOf(HTTPResponse.SC_OK));
    }

    @GetMapping("/{restaurantId}/edit")
    public String update(@PathVariable("restaurantId") Long id, RestaurantUpdateDto restaurantUpdateDto,
                         Model model) {
        model.addAttribute("restaurantRegisterDto", new RestaurantRegisterDto());
        return "registerForm";
    }

    @PostMapping("/{restaurantId}/edit")
    public String update(@PathVariable("restaurantId") Long id, @ModelAttribute @Valid RestaurantUpdateDto restaurantUpdateDto,
                         BindingResult bindingResult,
                         MultipartFile file) throws IOException {
        if (bindingResult.hasErrors()) {
            return "registerUpdateForm";
        }
        restaurantService.update(id, restaurantUpdateDto, file);
        return "home";
    }

    @PostMapping("/{restaurantId}/delete")
    public String delete(@PathVariable("restaurantId") Long id){
        restaurantService.delete(id);
        return "/usr/home/main";
    }


    //메인 페이지(Ajax 이용할 예정이라, 추후 수정 예정)
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

        return "/usr/home/main";

    }

//    //Ajax 적용 시, 메인 페이지
//    @GetMapping("/search/main")
//    public String searchMainPage(@PageableDefault(page = 0, size = 8, sort = "restaurantId", direction = Sort.Direction.ASC) Pageable pageable,
//                         Model model){
//        //대주소 리스트
//        List<String> address1List = addressService.getAddress1List();
//        model.addAttribute("address1List", address1List);
//
//        //식당 리스트
//        Page<Restaurant> restaurantList = restaurantService.getRestaurantList(pageable);
//        model.addAttribute("restaurants", restaurantList);
//
//        return "/tmp/tmp_list";
//    }
//
//    //Ajax 요청 시 결과
//    @GetMapping("/search/ajax")
//    @ResponseBody
//    public ResponseEntity<?> searchAjax(
//            AddressSearchDto addressSearchDto,
//            @PageableDefault(page = 0, size = 8, sort = "restaurantId", direction = Sort.Direction.ASC) Pageable pageable,
//                                 Model model){
//        //주소값 필터링
//        List<Address> addressList = null;
//        if(!addressSearchDto.getAddress1().isEmpty()){
//            addressList = addressService.getAddressList(addressSearchDto);
//        }else{
//            return new ResponseEntity<>("주소를 입력 해 주세요.", HttpStatusCode.valueOf(HTTPResponse.SC_BAD_REQUEST));
//        }
//
//        //식당 리스트
//        Page<Restaurant> restaurantList = restaurantService.getRestaurantList(pageable, addressList);
//        model.addAttribute("maxPage", 10);
//
//        return new ResponseEntity<>(restaurantList, HttpStatusCode.valueOf(HTTPResponse.SC_OK));
//    }

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
            for(int i = openTime; i <= 24; i++)
                reservationTimeList.add(i+":00");

            for(int i = 1; i < closeTime; i++)
                reservationTimeList.add(i+":00");
        }

        return reservationTimeList;
    }

}
