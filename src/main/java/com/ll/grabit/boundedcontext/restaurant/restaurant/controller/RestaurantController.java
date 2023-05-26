package com.ll.grabit.boundedcontext.restaurant.restaurant.controller;

import com.ll.grabit.boundedcontext.restaurant.restaurant.dto.RestaurantRegisterDto;

import com.ll.grabit.boundedcontext.restaurant.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
}
