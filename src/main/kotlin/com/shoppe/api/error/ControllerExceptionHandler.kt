package com.shoppe.api.error

import com.shoppe.api.RequestAttr
import com.shoppe.api.business.util.CommonUtil
import com.shoppe.api.config.Env
import java.util.stream.Collectors
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class ControllerExceptionHandler(val commonUtil: CommonUtil) {
    var logger = KotlinLogging.logger {}

    @ExceptionHandler(value = [java.lang.Exception::class])
    fun handleException(exception: Exception, request: WebRequest): ResponseEntity<ApiError> {
        logger.error("Exception occurred {} msg: ${exception.message}", exception)
        //		commonService.captureExceptionInSentry(exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiError(
                    code = ErrorCode.SOMETHING_WENT_WRONG,
                    message = ErrorCode.SOMETHING_WENT_WRONG.message,
                    requestId = getRequestId(request),
                    debugMessage = getDebugMessage(exception),
                    subErrors = arrayListOf()
                )
            )
    }

    @ExceptionHandler(value = [HttpRequestMethodNotSupportedException::class])
    fun handleHttpMethodNotSupportedException(exception: Exception, request: WebRequest): ResponseEntity<ApiError> {
        logger.error("Http method not support exception: ${exception.message}", exception)

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(
                ApiError(
                    code = ErrorCode.OPERATION_NOT_ALLOWED,
                    message = ErrorCode.OPERATION_NOT_ALLOWED.message,
                    requestId = getRequestId(request),
                    debugMessage = getDebugMessage(exception),
                    subErrors = arrayListOf()
                )
            )
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ApiError> {
        logger.error(
            "Method argument validation exception occurred {} error: ${exception.message}",
            exception
        )
        //		commonService.captureExceptionInSentry(exception)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ApiError(
                    code = ErrorCode.INPUT_INVALID,
                    message = ErrorCode.INPUT_INVALID.message,
                    requestId = "",
                    debugMessage = getDebugMessage(exception),
                    subErrors =
                        exception.bindingResult.fieldErrors
                            .stream()
                            .map { fieldError ->
                                ApiSubError(
                                    fieldName = fieldError.field,
                                    message = fieldError.defaultMessage.orEmpty()
                                )
                            }
                            .collect(Collectors.toList())
                )
            )
    }

    @ExceptionHandler(value = [EntityNotFoundException::class])
    fun handleEntityNotFoundException(
        exception: EntityNotFoundException,
        request: WebRequest
    ): ResponseEntity<ApiError> {
        logger.error(
            "Entity not found exception occurred {} error: ${exception.message}",
            exception
        )
        //		commonService.captureExceptionInSentry(exception)
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                ApiError(
                    code = ErrorCode.RESOURCE_NOT_FOUND,
                    message = ErrorCode.RESOURCE_NOT_FOUND.message,
                    requestId = getRequestId(request),
                    debugMessage = getDebugMessage(exception),
                    subErrors = arrayListOf(ApiSubError(message = exception.errorMessage))
                )
            )
    }

    @ExceptionHandler(value = [InvalidOperationException::class])
    fun handleInvalidOperationException(
        exception: InvalidOperationException,
        request: WebRequest
    ): ResponseEntity<ApiError> {
        logger.error(
            "Invalid operation exception occurred {} error: ${exception.message}",
            exception
        )
        //		commonService.captureExceptionInSentry(exception)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ApiError(
                    code = ErrorCode.OPERATION_NOT_ALLOWED,
                    message = ErrorCode.OPERATION_NOT_ALLOWED.message,
                    requestId = getRequestId(request),
                    debugMessage = getDebugMessage(exception),
                    subErrors = arrayListOf(ApiSubError(message = exception.errorMessage))
                )
            )
    }

    @ExceptionHandler(value = [InvalidInputException::class])
    fun handleInvalidInputException(
        exception: InvalidInputException,
        request: WebRequest
    ): ResponseEntity<ApiError> {
        logger.error("Invalid input exception occurred {} error: ${exception.message}", exception)
        //		commonService.captureExceptionInSentry(exception)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ApiError(
                    code = ErrorCode.INPUT_INVALID,
                    message = ErrorCode.INPUT_INVALID.message,
                    requestId = getRequestId(request),
                    debugMessage = getDebugMessage(exception),
                    subErrors = arrayListOf(ApiSubError(message = exception.errorMessage))
                )
            )
    }

    @ExceptionHandler(value = [UndefinedStateException::class])
    fun handleUndefinedStateException(
        exception: UndefinedStateException,
        request: WebRequest
    ): ResponseEntity<ApiError> {
        logger.error("Undefined state exception occurred {} error: ${exception.message}", exception)
        //		commonService.captureExceptionInSentry(exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiError(
                    code = ErrorCode.UNDEFINED_STATE,
                    message = ErrorCode.UNDEFINED_STATE.message,
                    requestId = getRequestId(request),
                    debugMessage = getDebugMessage(exception),
                    subErrors = arrayListOf(ApiSubError(message = exception.errorMessage))
                )
            )
    }

    @ExceptionHandler(value = [DataValidationException::class])
    fun handleDataValidationException(
        exception: DataValidationException,
        request: WebRequest
    ): ResponseEntity<ApiError> {
        logger.error("Data validation exception occurred {} error: ${exception.message}", exception)
        //		commonService.captureExceptionInSentry(exception)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ApiError(
                    code = ErrorCode.DATA_VALIDATION_ERROR,
                    message = ErrorCode.DATA_VALIDATION_ERROR.message,
                    requestId = getRequestId(request),
                    debugMessage = getDebugMessage(exception),
                    subErrors = arrayListOf(ApiSubError(message = exception.errorMessage))
                )
            )
    }

    @ExceptionHandler(value = [PaymentServiceException::class])
    fun handlePaymentServiceException(
        exception: PaymentServiceException,
        request: WebRequest
    ): ResponseEntity<ApiError> {
        logger.error("Payment service exception occurred {} error: ${exception.message}", exception)
        //		commonService.captureExceptionInSentry(exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiError(
                    code = ErrorCode.PAYMENT_FAILURE,
                    message = ErrorCode.PAYMENT_FAILURE.message,
                    requestId = getRequestId(request),
                    debugMessage = getDebugMessage(exception),
                    subErrors = arrayListOf(ApiSubError(message = exception.errorMessage))
                )
            )
    }

    @ExceptionHandler(value = [ExternalServiceException::class])
    fun handleExternalServiceException(
        exception: ExternalServiceException,
        request: WebRequest
    ): ResponseEntity<ApiError> {
        logger.error(
            "External service exception occurred {} error: ${exception.message}",
            exception
        )
        //		commonService.captureExceptionInSentry(exception)
        return ResponseEntity.status(exception.status)
            .body(
                ApiError(
                    code = exception.apiError.code,
                    message = exception.apiError.message,
                    requestId = getRequestId(request),
                    debugMessage = exception.apiError.debugMessage,
                    subErrors = exception.apiError.subErrors
                )
            )
    }

    private fun getRequestId(request: WebRequest): String {
        return request.getAttribute(
            RequestAttr.REQUEST_ID,
            RequestAttributes.SCOPE_REQUEST
        ) as String? ?: ""
    }

    private fun getDebugMessage(exception: Exception): String {
        if (commonUtil.getEnv() == Env.Prod) return String()
        return exception.message.orEmpty()
    }
}
