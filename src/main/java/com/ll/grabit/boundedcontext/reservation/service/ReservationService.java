package com.ll.grabit.boundedcontext.reservation.service;

import com.ll.grabit.boundedcontext.reservation.dto.ReservationRequestDto;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationResponseDto;
import com.ll.grabit.boundedcontext.reservation.entity.Reservation;
import com.ll.grabit.boundedcontext.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Long createReservation(ReservationRequestDto reservationDto) {
        Reservation reservation = new Reservation();
        reservation.setName(reservationDto.getName());
        reservation.setPhone(reservationDto.getPhone());
        reservation.setDate(reservationDto.getDate());
        reservation.setTime(reservationDto.getTime());
        reservation.setPartySize(reservationDto.getPartySize());

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
        reservationDto.setTime(reservation.getTime());
        reservationDto.setPartySize(reservation.getPartySize());

        return reservationDto;
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found with ID: " + id));

        reservationRepository.delete(reservation);
    }
}
