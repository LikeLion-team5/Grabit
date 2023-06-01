package com.ll.grabit.boundedcontext.restaurant.controller;


import com.ll.grabit.boundedcontext.address.dto.AddressSearchDto;
import com.ll.grabit.boundedcontext.menu.dto.MenuRegisterDto;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantRegisterDto;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantUpdateDto;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/register")
    public String restaurantRegister(RestaurantRegisterDto restaurantRegisterDto,
                                     List<MenuRegisterDto> menuRegisterDtoList,
                                     Model model) {
        model.addAttribute("restaurantRegisterDto", new RestaurantRegisterDto());
        return "registerForm";
    }

    @PostMapping("/register")
    public String restaurantRegister(@ModelAttribute @Valid RestaurantRegisterDto restaurantRegisterDto,
                                     BindingResult result,
                                     MultipartFile file) throws IOException {
        if(result.hasErrors()){
            return "registerForm";
        }
        System.out.println(file.isEmpty());
        restaurantService.save(restaurantRegisterDto, file);
        return "home";
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

    @GetMapping("/search")
    public String search(@ModelAttribute AddressSearchDto addressSearchDto,
                         @PageableDefault(page = 0, size = 8, sort = "restaurantId", direction = Sort.Direction.ASC) Pageable pageable,
                                   Model model){
        Page<Restaurant> restaurantList = restaurantService.search(addressSearchDto, pageable);
        model.addAttribute("restaurantList", restaurantList);

        return "home";
    }

}
