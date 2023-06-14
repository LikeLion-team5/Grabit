package com.ll.grabit.boundedcontext.payment.service;

import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.payment.entity.Payment;
import com.ll.grabit.boundedcontext.payment.repository.PaymentRepository;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationRequestDto;
import com.ll.grabit.boundedcontext.reservation.entity.Reservation;
import com.ll.grabit.boundedcontext.reservation.repository.ReservationRepository;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final String SECRET_KEY = "test_sk_7XZYkKL4Mrjg6q2bvqm80zJwlEWR";


    public void save(Member loginMember, String paymentKey, String orderId, Integer amount, ReservationRequestDto reservationRequestDto) {


        Reservation reservation = saveReservation(loginMember, reservationRequestDto);

        Payment payment = new Payment();
        payment.setPaymentKey(paymentKey);
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setPayedDate(LocalDateTime.now());
        payment.setMember(loginMember);
        payment.setReservation(reservation);

        paymentRepository.save(payment);
    }
    public Reservation saveReservation(Member member, ReservationRequestDto reservationDto) {
        Restaurant restaurant = restaurantRepository.findById(reservationDto.getRestaurantId())
                .orElseThrow(() -> new NoSuchElementException("Restaurant not found with ID: " + reservationDto.getRestaurantId()));

        Reservation reservation = new Reservation();
        reservation.setName(reservationDto.getName());
        reservation.setPhone(reservationDto.getPhone());
        reservation.setDate(reservationDto.getDate());
        reservation.setReservationTime(reservationDto.getReservationTime());
        reservation.setPartySize(reservationDto.getPartySize());
        reservation.setMember(member);
        reservation.setRestaurant(restaurant);
        reservation.setRestaurantName(restaurant.getRestaurantName());
        reservation.setStatus("PENDING");

        return reservationRepository.save(reservation);
    }

    public String paymentCancel(Payment payment){
        String paymentKey = payment.getPaymentKey();
        String cancelReason = "개인적인 사유";

        RestTemplate restTemplate = new RestTemplate();

        URI uri = URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);


        JSONObject param = new JSONObject();
        param.put("cancelReason", cancelReason);

        String response = restTemplate.postForObject(uri, new HttpEntity<>(param, headers), String.class);

        return response;
    }
}
