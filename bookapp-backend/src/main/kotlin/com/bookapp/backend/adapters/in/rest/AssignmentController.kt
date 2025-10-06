package com.bookapp.backend.adapters.`in`.rest

import com.bookapp.backend.application.AssignmentService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/users/{userId}/books/{bookId}")
class AssignmentController(private val service: AssignmentService) {

    @PostMapping(produces = ["application/xml", "application/json"])
    @ResponseStatus(HttpStatus.CREATED)
    fun assign(@PathVariable userId: UUID, @PathVariable bookId: UUID) =
        service.assign(userId, bookId)

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unassign(@PathVariable userId: UUID, @PathVariable bookId: UUID) =
        service.unassign(userId, bookId)
}
