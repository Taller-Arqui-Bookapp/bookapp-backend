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

    fun importByTitle(title: String): Book {
        // Buscar primero si ya existe un libro con ese título
        val existing = books.search(title, null, 0, 1).first
        if (existing.isNotEmpty()) {
            return existing.first()
        }

        // Buscar en la API externa por título
        val searchResults = info.search(title, 0, 1)
        if (searchResults.isEmpty()) {
            throw IllegalArgumentException("Book with title '$title' not found in external API")
        }

        val ext = searchResults.first()
        val entity = Book(
            id = UUID.randomUUID(),
            title = ext.title,
            author = ext.authors.firstOrNull() ?: "Unknown",
            isbn = ext.isbn13 ?: generateTempIsbn(),
            publishedYear = ext.publishedYear,
            coverUrl = ext.coverUrl,
            createdAt = Instant.now()
        )
        return books.save(entity)
    }

    private fun generateTempIsbn(): String {
        // Generar un ISBN temporal único basado en timestamp
        val timestamp = System.currentTimeMillis()
        return "TEMP-$timestamp"
    }

    fun searchExternal(query: String, page: Int, size: Int) =
        info.search(query, page, size)

    fun importSelectedBooks(selectedBooks: List<com.bookapp.backend.application.dto.ExternalBookSelection>): com.bookapp.backend.application.dto.BulkImportResponse {
        val imported = mutableListOf<com.bookapp.backend.application.dto.BookImportResult>()
        val errors = mutableListOf<com.bookapp.backend.application.dto.BookImportError>()

        for (selection in selectedBooks) {
            try {
                // Verificar si ya existe un libro con el mismo ISBN o título
                val isbn = selection.isbn
                val existingBook = if (!isbn.isNullOrBlank()) {
                    books.findByIsbn(isbn)
                } else {
                    books.search(selection.title, null, 0, 1).first.firstOrNull()
                }

                if (existingBook != null) {
                    imported.add(
                        com.bookapp.backend.application.dto.BookImportResult(
                            selection = selection,
                            importedBook = existingBook,
                            wasAlreadyExists = true
                        )
                    )
                } else {
                    // Crear nuevo libro
                    val newBook = Book(
                        id = UUID.randomUUID(),
                        title = selection.title,
                        author = selection.authors.firstOrNull() ?: "Unknown",
                        isbn = selection.isbn ?: generateTempIsbn(),
                        publishedYear = selection.publishedYear,
                        coverUrl = selection.coverUrl,
                        createdAt = Instant.now()
                    )
                    val savedBook = books.save(newBook)
                    
                    imported.add(
                        com.bookapp.backend.application.dto.BookImportResult(
                            selection = selection,
                            importedBook = savedBook,
                            wasAlreadyExists = false
                        )
                    )
                }
            } catch (e: Exception) {
                errors.add(
                    com.bookapp.backend.application.dto.BookImportError(
                        selection = selection,
                        error = e.message ?: "Unknown error occurred"
                    )
                )
            }
        }

        return com.bookapp.backend.application.dto.BulkImportResponse(imported, errors)
    }
}
