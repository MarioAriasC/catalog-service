package com.polarbookshop.catalogservice.domain

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import java.time.Instant

data class Book(
    @field:Id
    val id: Long? = null,

    @field:NotBlank(message = "The book ISBN must be defined.")
    @field:Pattern(
        regexp = "^([0-9]{10}|[0-9]{13})$",
        message = "The ISBN format must be valid."
    )
    val isbn: String,

    @field:NotBlank(message = "The book title must be defined.")
    val title: String,

    @field:NotBlank(message = "The book author must be defined.")
    val author: String,

    @field:NotNull(message = "The book price must be defined.")
    @field:Positive(message = "The book price must be greater than zero.")
    val price: Double?,

    val publisher: String,

    @field:CreatedDate
    val createdDate: Instant? = null,

    @field:LastModifiedDate
    val lastModifiedDate: Instant? = null,

    @field:Version
    val version: Int = 0
) {
    companion object {
        operator fun invoke(isbn: String, title: String, author: String, price: Double?, publisher: String): Book =
            Book(isbn = isbn, title = title, author = author, price = price, publisher = publisher)
    }
}

class BookNotFoundException(isbn: String) : RuntimeException("The book with ISBN $isbn was not found.")
class BookAlreadyExistsException(isbn: String) : RuntimeException("A book with ISBN $isbn already exists.")