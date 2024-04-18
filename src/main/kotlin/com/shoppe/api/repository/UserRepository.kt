package com.shoppe.api.repository

import com.shoppe.api.business.model.db.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByExternalId(externalId: String): User?
}
