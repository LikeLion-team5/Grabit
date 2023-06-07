package com.ll.grabit.boundedcontext.payment.eneity;

import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private String status;

    private Integer reservationPrice;

    private Integer numberOfPeople;

    private LocalDate 방문날짜;
    private LocalTime 방문시간;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;



}
