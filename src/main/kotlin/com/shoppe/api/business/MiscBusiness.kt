package com.shoppe.api.business

import com.shoppe.api.config.StripeConfig
import org.springframework.stereotype.Service

data class ConfigResponse(val stripePublishableKey: String)

@Service
class MiscBusiness(val stripeConfig: StripeConfig) {
    fun getConfig(): ConfigResponse {
        return ConfigResponse(stripePublishableKey = stripeConfig.publishableKey)
    }
}
