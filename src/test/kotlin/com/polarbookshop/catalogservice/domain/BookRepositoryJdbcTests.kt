package com.polarbookshop.catalogservice.domain

import com.polarbookshop.catalogservice.config.DataConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.test.context.ActiveProfiles

@DataJdbcTest
@Import(DataConfig::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")

class BookRepositoryJdbcTests {
    @Autowired
    private lateinit var repository: BookRepository

    @Autowired
    private lateinit var template: JdbcAggregateTemplate

    @Test
    fun `find all books`() {
        val book1 = Book("1234561235", "Title", "Author", 12.90, "Polarsophia")
        val book2 = Book("1234561236", "Another Title", "Author", 12.90, "Polarsophia")

        template.insert(book1)
        template.insert(book2)

        val actualBooks = repository.findAll()

        assertThat(actualBooks
            .filter { book -> book.isbn == book1.isbn || book.isbn == book2.isbn })
            .hasSize(2)
    }

    @Test
    fun `fund book by isbn when exists`() {
        val bookIsbn = "1234561237"
        val book = Book(bookIsbn, "Title", "Author", 12.90, "Polarsophia")
        template.insert(book)

        val actualBook = repository.findByIsbn(bookIsbn)
        assertThat(actualBook).isNotNull
        assertThat(actualBook!!.isbn).isEqualTo(book.isbn)
    }

    @Test
    fun `find book by isbn non existing`() {
        val actualBook = repository.findByIsbn("1234561238")
        assertThat(actualBook).isNull()
    }

    @Test
    fun `exists book by isbn when exists`() {
        val bookIsbn = "1234561237"
        val book = Book(bookIsbn, "Title", "Author", 12.90, "Polarsophia")
        template.insert(book)

        val existing = repository.existsByIsbn(bookIsbn)
        assertThat(existing).isTrue
    }

    @Test
    fun `exists book by isbn non existing`() {
        val existing = repository.existsByIsbn("1234561238")
        assertThat(existing).isFalse
    }

    @Test
    fun `delete book by isbn `() {
        val bookIsbn = "1234561237"
        val book = Book(bookIsbn, "Title", "Author", 12.90, "Polarsophia")
        val persistedBook = template.insert(book)

        repository.deleteByIsbn(bookIsbn)
        assertThat(template.findById(persistedBook.id!!, Book::class.java)).isNull()
    }
}