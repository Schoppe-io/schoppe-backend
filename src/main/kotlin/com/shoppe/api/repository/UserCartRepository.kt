package com.shoppe.api.repository

import com.shoppe.api.business.model.db.UserCart
import org.springframework.data.repository.CrudRepository

interface UserCartRepository : CrudRepository<UserCart, Long> {
	fun findByUserId(userId: Long): UserCart?
}
