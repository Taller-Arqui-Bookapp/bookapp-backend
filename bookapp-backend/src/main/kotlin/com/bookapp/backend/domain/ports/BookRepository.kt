package com.bookapp.backend.domain.ports

import com.bookapp.backend.domain.model.Book
import com.bookapp.backend.domain.model.User
import java.util.*

interface BookRepository {
    fun save(book: Book): Book
    fun findById(id: UUID): Book?
    fun findByIsbn(isbn: String): Book?
    fun search(q: String?, isbn: String?, page: Int, size: Int): Pair<List<Book>, Long>
    fun deleteById(id: UUID)
    fun listUsers(bookId: UUID, page: Int, size: Int): Pair<List<User>, Long>
}
