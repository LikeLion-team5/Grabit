package com.ll.grabit.boundedcontext.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationResponseDto {
    private Long reservationId;
    private String name;
    private String phone;
    private LocalDate date;
    private LocalTime time;
    private int partySize;

    public Long getReservationId() {
        return reservationId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }



}
