package com.polarbookshop.catalogservice.domain

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class BookValidationTests {

    @Test
    fun `when all fields are correct then validation succeeds`() {
        val book = Book("1234567890", "Title", "Author", 9.90, "Polarsophia")
        val violations = validator.validate(book)
        assertThat(violations).isEmpty()
    }

    @Test
    fun `when isbn not defined then validation fails`() {
        val book = Book("", "Title", "Author", 9.90, "Polarsophia")
        val violations = validator.validate(book)!!
        assertThat(violations).hasSize(2)
        val messages = violations.map { violation -> violation.message }
        assertThat(messages)
            .contains("The book ISBN must be defined.")
            .contains("The ISBN format must be valid.")
    }

    @Test
    fun `when Isbn Defined But Incorrect Then Validation Fails`() {
        val book = Book("a234567890", "Title", "Author", 9.90, "Polarsophia")
        val violations = validator.validate(book)
        assertThat(violations).hasSize(1)
        assertThat(violations.iterator().next().message)
            .isEqualTo("The ISBN format must be valid.")
    }

    @Test
    fun whenTitleIsNotDefinedThenValidationFails() {
        val book = Book("1234567890", "", "Author", 9.90, "Polarsophia")
        val violations = validator.validate(book)
        assertThat(violations).hasSize(1)
        assertThat(violations.iterator().next().message)
            .isEqualTo("The book title must be defined.")
    }

    @Test
    fun whenAuthorIsNotDefinedThenValidationFails() {
        val book = Book("1234567890", "Title", "", 9.90, "Polarsophia")
        val violations = validator.validate(book)
        assertThat(violations).hasSize(1)
        assertThat(violations.iterator().next().message)
            .isEqualTo("The book author must be defined.")
    }

    @Test
    fun whenPriceIsNotDefinedThenValidationFails() {
        val book = Book("1234567890", "Title", "Author", null, "Polarsophia")
        val violations = validator.validate(book)
        assertThat(violations).hasSize(1)
        assertThat(violations.iterator().next().message)
            .isEqualTo("The book price must be defined.")
    }

    @Test
    fun whenPriceDefinedButZeroThenValidationFails() {
        val book = Book("1234567890", "Title", "Author", 0.0, "Polarsophia")
        val violations = validator.validate(book)
        assertThat(violations).hasSize(1)
        assertThat(violations.iterator().next().message)
            .isEqualTo("The book price must be greater than zero.")
    }

    @Test
    fun whenPriceDefinedButNegativeThenValidationFails() {
        val book = Book("1234567890", "Title", "Author", -9.90, "Polarsophia")
        val violations = validator.validate(book)
        assertThat(violations).hasSize(1)
        assertThat(violations.iterator().next().message)
            .isEqualTo("The book price must be greater than zero.")
    }

    companion object {
        lateinit var validator: Validator

        @JvmStatic
        @BeforeAll
        fun setup() {
            val factory = Validation.buildDefaultValidatorFactory()!!
            validator = factory.validator
        }
    }
}