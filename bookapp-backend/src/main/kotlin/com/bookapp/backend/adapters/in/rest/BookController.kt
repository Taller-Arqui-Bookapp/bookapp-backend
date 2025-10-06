package com.bookapp.backend.adapters.`in`.rest

import com.bookapp.backend.application.BookService
import com.bookapp.backend.application.dto.CreateBookRequest
import com.bookapp.backend.application.dto.PageResponse
import com.bookapp.backend.domain.model.Book
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/books")
class BookController(private val service: BookService) {

    @PostMapping(produces = ["application/json","application/xml"])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody body: CreateBookRequest): Book =
        service.create(body.title, body.author, body.isbn, body.publishedYear)

    @GetMapping("/{id}", produces = ["application/json","application/xml"])
    fun get(@PathVariable id: UUID) = service.get(id)

    @GetMapping(produces = ["application/json","application/xml"])
    fun list(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) q: String?,
        @RequestParam(required = false) isbn: String?
    ): PageResponse<Book> {
        val (content, total) = service.list(q, isbn, page, size)
        val totalPages = if (size == 0) 1 else ((total + size - 1) / size).toInt()
        return PageResponse(content, page, size, total, totalPages)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) = service.delete(id)

    @GetMapping("/{id}/users", produces = ["application/json","application/xml"])
    fun listUsers(
        @PathVariable id: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ) = service.listUsers(id, page, size)
}
