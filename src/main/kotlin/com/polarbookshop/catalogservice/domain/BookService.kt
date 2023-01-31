package com.polarbookshop.catalogservice.domain

import org.springframework.stereotype.Service

@Service
class BookService(private val repository: BookRepository) {

    fun viewBookList(): List<Book> = repository.findAll()

    fun viewBookDetails(isbn: String): Book = repository.findByIsbn(isbn) ?: throw BookNotFoundException(isbn)

    fun addBookToCatalog(book: Book): Book {
        if (repository.existsByIsbn(book.isbn)) throw BookAlreadyExistsException(book.isbn)
        return repository.save(book)
    }

    fun removeBookFromCatalog(isbn: String) = repository.deleteByIsbn(isbn)

    fun editBookDetails(isbn: String, book: Book): Book {
        val existingBook = repository.findByIsbn(isbn)
        return if (existingBook != null) {
            repository.save(
                existingBook.copy(
                    title = book.title,
                    author = book.author,
                    price = book.price
                )
            )
        } else {
            addBookToCatalog(book)
        }
    }
}