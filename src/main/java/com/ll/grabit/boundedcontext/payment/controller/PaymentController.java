package com.ll.grabit.boundedcontext.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.grabit.base.rq.Rq;
import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.payment.exception.AmountNotMatchedException;
import com.ll.grabit.boundedcontext.payment.service.PaymentService;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import com.ll.grabit.boundedcontext.restaurant.service.RestaurantService;
import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final RestaurantService restaurantService;
    private final PaymentService paymentService;
    private final Rq rq;

    private final String SECRET_KEY = "test_sk_7XZYkKL4Mrjg6q2bvqm80zJwlEWR";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    @GetMapping("{restaurantId}/payment")
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

    @GetMapping("{restaurantId}/success")
    public String success(
            @PathVariable Long restaurantId,
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Integer amount,
            Model model) throws JsonProcessingException {

        if (amount < 0 || amount != 10000) {
            throw new AmountNotMatchedException();
        }


        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);


        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Member member = rq.getMember();
            paymentService.save(member, paymentKey, orderId, amount);

            return "toss/success";
        } else {
            //결제 실패
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "toss/fail";
        }
    }

    @GetMapping("{restaurantId}/fail")
    public String failPayment(@PathVariable Long restaurantId, @RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "toss/fail";
    }


    @PostMapping("/cancel")
    @ResponseBody
    public ResponseEntity<String> requestPaymentCancel(@RequestParam String paymentKey,
                                       @RequestParam String cancelReason){
        RestTemplate restTemplate1 = new RestTemplate();

        URI uri = URI.create("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel");
        HttpHeaders headers = new HttpHeaders();
//        byte[] secretKeyByte = (SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8);
//        headers.setBasicAuth(new String(Base64.getEncoder().encode(secretKeyByte)));
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);


        JSONObject param = new JSONObject();
        param.put("cancelReason", cancelReason);

        String response = restTemplate1.postForObject(uri, new HttpEntity<>(param, headers), String.class);
        return ResponseEntity.ok(response);

    }
}
