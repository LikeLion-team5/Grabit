package com.ll.grabit.boundedcontext.restaurant.controller;


import com.ll.grabit.boundedcontext.restaurant.dto.AddressSearchDto;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantUpdateDto;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.dto.RestaurantRegisterDto;

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

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/register")
    public String restaurantRegister(Model model) {
        model.addAttribute("restaurantRegisterDto", new RestaurantRegisterDto());
        return "registerForm";
    }

    @PostMapping("/register")
    public String restaurantRegister(@ModelAttribute @Valid RestaurantRegisterDto restaurantRegisterDto,
                                     BindingResult result) {
        if(result.hasErrors()){
            return "registerForm";
        }
        restaurantService.save(restaurantRegisterDto);
        return "home";
    }

    @PostMapping("/{restaurantId}/edit")
    public String update(@PathVariable("restaurantId") Long id, @ModelAttribute @Valid RestaurantUpdateDto restaurantUpdateDto){
        restaurantService.update(id, restaurantUpdateDto);
        return "home";
    }

    @PostMapping("/{restaurantId}/delete")
    public String delete(@PathVariable("restaurantId") Long id){
        restaurantService.delete(id);
        return "home";
    }

    @GetMapping("/search")
    @ResponseBody
    public Page<Restaurant> search(@ModelAttribute AddressSearchDto addressSearchDto,
                         @PageableDefault(page = 0, size = 8, sort = "restaurantId", direction = Sort.Direction.ASC) Pageable pageable){
        Page<Restaurant> restaurantList = restaurantService.search(addressSearchDto, pageable);

        return restaurantList;
    }

}
