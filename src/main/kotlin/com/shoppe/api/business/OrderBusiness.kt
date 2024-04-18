package com.shoppe.api.business

import com.shoppe.api.business.model.db.AmountDetails
import com.shoppe.api.business.model.db.Order
import com.shoppe.api.business.model.db.OrderStatus
import com.shoppe.api.business.util.CommonUtil
import com.shoppe.api.config.StripeConfig
import com.shoppe.api.error.EntityNotFoundException
import com.shoppe.api.repository.OrderRepository
import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import mu.KotlinLogging
import java.math.BigDecimal
import org.springframework.stereotype.Service

class InitiateOrderResponse(val orderId: String, val paymentIntentSecretKey: String)

class OrderResponse(
    val id: String,
    val status: OrderStatus,
    val amountDetails: AmountDetails
)

@Service
class OrderBusiness(
    val userCartBusiness: UserCartBusiness,
    val stripeConfig: StripeConfig,
    val commonUtil: CommonUtil,
    val orderRepository: OrderRepository
) {
    val logger = KotlinLogging.logger {  }

    fun findByOrderIdSafe(orderId: String): Order? {
        return orderRepository.findById(orderId).get()
    }

    fun findByOrderId(orderId: String): Order {
        return findByOrderIdSafe(orderId)
            ?: throw EntityNotFoundException(
                entityType = Order::class.simpleName!!,
                fieldValue = orderId,
                fieldName = "id"
            )
    }

    fun findByOrderIdAndUserIdSafe(orderId: String, userId: String): Order? {
        return orderRepository.findByIdAndUserId(orderId, userId)
    }

    fun findByOrderIdAndUserId(orderId: String, userId: String): Order {
        return findByOrderIdAndUserIdSafe(orderId, userId)
            ?: throw EntityNotFoundException(
                entityType = Order::class.simpleName!!,
                fieldValue = orderId,
                fieldName = "id"
            )
    }

    fun getOrder(orderId: String, userId: String): OrderResponse {
        val order = findByOrderIdAndUserId(orderId, userId)
        return OrderResponse(
            id = order.id,
            status = order.status,
            amountDetails = order.amountDetails
        )
    }

    fun createStripePaymentIntentFor(orderId: String): PaymentIntent {
        Stripe.apiKey = stripeConfig.secretKey
        val paymentIntentParams =
            PaymentIntentCreateParams.builder().setAmount(1000).setCurrency("AED")
                .putMetadata("orderId", orderId)
                .build()
        return PaymentIntent.create(paymentIntentParams)
    }

    fun onPaymentSuccess(intent: PaymentIntent) {
        val orderId = intent.metadata["orderId"]
        logger.info("Got payment success for order $orderId")

        if (orderId == null) {
            logger.info("Order id is null $orderId")
            return
        }

        val order = findByOrderId(orderId)
        order.status = OrderStatus.PAYMENT_SUCCESS
        orderRepository.save(order)
        logger.info("Order $orderId status updated to ${OrderStatus.PAYMENT_SUCCESS}")
    }

    fun onPaymentFailure(intent: PaymentIntent) {
        val orderId = intent.metadata["orderId"]
        logger.info("Got payment failure for order $orderId")

        if (orderId == null) {
            logger.info("Order id is null")
            return
        }

        val order = findByOrderId(orderId)
        order.status = OrderStatus.PAYMENT_FAILED
        orderRepository.save(order)
        logger.info("Order $orderId status updated to ${OrderStatus.PAYMENT_FAILED}")
    }

    fun initiateOrder(userExternalId: String): InitiateOrderResponse {
        val cartWithItemsExpanded = userCartBusiness.getUserCartWithItemsExpanded(userExternalId)
        val order =
            Order(
                id = commonUtil.getNextUlid(),
                userId = userExternalId,
                status = OrderStatus.INITIATED,
                amountDetails =
                    AmountDetails(
                        totalAmount = cartWithItemsExpanded.totalAmount,
                        discountAmount = BigDecimal.ZERO,
                        payableAmount = cartWithItemsExpanded.totalAmount
                    ),
                items = cartWithItemsExpanded.items
            )
        orderRepository.save(order)

        val stripePaymentIntent = createStripePaymentIntentFor(order.id)
        return InitiateOrderResponse(
            orderId = order.id,
            paymentIntentSecretKey = stripePaymentIntent.clientSecret
        )
    }
}
