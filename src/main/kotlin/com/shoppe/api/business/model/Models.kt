import java.math.BigDecimal

data class ProductCard(
    val cardType: CardType,
    val id: String,
    val title: String,
    val description: String,
    val portraitCoverImageUrl: String,
    val merchantDetails: MerchantDetails,
    val variantSpec: List<VariantDimensionAndValues>,
    val variants: List<ProductVariant>,
    val videoUrl: String,
    val tags: List<String>,
    val userContext: ProductCardUserContext
)

data class VariantDimensionAndValues(val dimension: String, val values: List<String>)

class ProductCardUserContext(val isBookmarked: Boolean)

class ProductVariant(
    val dimensionIdentifier: String,
    val id: String,
    val title: String?,
    val portraitCoverImageUrl: String?,
    val price: BigDecimal,
)

class MerchantDetails(
    val name: String,
    val logoUrl: String,
    val description: String,
    val followersCount: Int,
    val userContext: MerchantUserContext
)

class MerchantUserContext(val isFollowing: Boolean)

enum class CardType {
    FULLSCREEN_VIDEO
}

class AuthenticatedRequestContext(val userExternalId: String)

data class PageMetadata(val startCursor: String?, val endCursor: String?, val pageSize: Int)

data class Page<T>(val data: List<T>, val pageMetadata: PageMetadata)

enum class NewPageDirection {
    UP,
    DOWN
}

data class NewPageRequest(
    val afterCursor: String? = null,
    val beforeCursor: String? = null,
    val pageSize: Int? = null
) {
    fun getPageSize(): Int {
        return pageSize ?: 3
    }

    private fun getNewPageDirection(): NewPageDirection {
        if (beforeCursor != null) {
            return NewPageDirection.UP
        }
        return NewPageDirection.DOWN
    }

    fun isFetchingUp(): Boolean {
        return getNewPageDirection() == NewPageDirection.UP
    }

    fun isFetchingDown(): Boolean {
        return getNewPageDirection() == NewPageDirection.DOWN
    }
}
