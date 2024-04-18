package com.shoppe.api.controller

import NewPageRequest
import com.shoppe.api.business.FeedBusiness
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/feed")
class FeedController(val feedBusiness: FeedBusiness) {
    @GetMapping
    fun getFeed(
        @RequestParam("after") afterCursor: String?,
        @RequestParam("before") beforeCursor: String?,
        @RequestParam("page_size") pageSize: Int?
    ): ResponseEntity<Any> {
        return ResponseEntity.ok()
            .body(
                feedBusiness.getFeed(
                    newPageRequest = NewPageRequest(
                        afterCursor = afterCursor,
                        beforeCursor = beforeCursor,
                        pageSize = pageSize
                    )
                )
            )
    }
}
