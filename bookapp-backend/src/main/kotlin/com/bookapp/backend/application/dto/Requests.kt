package com.bookapp.backend.application.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Max

data class CreateUserRequest(
    @field:NotBlank
    @field:Size(min = 1, max = 100)
    val name: String,

    @field:NotBlank
    @field:Email
    val email: String
)

data class CreateBookRequest(
    @field:NotBlank val title: String,
    @field:NotBlank val author: String,
    @field:NotBlank val isbn: String,
    @field:Min(1400) @field:Max(2100) val publishedYear: Int?
)
