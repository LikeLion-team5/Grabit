package com.ll.grabit.boundedcontext.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    @GetMapping("/check")
    public String showReservation() {

        return "usr/reservation/check";
    }
}