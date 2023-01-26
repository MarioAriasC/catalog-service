package com.polarbookshop.catalogservice.domain

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class BookServiceTest {
    @Mock
    private lateinit var repository: BookRepository

    @InjectMocks
    private lateinit var service: BookService

    @Test
    fun `when book to create already exists then throws`() {
        val bookIsbn = "1234561232"
        val bookToCreate = Book(bookIsbn, "Title", "Author", 9.90)
        `when`(repository.existsByIsbn(bookIsbn)).thenReturn(true)
        assertThatThrownBy { service.addBookToCatalog(bookToCreate) }
            .isInstanceOf(BookAlreadyExistsException::class.java)
            .hasMessage("A book with ISBN $bookIsbn already exists.")
    }

    @Test
    fun `when book to read does not exists then throws`() {
        val bookIsbn = "1234561232"
        `when`(repository.findByIsbn(bookIsbn)).thenReturn(null)
        assertThatThrownBy { service.viewBookDetails(bookIsbn) }
            .isInstanceOf(BookNotFoundException::class.java)
            .hasMessage("The book with ISBN $bookIsbn was not found.")
    }
}