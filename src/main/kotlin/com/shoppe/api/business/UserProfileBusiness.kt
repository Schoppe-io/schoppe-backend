package com.shoppe.api.business

import com.fasterxml.jackson.annotation.JsonProperty
import com.shoppe.api.business.model.db.User
import com.shoppe.api.business.util.CommonUtil
import com.shoppe.api.business.util.toDate
import com.shoppe.api.config.TokenConfig
import com.shoppe.api.error.EntityNotFoundException
import com.shoppe.api.repository.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.time.LocalDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class CreateGuestUserResponse(val token: String)

data class UserResponse(
    val id: String,
    @get:JsonProperty(value="isGuest") val isGuest: Boolean,
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val isPhoneNumberVerified: Boolean? = null,
    val email: String? = null,
    val isEmailVerified: Boolean? = null,
)

@Service
class UserProfileBusiness(
    val userRepository: UserRepository,
    val commonUtil: CommonUtil,
    val tokenConfig: TokenConfig,
) {
    fun findByExternalIdSafe(userExternalId: String): User? {
        return userRepository.findByExternalId(userExternalId)
    }

    fun findByExternalId(userExternalId: String): User {
        return findByExternalIdSafe(userExternalId) ?: throw EntityNotFoundException(
            fieldName = "externalId",
            fieldValue = userExternalId,
            entityType = User::class.simpleName!!
        )
    }

    @Transactional
    fun createGuestUser(): CreateGuestUserResponse {
        val user = User(externalId = commonUtil.getNextUlid(), guestToRegularAt = null)
        userRepository.save(user)

        val jws =
            Jwts.builder()
                .setIssuer("Shoppe")
                .setSubject(user.externalId)
                .setIssuedAt(LocalDateTime.now().toDate())
                .signWith(Keys.hmacShaKeyFor(tokenConfig.secret.toByteArray()))
                .compact()!!
        return CreateGuestUserResponse(token = jws)
    }

    fun getUser(userExternalId: String): UserResponse {
        val user = findByExternalId(userExternalId = userExternalId)
        return mapUserToUserResponse(user)
    }

    private fun mapUserToUserResponse(user: User): UserResponse {
        return UserResponse(
            id = user.externalId,
            isGuest = user.guestToRegularAt == null,
            firstName = user.firstName,
            lastName = user.lastName,
            phoneNumber = user.phoneNumber,
            isPhoneNumberVerified = user.isPhoneNumberVerified,
            email = user.email,
            isEmailVerified = user.isEmailVerified
        )
    }
}
