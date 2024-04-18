package com.shoppe.api.repository

import com.shoppe.api.business.model.db.Order
import org.springframework.data.repository.CrudRepository

interface OrderRepository: CrudRepository<Order, String> {
	fun findByIdAndUserId(orderId: String, userId: String): Order?
}
