package com.ll.grabit.boundedcontext.reservation.controller;

import com.ll.grabit.base.rq.Rq;
import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.reservation.entity.Reservation;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.member.repository.MemberRepository;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationRequestDto;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationResponseDto;
import com.ll.grabit.boundedcontext.reservation.service.ReservationService;
import com.ll.grabit.boundedcontext.restaurant.repository.RestaurantRepository;
import com.ll.grabit.boundedcontext.review.entity.Review;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.userdetails.UserDetails;


@Controller
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final Rq rq;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    public ReservationController(ReservationService reservationService, Rq rq, MemberRepository memberRepository, RestaurantRepository restaurantRepository) {
        this.reservationService = reservationService;
        this.rq = rq;
        this.memberRepository = memberRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/check")
    @ResponseBody
    @Transactional
    public RsData<Long> createReservation(@RequestBody ReservationRequestDto reservationDto) {
        if (rq.isLogin()) {
            String loggedInUserId = rq.getMember().getUsername();
            Member loggedInMember = memberRepository.findByUsername(loggedInUserId).orElse(null);
            Long restaurantId = reservationDto.getRestaurantId();
            String restaurantName = reservationDto.getRestaurantName();
            if (loggedInMember != null && restaurantId != null && restaurantRepository.existsById(restaurantId)) {
                reservationDto.setMemberId(loggedInMember.getId());
                reservationDto.setRestaurantName(restaurantName);
                Long reservationId = reservationService.createReservation(reservationDto);
                return RsData.of("S-1", "예약에 성공하셨습니다.", reservationId);
            }
        }
        return RsData.of("F-1", "예약에 실패하셨습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/check")
    public String showReservationCheckPage(Model model) {
        if (rq.isLogin()) {
            String loggedInUserId = rq.getMember().getUsername();
            Member loggedInMember = memberRepository.findByUsername(loggedInUserId).orElse(null);
            if (loggedInMember != null) {
                List<ReservationResponseDto> reservationList = reservationService.getReservationsByMemberId(loggedInMember.getId());
                model.addAttribute("reservations", reservationList);
            }
        }
        return "usr/reservation/check";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ReservationResponseDto>> getUserReservations(@PathVariable Long id) {
        List<ReservationResponseDto> responseDtoList = reservationService.getReservationsByMemberId(id);
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> completeReservation(@PathVariable Long id) {
        reservationService.completeReservation(id);
        return ResponseEntity.noContent().build();
    }

}
