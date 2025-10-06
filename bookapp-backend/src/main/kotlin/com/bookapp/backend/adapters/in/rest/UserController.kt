package com.bookapp.backend.adapters.`in`.rest

import com.bookapp.backend.application.UserService
import com.bookapp.backend.application.dto.CreateUserRequest
import com.bookapp.backend.domain.model.User
import com.bookapp.backend.application.dto.PageResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.validation.annotation.Validated
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@Validated
@RestController
@RequestMapping("/api/v1/users")
class UserController(private val service: UserService) {

    @PostMapping(produces = ["application/json","application/xml"])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody body: CreateUserRequest): User =
        service.create(body.name, body.email)

    @GetMapping("/{id}", produces = ["application/json","application/xml"])
    fun get(@PathVariable id: UUID) = service.get(id)

    @GetMapping(produces = ["application/json","application/xml"])
    fun list(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) q: String?
    ): PageResponse<User> {
        val (content, total) = service.list(q, page, size)
        val totalPages = if (size == 0) 1 else ((total + size - 1) / size).toInt()
        return PageResponse(content, page, size, total, totalPages)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) = service.delete(id)

    @GetMapping("/{id}/books", produces = ["application/json","application/xml"])
    fun listBooks(
        @PathVariable id: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ) = service.listBooks(id, page, size)
}
