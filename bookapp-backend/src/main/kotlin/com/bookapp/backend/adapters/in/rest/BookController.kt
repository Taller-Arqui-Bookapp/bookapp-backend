package com.bookapp.backend.adapters.`in`.rest

import com.bookapp.backend.application.BookImportService
import com.bookapp.backend.application.BookService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/books")
class BookController(
    private val service: BookService,
    private val importService: BookImportService
) {
    // ya tienes tus endpoints normales (listar, crear, etc.) usando 'service'

    @GetMapping("/external", produces = ["application/json"])
    fun searchExternal(
        @RequestParam q: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ) = importService.searchExternal(q, page, size)

    @PostMapping("/import", produces = ["application/json"])
    fun importByIsbn(@RequestParam isbn: String) =
        importService.importByIsbn(isbn)
}
