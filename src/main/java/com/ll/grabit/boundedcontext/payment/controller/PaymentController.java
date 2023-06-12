package com.ll.grabit.boundedcontext.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.grabit.base.rq.Rq;
import com.ll.grabit.base.rsdata.RsData;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.payment.exception.AmountNotMatchedException;
import com.ll.grabit.boundedcontext.payment.service.PaymentService;
import com.ll.grabit.boundedcontext.reservation.dto.ReservationRequestDto;
import com.ll.grabit.boundedcontext.reservation.service.ReservationService;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final RestaurantService restaurantService;

    private final ReservationService reservationService;

    private final Rq rq;

    private final PaymentService paymentService;

    private final String SECRET_KEY = "test_sk_7XZYkKL4Mrjg6q2bvqm80zJwlEWR";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    @GetMapping("/{restaurantId}/payment")
    public String paymentPage(@PathVariable Long restaurantId, Model model) {
        Restaurant restaurant = restaurantService.findOne(restaurantId);
        Member member = rq.getMember();
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("member", member);

        return "toss/payment";
    }

    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }

    @GetMapping("/{restaurantId}/pay/success")
    @PreAuthorize("isAuthenticated() and hasAuthority('member')")
    public String success(
            @PathVariable Long restaurantId,
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Integer amount, //여기까지는 필수 정보
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam LocalDate date,
            @RequestParam String reservationTime,
            @RequestParam Integer partySize,
            Model model) throws JsonProcessingException {
        if (amount < 0 || amount != 10000) {
            throw new AmountNotMatchedException();
        }


        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        //페이로드 설정 후, request 생성
        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        //토스 페이먼츠 결제 요청
        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);


        //결제 성공
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Member member = rq.getMember();
            paymentService.save(member, paymentKey, orderId, amount);

            Member loginMember = rq.getMember();
            if (loginMember != null && restaurantId != null) {
                ReservationRequestDto reservationRequestDto = new ReservationRequestDto();
                reservationRequestDto.setName(name);
                reservationRequestDto.setPhone(phone);
                reservationRequestDto.setDate(date);
                reservationRequestDto.setReservationTime(extractedLocalTime(reservationTime));
                reservationRequestDto.setPartySize(partySize);
                reservationRequestDto.setRestaurantId(restaurantId);
                reservationRequestDto.setMemberId(loginMember.getId());
                reservationService.createReservation(reservationRequestDto);
            }

            return "redirect:/payment/success";
        } else {
            //결제 실패
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "toss/fail";
        }
    }

    @GetMapping("/{restaurantId}/pay/fail")
    @PreAuthorize("isAuthenticated() and hasAuthority('member')")
    public String failPayment(@PathVariable Long restaurantId, @RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "toss/fail";
    }

    //결제 성공 시, 결제 성공 알림 페이지
    @PreAuthorize("isAuthenticated() and hasAuthority('member')")
    @GetMapping("/success")
    public String successPage(){
        return "toss/success";
    }




    //결제 취소 API
    @PostMapping("/cancel")
    @PreAuthorize("isAuthenticated() and hasAuthority('member')")
    @ResponseBody
    public ResponseEntity<String> requestPaymentCancel(@RequestParam String paymentKey,
                                       @RequestParam String cancelReason){
        RestTemplate restTemplate1 = new RestTemplate();

        URI uri = URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);


        JSONObject param = new JSONObject();
        param.put("cancelReason", cancelReason);

        String response = restTemplate1.postForObject(uri, new HttpEntity<>(param, headers), String.class);
        return ResponseEntity.ok(response);

    }

    private static LocalTime extractedLocalTime(String time) {
        String[] split = time.split(":");
        LocalTime localTime = LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        return localTime;
    }
}
