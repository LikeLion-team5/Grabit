package com.ll.grabit.boundedcontext.reservation.controller;

import com.ll.grabit.base.rq.Rq;
import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.member.repository.MemberRepository;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationRequestDto;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationResponseDto;
import com.ll.grabit.boundedcontext.reservation.service.ReservationService;
import com.ll.grabit.boundedcontext.restaurant.repository.RestaurantRepository;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import com.ll.grabit.boundedcontext.review.service.ReviewService;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final Rq rq;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    public ReservationController(ReservationService reservationService, Rq rq, MemberRepository memberRepository, RestaurantRepository restaurantRepository, RestaurantService restaurantService, ReviewService reviewService) {
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
    public String showReservationCheckPage(Model model, @RequestParam(required = false) String sort) {
        if (rq.isLogin()) {
            String loggedInUserId = rq.getMember().getUsername();
            Member loggedInMember = memberRepository.findByUsername(loggedInUserId).orElse(null);
            if (loggedInMember != null) {
                List<ReservationResponseDto> reservationList = reservationService.getReservationsByMemberId(loggedInMember.getId());

                // 선택한 정렬 옵션에 따라 목록을 정렬
                if ("asc".equals(sort)) {
                    // 예약 날짜 오름차순
                    reservationList.sort(Comparator.comparing(ReservationResponseDto::getDate).thenComparing(ReservationResponseDto::getReservationTime));
                } else if ("desc".equals(sort)) {
                    // 예약 날짜 내림차순
                    reservationList.sort(Comparator.comparing(ReservationResponseDto::getDate).thenComparing(ReservationResponseDto::getReservationTime).reversed());
                } else {
                    // 기본 정렬: 현재 시간과 가장 가까운 예약부터 보여주되, 이미 지난 예약은 뒤로 미룸
                    LocalDateTime now = LocalDateTime.now();
                    reservationList.sort((r1, r2) -> {
                        LocalDateTime d1 = r1.getDate().atTime(r1.getReservationTime());
                        LocalDateTime d2 = r2.getDate().atTime(r2.getReservationTime());

                        boolean isR1Past = d1.isBefore(now);
                        boolean isR2Past = d2.isBefore(now);

                        if (isR1Past && !isR2Past) {
                            return 1; // r1이 과거이고 r2가 아니면, r1을 뒤로 보냄
                        } else if (!isR1Past && isR2Past) {
                            return -1; // r1이 과거가 아니고 r2가 과거면, r2를 뒤로 보냄
                        } else {
                            // 두 예약이 모두 과거이거나 모두 과거가 아니면, 시간이 더 가까운 것을 앞으로 보냄
                            return d1.compareTo(d2);
                        }
                    });
                }

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
