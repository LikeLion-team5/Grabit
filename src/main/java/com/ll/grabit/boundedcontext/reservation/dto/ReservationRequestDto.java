package com.ll.grabit.boundedcontext.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationRequestDto {
    private String name;
    private String phone;
    private LocalDate date;
    private LocalTime time;
    private int partySize;

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

    // Setter 메서드
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