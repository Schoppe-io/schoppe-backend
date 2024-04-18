package com.shoppe.api.controller

import AuthenticatedRequestContext
import com.shoppe.api.RequestAttr
import com.shoppe.api.business.UserCartWithItemsExpanded
import com.shoppe.api.business.UserCartBusiness
import com.shoppe.api.business.UserCartUpdateRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/user/cart")
class UserCartController(val userCartBusiness: UserCartBusiness) {
    @GetMapping
    fun getUserCart(
        @RequestAttribute(RequestAttr.REQUEST_CONTEXT)
        authenticatedRequestContext: AuthenticatedRequestContext
    ): ResponseEntity<UserCartWithItemsExpanded> {
        return ResponseEntity.ok()
            .body(userCartBusiness.getUserCartWithItemsExpanded(authenticatedRequestContext.userExternalId))
    }

    @PutMapping
    fun updateUserCart(
        @RequestAttribute(RequestAttr.REQUEST_CONTEXT)
        authenticatedRequestContext: AuthenticatedRequestContext,
        @Valid @RequestBody userCartUpdateRequest: UserCartUpdateRequest
    ): ResponseEntity<UserCartWithItemsExpanded> {
        return ResponseEntity.ok()
            .body(
                userCartBusiness.updateUserCart(
                    authenticatedRequestContext.userExternalId,
                    userCartUpdateRequest
                )
            )
    }

    @DeleteMapping
    fun clearUserCart(
        @RequestAttribute(RequestAttr.REQUEST_CONTEXT)
        authenticatedRequestContext: AuthenticatedRequestContext
    ): ResponseEntity<UserCartWithItemsExpanded> {
        return ResponseEntity.ok()
            .body(userCartBusiness.clearUserCart(authenticatedRequestContext.userExternalId))
    }
}
