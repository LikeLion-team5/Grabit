package com.ll.grabit.boundedcontext.payment.service;

import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.payment.eneity.Payment;
import com.ll.grabit.boundedcontext.payment.repository.PaymentRepository;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;



    public void save(Member member, String paymentKey, String orderId, Integer amount) {
        Payment payment = new Payment();
        payment.setPaymentKey(paymentKey);
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setPayedDate(LocalDateTime.now());

        payment.setMember(member);

        paymentRepository.save(payment);
    }
}
