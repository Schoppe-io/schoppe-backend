package com.shoppe.api.controller

import com.shoppe.api.business.StripeBusiness
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stripe/webhook")
class StripeWebhookController(val stripeBusiness: StripeBusiness) {
    @PostMapping
    fun handleStripeWebhook(
        @RequestHeader("Stripe-Signature") stripeSignature: String,
        @RequestBody payload: String
    ) {
        stripeBusiness.onWebhookEvent(payload, stripeSignature)
    }
}
