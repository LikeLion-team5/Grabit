package com.ll.grabit.boundedcontext.reservation.service;

import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.member.repository.MemberRepository;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationRequestDto;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationResponseDto;
import com.ll.grabit.boundedcontext.reservation.entity.Reservation;
import com.ll.grabit.boundedcontext.reservation.repository.ReservationRepository;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository; // 추가된 코드
    private final RestaurantRepository restaurantRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, RestaurantRepository restaurantRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.restaurantRepository = restaurantRepository;
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

        return reservationDto;
    }

    public List<ReservationResponseDto> getReservationsByMemberId(Long member_id) {
        List<Reservation> reservations = reservationRepository.findAllByMemberId(member_id);
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

            reservationDtos.add(reservationDto);
        }
        return reservationDtos;
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found with ID: " + id));

        reservationRepository.delete(reservation);
    }

}
