package com.shoppe.api.business.model.db

import com.shoppe.api.business.CartItemExpanded
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal

@Entity(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
    val externalId: String,
    var guestToRegularAt: LocalDateTime?,
    var firstName: String? = null,
    var lastName: String? = null,
    var phoneNumber: String? = null,
    var isPhoneNumberVerified: Boolean? = null,
    var email: String? = null,
    var isEmailVerified: Boolean? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @UpdateTimestamp var updatedAt: LocalDateTime = LocalDateTime.now(),
    var deletedAt: LocalDateTime? = null,
)

enum class AppTheme {
    dark,
    light,
    system
}

data class Address(
    val id: String,
    val apartmentNumber: String? = null,
    val buildingName: String? = null,
    val street1: String,
    val street2: String? = null,
    val city: String,
    val pinCode: String,
    val coords: Pair<Float, Float>? = null
)

@Entity(name = "user_preferences")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
data class UserPreferences(
    @Id val userId: Long,
    @Type(type = "jsonb") var interests: List<String> = listOf(),
    var defaultBillingAddressId: String? = null,
    var defaultShippingAddressId: String? = null,
    @Type(type = "jsonb") var addresses: List<Address> = listOf(),
    @Enumerated(EnumType.STRING) var appTheme: AppTheme = AppTheme.system,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @UpdateTimestamp var updatedAt: LocalDateTime = LocalDateTime.now(),
    var deletedAt: LocalDateTime? = null,
)

data class CartItem(val productId: String, val variantId: String, val quantity: Int)

@Entity(name = "user_cart")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
data class UserCart(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
    val userId: Long,
    @Type(type = "jsonb") var items: List<CartItem> = listOf(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @UpdateTimestamp var updatedAt: LocalDateTime = LocalDateTime.now(),
    var deletedAt: LocalDateTime? = null,
)

enum class OrderStatus {
    INITIATED, COMPLETED, PAYMENT_FAILED, PAYMENT_SUCCESS
}

data class AmountDetails(
    val totalAmount: BigDecimal,
    val discountAmount: BigDecimal,
    val payableAmount: BigDecimal,
)

@Entity(name = "orders")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
data class Order(
    @Id val id: String,
    val userId: String,
    var status: OrderStatus,
    @Type(type = "jsonb") val amountDetails: AmountDetails,
    @Type(type = "jsonb") val items: List<CartItemExpanded> = listOf(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @UpdateTimestamp var updatedAt: LocalDateTime = LocalDateTime.now(),
    var deletedAt: LocalDateTime? = null,
)
