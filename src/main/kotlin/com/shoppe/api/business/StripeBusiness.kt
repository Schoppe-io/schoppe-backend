package com.shoppe.api.business

import com.shoppe.api.config.StripeConfig
import com.stripe.model.PaymentIntent
import com.stripe.net.Webhook
import org.springframework.stereotype.Service

@Service
class StripeBusiness(val stripeConfig: StripeConfig, val orderBusiness: OrderBusiness) {
    fun onWebhookEvent(payload: String, stripeSignature: String) {
        val event = Webhook.constructEvent(payload, stripeSignature, stripeConfig.webhookSigningKey)

        val intent = event.dataObjectDeserializer.getObject().get() as PaymentIntent

        println(intent)
        when (event.type) {
            "payment_intent.succeeded" -> orderBusiness.onPaymentSuccess(intent)
            "payment_intent.payment_failed" -> orderBusiness.onPaymentFailure(intent)
            else -> {}
        }
    }
}
