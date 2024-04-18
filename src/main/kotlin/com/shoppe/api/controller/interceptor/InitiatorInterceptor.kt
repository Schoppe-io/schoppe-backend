package com.shoppe.api.controller.interceptor

import com.shoppe.api.RequestAttr
import com.shoppe.api.RequestHeader.REQUEST_ID_HEADER
import com.shoppe.api.business.util.CommonUtil
import com.shoppe.api.config.TokenConfig
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class InitiatorInterceptor(val commonUtil: CommonUtil) : HandlerInterceptor {
	var logger = KotlinLogging.logger {}

	override fun preHandle(
		request: HttpServletRequest,
		response: HttpServletResponse,
		handler: Any,
	): Boolean {
		var requestId = request.getHeader(REQUEST_ID_HEADER)

		if (requestId.isNullOrBlank())
			requestId = commonUtil.getNextUlid()

		request.setAttribute(RequestAttr.REQUEST_ID, requestId)

		return true
	}
}

