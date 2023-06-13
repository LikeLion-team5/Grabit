package com.ll.grabit.boundedcontext.reservation.repository;

import com.ll.grabit.boundedcontext.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByMemberId(Long memberId);

    List<Reservation> findAllByMemberIdOrderByDateDesc(Long member_id);

    Optional<Reservation> findByReservationId(Long reservationId);
}
