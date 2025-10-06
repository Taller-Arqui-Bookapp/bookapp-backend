package com.bookapp.backend.application

import com.bookapp.backend.domain.model.Book
import com.bookapp.backend.domain.model.User
import com.bookapp.backend.domain.ports.BookRepository
import com.bookapp.backend.domain.ports.UserBookRepository
import com.bookapp.backend.domain.ports.UserRepository
import com.bookapp.backend.infrastructure.exceptions.ConflictException
import com.bookapp.backend.infrastructure.exceptions.NotFoundException
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class UserService(private val users: UserRepository, private val books: BookRepository) {
    fun create(name: String, email: String): User {
        if (users.findByEmail(email) != null) throw ConflictException("email already exists")
        val u = User(UUID.randomUUID(), name, email, Instant.now())
        return users.save(u)
    }
    fun get(id: UUID) = users.findById(id) ?: throw NotFoundException("user not found")
    fun list(q: String?, page: Int, size: Int) = users.search(q, page, size)
    fun delete(id: UUID) = users.deleteById(id)
    fun listBooks(id: UUID, page: Int, size: Int) = users.listBooks(id, page, size)
}

@Service
class BookService(private val books: BookRepository) {
    fun create(title: String, author: String, isbn: String, year: Int?) : Book {
        if (books.findByIsbn(isbn) != null) throw ConflictException("isbn already exists")
        val b = Book(UUID.randomUUID(), title, author, isbn, year, Instant.now())
        return books.save(b)
    }
    fun get(id: UUID) = books.findById(id) ?: throw NotFoundException("book not found")
    fun list(q: String?, isbn: String?, page: Int, size: Int) = books.search(q, isbn, page, size)
    fun delete(id: UUID) = books.deleteById(id)
    fun listUsers(bookId: UUID, page: Int, size: Int) = books.listUsers(bookId, page, size)
}

@Service
class AssignmentService(
    private val users: UserRepository,
    private val books: BookRepository,
    private val links: UserBookRepository
) {
    fun assign(userId: UUID, bookId: UUID) {
        users.findById(userId) ?: throw NotFoundException("user not found")
        books.findById(bookId) ?: throw NotFoundException("book not found")
        if (links.exists(userId, bookId)) throw ConflictException("already assigned")
        links.assign(userId, bookId)
    }
    fun unassign(userId: UUID, bookId: UUID) {
        users.findById(userId) ?: throw NotFoundException("user not found")
        books.findById(bookId) ?: throw NotFoundException("book not found")
        links.unassign(userId, bookId)
    }
}
