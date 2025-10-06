package com.bookapp.backend.domain.ports

data class ExternalBook(
    val title: String,
    val authors: List<String>,
    val isbn13: String?,
    val publishedYear: Int?,
    val coverUrl: String?
)

interface BookInfoPort {
    fun findByIsbn(isbn: String): ExternalBook?
    fun search(query: String, page: Int, size: Int): List<ExternalBook>
}
