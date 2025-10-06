package com.bookapp.backend.adapters.out.jpa.adapter

import com.bookapp.backend.adapters.out.jpa.entity.*
import com.bookapp.backend.adapters.out.jpa.repo.*
import com.bookapp.backend.domain.model.*
import com.bookapp.backend.domain.ports.*
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class UserRepositoryJpaAdapter(
    private val userJpa: UserJpa,
    private val bookJpa: BookJpa,
    private val linkJpa: UserBookJpa
) : UserRepository {

    override fun save(user: User): User =
        userJpa.save(user.toEntity()).toDomain()

    override fun findById(id: UUID): User? = userJpa.findById(id).orElse(null)?.toDomain()

    override fun findByEmail(email: String): User? = userJpa.findByEmail(email)?.toDomain()

    override fun search(q: String?, page: Int, size: Int): Pair<List<User>, Long> {
        val p = PageRequest.of(page, size)
        val res = if (q.isNullOrBlank())
            userJpa.findAll(p)
        else userJpa.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(q, q, p)
        return Pair(res.content.map { it.toDomain() }, res.totalElements)
    }

    override fun deleteById(id: UUID) = userJpa.deleteById(id)

    override fun listBooks(userId: UUID, page: Int, size: Int): Pair<List<Book>, Long> {
        val p = PageRequest.of(page, size)
        val links = linkJpa.findAllByIdUserId(userId, p)
        val books = bookJpa.findAllById(links.content.map { it.id.bookId!! }.toSet())
        return Pair(books.map { it.toDomain() }, links.totalElements)
    }
}

@Component
class BookRepositoryJpaAdapter(
    private val bookJpa: BookJpa,
    private val userJpa: UserJpa,
    private val linkJpa: UserBookJpa
) : BookRepository {

    override fun save(book: Book): Book = bookJpa.save(book.toEntity()).toDomain()

    override fun findById(id: UUID): Book? = bookJpa.findById(id).orElse(null)?.toDomain()

    override fun findByIsbn(isbn: String): Book? = bookJpa.findByIsbn(isbn)?.toDomain()

    override fun search(q: String?, isbn: String?, page: Int, size: Int): Pair<List<Book>, Long> {
        val p = PageRequest.of(page, size)
        val res = when {
            !isbn.isNullOrBlank() -> bookJpa.findByIsbnContaining(isbn, p)
            !q.isNullOrBlank()    -> bookJpa.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(q, q, p)
            else                  -> bookJpa.findAll(p)
        }
        return Pair(res.content.map { it.toDomain() }, res.totalElements)
    }

    override fun deleteById(id: UUID) = bookJpa.deleteById(id)

    override fun listUsers(bookId: UUID, page: Int, size: Int): Pair<List<User>, Long> {
        val p = PageRequest.of(page, size)
        val links = linkJpa.findAllByIdBookId(bookId, p)
        val users = userJpa.findAllById(links.content.map { it.id.userId!! }.toSet())
        return Pair(users.map { it.toDomain() }, links.totalElements)
    }
}

@Component
class UserBookRepositoryJpaAdapter(private val linkJpa: UserBookJpa) : UserBookRepository {
    override fun assign(userId: UUID, bookId: UUID) {
        linkJpa.save(UserBookEntity(UserBookId(userId, bookId), Instant.now()))
    }
    override fun unassign(userId: UUID, bookId: UUID) {
        linkJpa.deleteById(UserBookId(userId, bookId))
    }
    override fun exists(userId: UUID, bookId: UUID) = linkJpa.existsById(UserBookId(userId, bookId))
}
