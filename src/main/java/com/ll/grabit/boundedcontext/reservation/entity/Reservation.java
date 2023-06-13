package com.ll.grabit.boundedcontext.reservation.entity;

import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.payment.eneity.Payment;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;

import com.ll.grabit.boundedcontext.review.entity.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToOne(fetch = LAZY)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(mappedBy = "reservation")
    private Payment payment;

    private String name;

    private String phone;

    private LocalDate date;

    private LocalTime reservationTime;

    private int partySize;

    private String restaurantName;

    private String status;

    @Builder
    public Reservation(String name, String phone, LocalDate date, LocalTime reservationTime, int partySize, String status) {
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.reservationTime = reservationTime;
        this.partySize = partySize;
        this.status = status;
    }
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }


    public String getName() {
        return name;
    }

    public String getRestaurantName() {
        return this.restaurantName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        restaurant.getReservations().add(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getReservations().add(this);
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void cancelReservation() {
        if (status.equals("PENDING")) {
            status = "CANCELLED";
        } else {
            throw new IllegalStateException("Reservation cannot be cancelled.");
        }
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

}