package com.shoppe.api.controller

import AuthenticatedRequestContext
import com.shoppe.api.RequestAttr
import com.shoppe.api.business.UserPreferencesBusiness
import com.shoppe.api.business.UserPreferencesResponse
import com.shoppe.api.business.UserPreferencesUpdateRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user/preferences")
class UserPreferencesController(val userPreferencesBusiness: UserPreferencesBusiness) {
    @GetMapping
    fun getUserPreferences(
        @RequestAttribute(RequestAttr.REQUEST_CONTEXT)
        authenticatedRequestContext: AuthenticatedRequestContext
    ): ResponseEntity<UserPreferencesResponse> {
        return ResponseEntity.ok()
            .body(
                userPreferencesBusiness.getUserPreferences(
                    authenticatedRequestContext.userExternalId
                )
            )
    }

    @PatchMapping
    fun updateUserPreferences(
        @RequestAttribute(RequestAttr.REQUEST_CONTEXT)
        authenticatedRequestContext: AuthenticatedRequestContext,
        @RequestBody userPreferencesUpdateRequest: UserPreferencesUpdateRequest,
    ): ResponseEntity<UserPreferencesResponse> {
        return ResponseEntity.ok()
            .body(
                userPreferencesBusiness.updateUserPreferences(
                    authenticatedRequestContext.userExternalId,
                    userPreferencesUpdateRequest
                )
            )
    }
}
