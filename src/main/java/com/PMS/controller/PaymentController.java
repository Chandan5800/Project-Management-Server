package com.PMS.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PMS.model.PlanType;
import com.PMS.model.User;
import com.PMS.response.PaymentLinkResponse;
import com.PMS.service.UserService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Value("rzp_test_XjOflk1zF6A2EI")
    private String apiKey;

    @Value("qulLVakgpg3eFwEBZaRL4oiS")
    private String apiSecret;

    @Autowired
    private UserService userService;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @PostMapping("/{planType}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable PlanType planType,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        int amount = 0;
        if (planType.equals(PlanType.WEEKLY)) {
            amount = 249 * 100;
        } else if (planType.equals(PlanType.MONTHLY)) {
            amount = 799 * 100;
        } else if (planType.equals(PlanType.ANNUALLY)) {
            amount = 6911 * 100;
        }

        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", amount);
        paymentLinkRequest.put("currency", "INR");

        JSONObject customer = new JSONObject();
        customer.put("name", user.getFullName());
        customer.put("email", user.getEmail());
        paymentLinkRequest.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("email", true);
        paymentLinkRequest.put("notify", notify);

        paymentLinkRequest.put("callback_url", frontendUrl + "/upgrade_plan/success?planType=" + planType);

        PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

        String paymentLinkId = payment.get("id");
        String paymentLinkUrl = payment.get("short_url");

        PaymentLinkResponse res = new PaymentLinkResponse();
        res.setPayment_link_url(paymentLinkUrl);
        res.setPayment_link_id(paymentLinkId);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
