package com.ll.grabit.boundedcontext.reservation.service;

import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.member.repository.MemberRepository;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationRequestDto;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationResponseDto;
import com.ll.grabit.boundedcontext.reservation.entity.Reservation;
import com.ll.grabit.boundedcontext.reservation.repository.ReservationRepository;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.repository.RestaurantRepository;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import com.ll.grabit.boundedcontext.review.entity.Review;
import com.ll.grabit.boundedcontext.review.repository.ReviewRepository;
import com.ll.grabit.boundedcontext.review.service.ReviewService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository; // 추가된 코드
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, RestaurantRepository restaurantRepository, RestaurantService restaurantService, ReviewService reviewService, ReviewRepository reviewRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
    }

    public Long createReservation(ReservationRequestDto reservationDto) {
        Member member = memberRepository.findById(reservationDto.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("Member not found with ID: " + reservationDto.getMemberId()));

        Restaurant restaurant = restaurantRepository.findById(reservationDto.getRestaurantId())
                .orElseThrow(() -> new NoSuchElementException("Restaurant not found with ID: " + reservationDto.getRestaurantId()));

        Reservation reservation = new Reservation();
        reservation.setName(reservationDto.getName());
        reservation.setPhone(reservationDto.getPhone());
        reservation.setDate(reservationDto.getDate());
        reservation.setReservationTime(reservationDto.getReservationTime());
        reservation.setPartySize(reservationDto.getPartySize());
        reservation.setMember(member);
        reservation.setRestaurant(restaurant);
        reservation.setRestaurantName(restaurant.getRestaurantName());
        reservation.setStatus("PENDING");

        Reservation savedReservation = reservationRepository.save(reservation);
        return savedReservation.getReservationId();
    }

    public ReservationResponseDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found with ID: " + id));

        ReservationResponseDto reservationDto = new ReservationResponseDto();
        reservationDto.setReservationId(reservation.getReservationId());
        reservationDto.setName(reservation.getName());
        reservationDto.setPhone(reservation.getPhone());
        reservationDto.setDate(reservation.getDate());
        reservationDto.setReservationTime(reservation.getReservationTime());
        reservationDto.setPartySize(reservation.getPartySize());
        reservationDto.setMemberId(reservation.getMember().getId());

        if (reservation.getStatus().equals("CONFIRMED")) {
            reservationDto.setStatus("확정");
        } else if (reservation.getStatus().equals("CANCELLED")) {
            reservationDto.setStatus("취소");
        } else {
            reservationDto.setStatus("대기");
        }

        return reservationDto;
    }

    public List<ReservationResponseDto> getReservationsByMemberId(Long member_id) {
        List<Reservation> reservations = reservationRepository.findAllByMemberId(member_id);

        reservations.sort(Comparator.comparing(Reservation::getDate).thenComparing(Reservation::getReservationTime));

        List<ReservationResponseDto> reservationDtos = new ArrayList<>();

        for (Reservation reservation : reservations) {
            System.out.println("예약 : " + reservation);
            ReservationResponseDto reservationDto = new ReservationResponseDto();
            Boolean hasReview = reviewRepository.findByReservationReservationId(reservation.getReservationId()).isPresent();
            reservationDto.setHasReview(hasReview);
            reservationDto.setReservationId(reservation.getReservationId());
            reservationDto.setName(reservation.getName());
            reservationDto.setPhone(reservation.getPhone());
            reservationDto.setDate(reservation.getDate());
            reservationDto.setReservationTime(reservation.getReservationTime());
            reservationDto.setPartySize(reservation.getPartySize());
            reservationDto.setRestaurantName(reservation.getRestaurantName());

            if (reservation.getStatus().equals("CONFIRMED")) {
                reservationDto.setStatus("확정");
            } else if (reservation.getStatus().equals("CANCELLED")) {
                reservationDto.setStatus("취소");
            } else if (reservation.getStatus().equals("COMPLETED")) {
                reservationDto.setStatus("방문 완료");
            } else {
                reservationDto.setStatus("대기");
            }

            reservationDtos.add(reservationDto);
        }
        return reservationDtos;
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found with ID: " + id));

        reservationRepository.delete(reservation);
    }

    public void confirmReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found with ID: " + id));

        if (reservation.getStatus().equals("PENDING")) {
            // 예약 확정
            reservation.setStatus("CONFIRMED");
            reservationRepository.save(reservation);
        } else {
            throw new IllegalStateException("Reservation cannot be confirmed.");
        }
    }

    public void completeReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found with ID: " + id));

        if (!reservation.getStatus().equals("CONFIRMED")) {
            throw new IllegalStateException("Reservation cannot be completed.");
        }

        reservation.setStatus("COMPLETED");
        reservationRepository.save(reservation);
    }

    public void cancelReservation(Long id) {
        Reservation reservation = findReservation(id);
        reservation.cancelReservation();
        reservationRepository.save(reservation);
    }


    public Reservation findReservation(Long reservationId){
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found with ID: " + reservationId));
    }


    public Reservation findByIdElseThrow(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow();
    }

    @Scheduled(fixedRate = 60000) // 1분에 한 번씩 실행
    public void autoUpdateReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        for (Reservation reservation : reservations) {
            LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getDate(), reservation.getReservationTime());
            LocalDateTime now = LocalDateTime.now();

            // 예약 시간 한 시간 전에 "확정"으로 상태 변경
            if (now.isAfter(reservationDateTime.minusHours(1)) && reservation.getStatus().equals("PENDING")) {
                reservation.setStatus("CONFIRMED");
                reservationRepository.save(reservation);
            }

            // 예약 시간이 지나면 "방문 완료"로 상태 변경
            if (now.isAfter(reservationDateTime) && reservation.getStatus().equals("CONFIRMED")) {
                reservation.setStatus("COMPLETED");
                reservationRepository.save(reservation);
            }
        }
    }

}

