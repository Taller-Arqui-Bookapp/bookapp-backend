package com.bookapp.backend.application.dto

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserRequest(
    @field:NotBlank @field:Size(min = 1, max = 100) val name: String,
    @field:NotBlank @field:Email val email: String
)

data class CreateBookRequest(
    @field:NotBlank val title: String,
    @field:NotBlank val author: String,
    @field:NotBlank val isbn: String,
    val publishedYear: Int?
)

data class ImportSelectedBooksRequest(
    val books: List<ExternalBookSelection>
)

data class ExternalBookSelection(
    val title: String,
    val authors: List<String>,
    val isbn: String?,
    val publishedYear: Int?,
    val coverUrl: String?
)

data class BulkImportResponse(
    val imported: List<BookImportResult>,
    val errors: List<BookImportError>
)

data class BookImportResult(
    val selection: ExternalBookSelection,
    val importedBook: com.bookapp.backend.domain.model.Book,
    val wasAlreadyExists: Boolean
)

data class BookImportError(
    val selection: ExternalBookSelection,
    val error: String
)

/**
 * Para XML, indicamos el wrapper de la lista y el nombre de cada ítem.
 * Usamos "item" genérico para que sirva con User y Book.
 */
@JacksonXmlRootElement(localName = "Page")
data class PageResponse<T>(
    @field:JacksonXmlElementWrapper(localName = "content")
    @field:JacksonXmlProperty(localName = "item")
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
