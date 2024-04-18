package com.shoppe.api.controller

import com.shoppe.api.annotation.SkipAuthentication
import com.shoppe.api.business.ConfigResponse
import com.shoppe.api.business.MiscBusiness
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/config")
class ConfigController(val miscBusiness: MiscBusiness) {
    @GetMapping
    @SkipAuthentication
    fun getConfig(): ResponseEntity<ConfigResponse> {
        return ResponseEntity.ok().body(miscBusiness.getConfig())
    }
}
