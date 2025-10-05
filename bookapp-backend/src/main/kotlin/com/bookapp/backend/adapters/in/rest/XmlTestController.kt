package com.bookapp.backend.adapters.`in`.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class BookDTO(val title: String, val author: String)

@RestController
class XmlTestController {

    @GetMapping("/api/v1/test", produces = ["application/xml", "application/json"])
    fun test(): BookDTO = BookDTO("El alquimista", "Paulo Coelho")
}
