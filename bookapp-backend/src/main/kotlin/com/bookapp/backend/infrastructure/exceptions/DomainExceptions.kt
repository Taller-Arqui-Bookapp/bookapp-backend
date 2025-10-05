package com.bookapp.backend.infrastructure.exceptions

class NotFoundException(msg: String): RuntimeException(msg)
class ConflictException(msg: String): RuntimeException(msg)
