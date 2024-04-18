package com.shoppe.api.controller.interceptor

import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class InterceptorConfig(
    val initiatorInterceptor: InitiatorInterceptor,
    val authInterceptor: AuthInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(initiatorInterceptor)
        registry.addInterceptor(authInterceptor)
    }
}
