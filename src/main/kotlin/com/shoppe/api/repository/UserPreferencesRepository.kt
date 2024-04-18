package com.shoppe.api.repository;

import com.shoppe.api.business.model.db.UserPreferences
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserPreferencesRepository : CrudRepository<UserPreferences, Long> {
    fun findByUserId(userId: Long): UserPreferences?
}
