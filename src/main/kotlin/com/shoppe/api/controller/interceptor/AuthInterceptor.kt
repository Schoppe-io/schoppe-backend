package com.shoppe.api.controller.interceptor

import AuthenticatedRequestContext
import com.shoppe.api.RequestAttr
import com.shoppe.api.RequestHeader
import com.shoppe.api.annotation.SkipAuthentication
import com.shoppe.api.business.util.hasElapsed
import com.shoppe.api.business.util.toLocalDateTime
import com.shoppe.api.config.TokenConfig
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor(val tokenConfig: TokenConfig) : HandlerInterceptor {
    var logger = KotlinLogging.logger {}

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val logger = KotlinLogging.logger {}

        if (request.method.equals("options", ignoreCase = true)) {
            logger.info("Preflight request - Skipping authentication")
            return true
        }

        if (
            request.servletPath.contains("api-docs", ignoreCase = true) ||
                request.servletPath.contains("swagger", ignoreCase = true)
        ) {
            logger.info("Skipping authentication api doc")
            return true
        }

        val controllerMethod = handler as HandlerMethod
        val method = controllerMethod.method

        if (method.isAnnotationPresent(SkipAuthentication::class.java)) {
            logger.info("Skipping authentication for this method")
            return true
        }

        val authToken = request.getHeader(RequestHeader.AUTH_TOKEN_HEADER)
        val signingKey = Keys.hmacShaKeyFor(tokenConfig.secret.toByteArray())
        val jwtParser = Jwts.parserBuilder().setSigningKey(signingKey).build()
        try {
            val claims = jwtParser.parseClaimsJws(authToken)

            val issuedAt = claims.body.issuedAt.toLocalDateTime()
            if (issuedAt.plusDays(tokenConfig.expiryInDays).hasElapsed()) {
                logger.info("Token has expired. Returning")
                return false
            }
            val userId = claims.body.subject

            logger.info("Got user id - $userId")
            request.setAttribute(
                RequestAttr.REQUEST_CONTEXT,
                AuthenticatedRequestContext(userExternalId = userId)
            )
            return true
        } catch (e: Exception) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED)
        }
    }
}
