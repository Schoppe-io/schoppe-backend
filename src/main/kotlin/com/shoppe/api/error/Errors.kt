package com.shoppe.api.error

import com.fasterxml.jackson.annotation.JsonProperty

enum class ErrorCode(val message: String) {
	SOMETHING_WENT_WRONG("Something went wrong. We're working on it"),
	RESOURCE_NOT_FOUND("Requested resource is not found"),
	INPUT_INVALID("Invalid input"),
	OPERATION_NOT_ALLOWED("Operation not allowed"),
	UNDEFINED_STATE("Undefined state encountered"),
	PAYMENT_FAILURE("Payment transaction failed"),
	EXTERNAL_FAILURE("External service failure"),
	DATA_VALIDATION_ERROR("Data validation failed"),
}

data class ApiSubError(
	@JsonProperty("fieldName") val fieldName: String? = null,
	@JsonProperty("message") val message: String? = "Error"
)

data class ApiError(
	@JsonProperty("code") val code: ErrorCode,
	@JsonProperty("message") val message: String,
	@JsonProperty("requestId") val requestId: String,
	@JsonProperty("debugMessage") val debugMessage: String,
	@JsonProperty("subErrors") val subErrors: List<ApiSubError>
)

data class EntityNotFoundException(
	val fieldName: String = "id",
	val fieldValue: String,
	val entityType: String,
	val errorMessage: String = "Entity with $fieldName : $fieldValue, type : $entityType, not found"
) : Exception(errorMessage)

data class InvalidOperationException(
	val errorMessage: String = ErrorCode.OPERATION_NOT_ALLOWED.message
) : Exception(errorMessage)

data class InvalidInputException(val errorMessage: String = ErrorCode.INPUT_INVALID.message) :
	Exception(errorMessage)

data class UndefinedStateException(val errorMessage: String = ErrorCode.UNDEFINED_STATE.message) :
	Exception(errorMessage)

data class PaymentServiceException(val errorMessage: String = ErrorCode.PAYMENT_FAILURE.message) :
	Exception(errorMessage)

data class ExternalServiceException(
	val errorMessage: String = ErrorCode.EXTERNAL_FAILURE.message,
	val status: Int,
	val apiError: ApiError
) : RuntimeException(errorMessage)

data class DataValidationException(
	val errorMessage: String = ErrorCode.DATA_VALIDATION_ERROR.message
) : Exception(errorMessage)
