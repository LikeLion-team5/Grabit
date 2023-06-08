package com.ll.grabit.boundedcontext.reservation.repository;

import com.ll.grabit.boundedcontext.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
