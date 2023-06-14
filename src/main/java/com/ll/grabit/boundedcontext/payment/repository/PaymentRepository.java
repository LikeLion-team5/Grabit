package com.ll.grabit.boundedcontext.payment.repository;

import com.ll.grabit.boundedcontext.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
