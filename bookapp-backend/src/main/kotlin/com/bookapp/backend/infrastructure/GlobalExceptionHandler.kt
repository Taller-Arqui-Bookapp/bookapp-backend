package com.bookapp.backend.infrastructure

import com.bookapp.backend.infrastructure.exceptions.ConflictException
import com.bookapp.backend.infrastructure.exceptions.NotFoundException
import org.slf4j.MDC
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private fun error(status: HttpStatus, msg: String, code: String? = null): ResponseEntity<ApiError> {
        val traceId = MDC.get("traceId")
        return ResponseEntity
            .status(status)
            .body(ApiError(status.value(), code, msg, traceId))
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException) = error(HttpStatus.NOT_FOUND, ex.message ?: "Not found", "NOT_FOUND")

    @ExceptionHandler(ConflictException::class, DataIntegrityViolationException::class, IllegalStateException::class)
    fun handleConflict(ex: Exception) = error(HttpStatus.CONFLICT, ex.message ?: "Conflict", "CONFLICT")

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ApiError> {
        val details = ex.bindingResult
            .allErrors
            .joinToString("; ") {
                if (it is FieldError) "${it.field}: ${it.defaultMessage}" else it.defaultMessage ?: "invalid"
            }
        return error(HttpStatus.UNPROCESSABLE_ENTITY, details, "VALIDATION_ERROR")
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception) = error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", "INTERNAL_ERROR")
}
