package com.ll.grabit.boundedcontext.reservation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    @Future
    @NotNull
    private LocalDate date;
    @NotNull
    private LocalTime reservationTime;
    @NotNull
    private int partySize;
    @NotNull
    private Long memberId;
    @NotNull
    private Long restaurantId;

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getRestaurantId() {
        return restaurantId;
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

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}