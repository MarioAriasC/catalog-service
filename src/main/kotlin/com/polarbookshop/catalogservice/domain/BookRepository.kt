package com.polarbookshop.catalogservice.domain

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

interface BookRepository {
    fun findAll(): List<Book>
    fun findByIsbn(isbn: String): Book?
    fun existsByIsbn(isbn: String): Boolean
    fun save(book: Book): Book
    fun deleteByIsbn(isbn: String)
}

@Repository
class InMemoryBookRepository : BookRepository {
    private val books = ConcurrentHashMap<String, Book>()

    override fun findAll(): List<Book> = books.values.toList()

    override fun findByIsbn(isbn: String): Book? = books[isbn]

    override fun existsByIsbn(isbn: String): Boolean = findByIsbn(isbn) != null

    override fun save(book: Book): Book {
        books[book.isbn] = book
        return book
    }

    override fun deleteByIsbn(isbn: String) {
        books.remove(isbn)
    }

}