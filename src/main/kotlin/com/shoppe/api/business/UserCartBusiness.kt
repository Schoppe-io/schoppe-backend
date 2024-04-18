package com.shoppe.api.business

import VariantDimensionAndValues
import com.fasterxml.jackson.annotation.JsonProperty
import com.shoppe.api.business.model.db.CartItem
import com.shoppe.api.business.model.db.UserCart
import com.shoppe.api.error.EntityNotFoundException
import com.shoppe.api.repository.UserCartRepository
import java.math.BigDecimal
import org.springframework.stereotype.Service

data class CartItemExpanded(
    val productId: String,
    val productTitle: String,
    val productPortraitCoverImageUrl: String,
    val variantSpec: List<VariantDimensionAndValues>,
    val dimensionIdentifier: String,
    val variantId: String,
    val variantTitle: String?,
    val variantPortraitCoverImageUrl: String?,
    val price: BigDecimal,
    val quantity: Int,
    val totalAmount: BigDecimal
)

data class UserCartWithItemsExpanded(val totalAmount: BigDecimal, val items: List<CartItemExpanded>)

data class UserCartUpdateRequest(@JsonProperty("items") val items: List<CartItem>)

@Service
class UserCartBusiness(
    val userCartRepository: UserCartRepository,
    val userProfileBusiness: UserProfileBusiness,
    val productBusiness: ProductBusiness
) {
    fun findByUserIdSafe(userId: Long): UserCart? {
        return userCartRepository.findByUserId(userId)
    }

    fun findByUserId(userId: Long): UserCart {
        return findByUserIdSafe(userId)
            ?: throw EntityNotFoundException(
                entityType = UserCart::class.simpleName!!,
                fieldValue = userId.toString(),
                fieldName = "userId"
            )
    }

    fun getUserCartWithItemsExpanded(userExternalId: String): UserCartWithItemsExpanded {
        val user = userProfileBusiness.findByExternalId(userExternalId)
        val userCart = findByUserIdSafe(user.id)

        if (userCart == null) {
            val newUserCart = UserCart(userId = user.id)
            userCartRepository.save(newUserCart)
            return mapUserCartToUserCartWithItemsExpanded(newUserCart)
        }

        return mapUserCartToUserCartWithItemsExpanded(userCart)
    }

    fun updateUserCart(
        userExternalId: String,
        userCartUpdateRequest: UserCartUpdateRequest,
    ): UserCartWithItemsExpanded {
        val user = userProfileBusiness.findByExternalId(userExternalId)
        val userCart = findByUserId(user.id)
        userCart.items = userCartUpdateRequest.items
        userCartRepository.save(userCart)
        return mapUserCartToUserCartWithItemsExpanded(userCart)
    }

    fun clearUserCart(userExternalId: String): UserCartWithItemsExpanded {
        val user = userProfileBusiness.findByExternalId(userExternalId)
        val userCart = findByUserId(user.id)
        userCart.items = listOf()
        userCartRepository.save(userCart)
        return mapUserCartToUserCartWithItemsExpanded(userCart)
    }

    private fun mapUserCartToUserCartWithItemsExpanded(userCart: UserCart): UserCartWithItemsExpanded {
        val items = userCart.items
        val productSummaries =
            productBusiness.getProductSummaries(
                items.map {
                    ProductAndVariantId(productId = it.productId, variantId = it.variantId)
                }
            )

        val cartItemRespons =
            productSummaries.zip(items).map { (productSummary, item) ->
                mapProductSummaryToCartItemExpanded(productSummary, item.quantity)
            }

        val totalAmount = cartItemRespons.sumOf { it.totalAmount }
        return UserCartWithItemsExpanded(totalAmount = totalAmount, items = cartItemRespons)
    }

    private fun mapProductSummaryToCartItemExpanded(productSummary: ProductSummary, quantity: Int): CartItemExpanded {
        return CartItemExpanded(
            productId = productSummary.productId,
            productTitle = productSummary.productTitle,
            productPortraitCoverImageUrl = productSummary.productPortraitCoverImageUrl,
            variantSpec = productSummary.variantSpec,
            dimensionIdentifier = productSummary.dimensionIdentifier,
            variantId = productSummary.variantId,
            variantTitle = productSummary.variantTitle,
            variantPortraitCoverImageUrl = productSummary.variantPortraitCoverImageUrl,
            price = productSummary.price,
            quantity = quantity,
            totalAmount = productSummary.price.multiply(quantity.toBigDecimal())
        )
    }
}
