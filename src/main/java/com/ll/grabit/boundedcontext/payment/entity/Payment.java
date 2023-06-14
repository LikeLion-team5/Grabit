package com.ll.grabit.boundedcontext.payment.entity;

import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private String paymentKey;
    private String orderId;

    private Integer amount;

    private LocalDateTime payedDate;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        reservation.setPayment(this);
    }
}

