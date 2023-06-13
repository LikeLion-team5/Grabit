package com.ll.grabit.boundedcontext.reservation.service;

import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.member.repository.MemberRepository;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationRequestDto;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationResponseDto;
import com.ll.grabit.boundedcontext.reservation.entity.Reservation;
import com.ll.grabit.boundedcontext.reservation.repository.ReservationRepository;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.repository.RestaurantRepository;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository; // 추가된 코드
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, RestaurantRepository restaurantRepository, RestaurantService restaurantService) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurantService = restaurantService;
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
            ReservationResponseDto reservationDto = new ReservationResponseDto();
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
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found with ID: " + id));

        reservation.cancelReservation();
        reservationRepository.save(reservation);
    }

    public Reservation findByIdElseThrow(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow();
    }
}
