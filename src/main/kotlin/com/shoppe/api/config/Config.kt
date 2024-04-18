package com.shoppe.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import kotlin.properties.Delegates

enum class Env {
	Local, Dev, Prod
}

@Configuration
@ConfigurationProperties("general")
class GeneralConfig {
	lateinit var env: Env
}

@Configuration
@ConfigurationProperties("token")
class TokenConfig {
	lateinit var secret: String
	var expiryInDays by Delegates.notNull<Long>()
}

@Configuration
@ConfigurationProperties("stripe")
class StripeConfig {
	lateinit var secretKey: String
	lateinit var publishableKey: String
	lateinit var webhookSigningKey: String
}
