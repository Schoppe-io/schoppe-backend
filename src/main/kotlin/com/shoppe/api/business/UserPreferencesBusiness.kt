package com.shoppe.api.business

import com.shoppe.api.business.model.db.Address
import com.shoppe.api.business.model.db.AppTheme
import com.shoppe.api.business.model.db.UserPreferences
import com.shoppe.api.repository.UserPreferencesRepository
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Service

data class UserPreferencesResponse(
    val interests: List<String>,
    val defaultBillingAddressId: String?,
    var defaultShippingAddressId: String?,
    val addresses: List<Address>,
    val appTheme: AppTheme
)

data class UserPreferencesUpdateRequest(
    val interests: List<String>?,
    val defaultBillingAddressId: String?,
    var defaultShippingAddressId: String?,
    val addresses: List<Address>?,
    val appTheme: AppTheme?
)

@Service
class UserPreferencesBusiness(
    val userPreferencesRepository: UserPreferencesRepository,
    val userProfileBusiness: UserProfileBusiness
) {
    fun findByUserIdSafe(userId: Long): UserPreferences? {
        return userPreferencesRepository.findByUserId(userId)
    }

    fun findByUserId(userId: Long): UserPreferences {
        return findByUserIdSafe(userId) ?: throw ChangeSetPersister.NotFoundException()
    }

    fun getUserPreferences(userExternalId: String): UserPreferencesResponse {
        val user = userProfileBusiness.findByExternalId(userExternalId)
        val userPreferences = findByUserIdSafe(user.id)

        if (userPreferences == null) {
            val newUserPreferences =
                UserPreferences(
                    userId = user.id,
                )
            userPreferencesRepository.save(newUserPreferences)
            return mapUserPreferencesToUserPreferencesResponse(newUserPreferences)
        }

        return mapUserPreferencesToUserPreferencesResponse(userPreferences)
    }

    fun updateUserPreferences(
        userExternalId: String,
        userPreferencesUpdateRequest: UserPreferencesUpdateRequest
    ): UserPreferencesResponse {
        val user = userProfileBusiness.findByExternalId(userExternalId)
        val userPreferences = findByUserId(user.id)

        if (userPreferencesUpdateRequest.interests != null) {
            userPreferences.interests = userPreferencesUpdateRequest.interests
        }
        if (userPreferencesUpdateRequest.addresses != null) {
            userPreferences.addresses = userPreferencesUpdateRequest.addresses
        }
        if (userPreferencesUpdateRequest.defaultBillingAddressId != null) {
            userPreferences.defaultBillingAddressId =
                userPreferencesUpdateRequest.defaultBillingAddressId
        }
        if (userPreferencesUpdateRequest.defaultShippingAddressId != null) {
            userPreferences.defaultShippingAddressId =
                userPreferencesUpdateRequest.defaultShippingAddressId
        }
        if (userPreferencesUpdateRequest.appTheme != null) {
            userPreferences.appTheme = userPreferencesUpdateRequest.appTheme
        }
        userPreferencesRepository.save(userPreferences)
        return mapUserPreferencesToUserPreferencesResponse(userPreferences)
    }

    private fun mapUserPreferencesToUserPreferencesResponse(
        userPreferences: UserPreferences
    ): UserPreferencesResponse {
        return UserPreferencesResponse(
            interests = userPreferences.interests,
            addresses = userPreferences.addresses,
            defaultBillingAddressId = userPreferences.defaultBillingAddressId,
            defaultShippingAddressId = userPreferences.defaultShippingAddressId,
            appTheme = userPreferences.appTheme
        )
    }
}
