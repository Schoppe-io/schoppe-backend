package com.shoppe.api.business

import NewPageRequest
import Page
import PageMetadata
import ProductCard
import com.shoppe.api.factory.ProductCardFactory
import org.springframework.stereotype.Service

@Service
class FeedBusiness {
    fun getFeed(newPageRequest: NewPageRequest): Page<ProductCard> {
        val allCards = ProductCardFactory.getCollection()

        val filteredCards: List<ProductCard>

        filteredCards =
            if (newPageRequest.isFetchingUp()) {
                allCards
                    .filter { it.id < newPageRequest.beforeCursor!! }
                    .takeLast(newPageRequest.getPageSize())
            } else if (newPageRequest.afterCursor != null) {
                allCards
                    .filter { it.id > newPageRequest.afterCursor }
                    .take(newPageRequest.getPageSize())
            } else {
                allCards.take(newPageRequest.getPageSize())
            }

        return Page(
            data = filteredCards,
            pageMetadata =
                PageMetadata(
                    startCursor = filteredCards.firstOrNull()?.id,
                    endCursor = filteredCards.lastOrNull()?.id,
                    pageSize = filteredCards.size
                )
        )
    }
}
