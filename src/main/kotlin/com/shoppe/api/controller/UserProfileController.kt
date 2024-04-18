package com.shoppe.api.controller

import AuthenticatedRequestContext
import com.shoppe.api.RequestAttr
import com.shoppe.api.annotation.SkipAuthentication
import com.shoppe.api.business.UserProfileBusiness
import com.shoppe.api.business.UserResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserProfileController(val userProfileBusiness: UserProfileBusiness) {
    @PostMapping("/create-guest")
    @SkipAuthentication
    fun createGuestUser(): ResponseEntity<Any> {
        return ResponseEntity.ok().body(userProfileBusiness.createGuestUser())
    }

    @PostMapping
    fun updateUserProfile(): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun getUserProfile(
        @RequestAttribute(RequestAttr.REQUEST_CONTEXT)
        authenticatedRequestContext: AuthenticatedRequestContext
    ): ResponseEntity<UserResponse> {
        return ResponseEntity.ok()
            .body(userProfileBusiness.getUser(authenticatedRequestContext.userExternalId))
    }
}
