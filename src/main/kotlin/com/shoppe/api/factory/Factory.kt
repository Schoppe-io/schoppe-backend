package com.shoppe.api.factory

import MerchantDetails
import MerchantUserContext
import ProductCard
import ProductCardUserContext
import ProductVariant
import VariantDimensionAndValues
import com.shoppe.api.business.util.CommonUtil
import com.shoppe.api.config.GeneralConfig
import kotlin.random.Random
import kotlin.random.nextInt

object ProductCardFactory {
    private val commonUtil = CommonUtil(GeneralConfig())

    private var cache: List<ProductCard> = mutableListOf()

    fun getCollection(): List<ProductCard> {
        if (cache.isNotEmpty()) return cache

        val videoUrls =
            (1..11).map {
                "https://shoppe-assets.s3.me-central-1.amazonaws.com/videos/sample$it.MOV"
            }

        val idsAndNames =
            listOf(
                "11GFSP9AGX9BCE6B3JAA0YW969" to "Sonio UltraSmooth",
                "21GFSP9AGXKYGRVSEXAGGYFDSK" to "Black Frill Around Me Dress",
                "31GFSP8V8PABFMBVHCGCZFVFMT" to "Rani Pink Flared Suit (Set of 3)",
                "41GFSP8V8P46836M09SDNN2JNV" to "Maroon Flare Up For A Moment Dress",
                "51GFSP8V8PH851SFFWW7DYKE9C" to "Rose Gold Watch Me Glowing Heels",
                "61GFSP88XTER4SGASA69AZWB5R" to "Subtle Style In Heels",
                "71GFSP88XTP9G2SF9V3JMB4AC1" to "Mahaganpati Hand-Etched Wall Decor",
                "81GFSP88XTBVKZDQK8M8S9GZEC" to "Wallpaper Alexanderstone In Blue Floral Damask",
                "91GFSP6HRY408Z2NPDDJK78YHN" to "The Art House - Abstract painting",
                "92GFSP6HRYVMTCBQGBY2C7FB9F" to "Silver Oxidized Krishna Ghungroo",
                "93GFSP6HRYQYVKQ51YPAWF5H8R" to "Women Gold-Plated Black Chain"
            )
        cache =
            idsAndNames.zip(videoUrls).map {
                get(id = it.first.first, name = it.first.second, videoUrl = it.second)
            }
        return cache
    }

    fun get(id: String? = null, name: String, videoUrl: String): ProductCard {
        val finalId = id ?: commonUtil.getNextUlid()
        return ProductCard(
            cardType = CardType.FULLSCREEN_VIDEO,
            id = finalId,
            title = name,
            description = "The sweetest bird",
            portraitCoverImageUrl = "https://shoppe-assets.s3.me-central-1.amazonaws.com/images/dummy-product-cover.png",
            merchantDetails =
                MerchantDetails(
                    name = "Tanaz by Nisas",
                    logoUrl =
                        "https://upload.wikimedia.org/wikipedia/commons/c/cb/Louis_Vuitton_LV_logo.png",
                    description =
                        "Louis Vuitton Malletier, commonly known as Louis Vuitton, is a French luxury fashion house and company founded in 1854 by Louis Vuitton.",
                    followersCount = Random.nextInt(100..1000),
                    userContext = MerchantUserContext(isFollowing = Random.nextBoolean())
                ),
            variantSpec =
                listOf(
                    VariantDimensionAndValues(dimension = "size", values = listOf("S", "L", "XL")),
                    VariantDimensionAndValues(
                        dimension = "color",
                        values = listOf("Red", "Orange", "Brown")
                    ),
                ),
            variants =
                listOf(
                    ProductVariant(
                        dimensionIdentifier = "S_Red",
                        id = "${finalId}-1",
                        title = null,
                        portraitCoverImageUrl = null,
                        price = Random.nextInt(100..1000).toBigDecimal()
                    ),
                    ProductVariant(
                        dimensionIdentifier = "L_Red",
                        id = "${finalId}-2",
                        title = null,
                        portraitCoverImageUrl = null,
                        price = Random.nextInt(100..1000).toBigDecimal()
                    ),
                    ProductVariant(
                        dimensionIdentifier = "L_Orange",
                        id = "${finalId}-3",
                        title = null,
                        portraitCoverImageUrl = null,
                        price = Random.nextInt(100..1000).toBigDecimal()
                    ),
                    ProductVariant(
                        dimensionIdentifier = "L_Brown",
                        id = "${finalId}-3",
                        title = null,
                        portraitCoverImageUrl = null,
                        price = Random.nextInt(100..1000).toBigDecimal()
                    ),
                    ProductVariant(
                        dimensionIdentifier = "XL_Orange",
                        id = "${finalId}-3",
                        title = null,
                        portraitCoverImageUrl = null,
                        price = Random.nextInt(100..1000).toBigDecimal()
                    )
                ),
            videoUrl = videoUrl,
            tags = listOf("fashion", "lifestyle"),
            userContext = ProductCardUserContext(isBookmarked = true)
        )
    }
}
