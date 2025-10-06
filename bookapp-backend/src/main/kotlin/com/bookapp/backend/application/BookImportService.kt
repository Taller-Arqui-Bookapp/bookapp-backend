package com.bookapp.backend.application

import com.bookapp.backend.domain.model.Book               // <-- AJUSTA si tu modelo está en otro paquete
import com.bookapp.backend.domain.ports.BookInfoPort
import com.bookapp.backend.domain.ports.BookRepository     // <-- AJUSTA si tu puerto repos está en otro paquete
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class BookImportService(
    private val info: BookInfoPort,
    private val books: BookRepository
) {
    fun importByIsbn(isbn: String): Book {
        books.findByIsbn(isbn)?.let { return it }

        val ext = info.findByIsbn(isbn) ?: throw IllegalArgumentException("Book not found in external API")
        val entity = Book(
            id = UUID.randomUUID(),
            title = ext.title,
            author = ext.authors.firstOrNull() ?: "Unknown",
            isbn = ext.isbn13 ?: isbn,
            publishedYear = ext.publishedYear,
            coverUrl = ext.coverUrl,   // <--- portada
            createdAt = Instant.now()
        )
        return books.save(entity)
    }

    fun searchExternal(query: String, page: Int, size: Int) =
        info.search(query, page, size)
}
