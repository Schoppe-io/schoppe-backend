package com.shoppe.api.business

import VariantDimensionAndValues
import com.shoppe.api.factory.ProductCardFactory
import java.math.BigDecimal
import org.springframework.stereotype.Service

data class ProductSummary(
    val productId: String,
    val productTitle: String,
    val productPortraitCoverImageUrl: String,
    val variantSpec: List<VariantDimensionAndValues>,
    val dimensionIdentifier: String,
    val variantId: String,
    val variantTitle: String?,
    val variantPortraitCoverImageUrl: String?,
    val price: BigDecimal
)

data class ProductAndVariantId(val productId: String, val variantId: String)

@Service
class ProductBusiness {
    fun getProductSummaries(productAndVariantIds: List<ProductAndVariantId>): List<ProductSummary> {
        val cards = ProductCardFactory.getCollection()

        return productAndVariantIds.map { productAndVariantId ->
            val card = cards.first { it.id == productAndVariantId.productId }
            val variant = card.variants.first{ it.id == productAndVariantId.variantId }
            ProductSummary(
                productId = card.id,
                productTitle = card.title,
                productPortraitCoverImageUrl = card.portraitCoverImageUrl,
                variantSpec = card.variantSpec,
                dimensionIdentifier = variant.dimensionIdentifier,
                variantId = variant.id,
                variantTitle = variant.title,
                variantPortraitCoverImageUrl = variant.portraitCoverImageUrl,
                price = variant.price
            )
        }
    }
}
