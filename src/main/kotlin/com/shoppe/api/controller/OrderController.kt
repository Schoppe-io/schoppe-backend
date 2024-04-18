package com.shoppe.api.controller

import AuthenticatedRequestContext
import com.shoppe.api.RequestAttr
import com.shoppe.api.business.InitiateOrderResponse
import com.shoppe.api.business.OrderBusiness
import com.shoppe.api.business.OrderResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(val orderBusiness: OrderBusiness) {
    @PostMapping("/{orderId}")
    fun getOrder(
        @RequestAttribute(RequestAttr.REQUEST_CONTEXT)
        authenticatedRequestContext: AuthenticatedRequestContext,
        @PathVariable("orderId") orderId: String
    ): ResponseEntity<OrderResponse> {
        return ResponseEntity.ok()
            .body(orderBusiness.getOrder(
                orderId = orderId,
                userId = authenticatedRequestContext.userExternalId
            ))
    }

    @PostMapping("initiate-payment")
    fun initiatePayment(
        @RequestAttribute(RequestAttr.REQUEST_CONTEXT)
        authenticatedRequestContext: AuthenticatedRequestContext
    ): ResponseEntity<InitiateOrderResponse> {
        return ResponseEntity.ok()
            .body(orderBusiness.initiateOrder(authenticatedRequestContext.userExternalId))
    }
}
