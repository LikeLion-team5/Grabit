package com.ll.grabit.boundedcontext.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationResponseDto {
    private Long reservationId;
    private String name;
    private String phone;
    private LocalDate date;
    private LocalTime reservationTime;
    private int partySize;
    private Long memberId;
    private Long restaurantId;
    private String restaurantName;
    private String status;

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

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public int getPartySize() {
        return partySize;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getStatus() { return status; }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
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

    public void setStatus(String status) {
        this.status = status;
    }
}
