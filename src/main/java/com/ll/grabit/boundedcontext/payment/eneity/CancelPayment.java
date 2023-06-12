package com.ll.grabit.boundedcontext.payment.eneity;

import com.ll.grabit.boundedcontext.member.entity.Member;
import jakarta.persistence.*;

@Entity

public class CancelPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long cancelPaymentId;

    private String orderId;
    private String paymentKey;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
