package com.ll.grabit.boundedcontext.restaurant.controller;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.address.dto.AddressSearchDto;
import com.ll.grabit.boundedcontext.address.entity.Address;
import com.ll.grabit.boundedcontext.menu.dto.MenuRegisterDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/register")
    public String restaurantRegister(Model model) {
        model.addAttribute("restaurantRegisterDto", new RestaurantRegisterDto());
        List<String> address1List = restaurantService.findAddress1();
        model.addAttribute("address1List", address1List);
        return "tmp_reg_design";
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
        return "home";
    }


    //메인 페이지(Ajax 이용할 예정이라, 추후 수정 예정)
    @GetMapping("/search")
    public String search(@ModelAttribute AddressSearchDto addressSearchDto,
                         @PageableDefault(page = 0, size = 8, sort = "restaurantId", direction = Sort.Direction.ASC) Pageable pageable,
                                   Model model){
        Page<Restaurant> restaurantList = restaurantService.search(addressSearchDto, pageable);
        model.addAttribute("restaurantList", restaurantList);

        return "home";
    }

    //식당 클릭 시, 식당 1건 조회
    @GetMapping("/{restaurantId}")
    public String searchOne(@PathVariable Long restaurantId, Model model){
        Restaurant findRestaurant = restaurantService.findOne(restaurantId);
        model.addAttribute("restaurant", findRestaurant);

        return "식당 상세보기 페이지";
    }


}
