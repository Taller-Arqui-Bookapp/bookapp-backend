package com.bookapp.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookappBackendApplication

fun main(args: Array<String>) {
	runApplication<BookappBackendApplication>(*args)
}
