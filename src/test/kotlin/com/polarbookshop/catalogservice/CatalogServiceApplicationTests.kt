package com.polarbookshop.catalogservice

import com.polarbookshop.catalogservice.domain.Book
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `when get request with id then book returned`() {
        val bookIsbn = "1231231230"
        val bookToCreate = Book(bookIsbn, "Title", "Author", 9.90, "Polarsophia")
        val expectedBook = webTestClient
            .post()
            .uri("/books")
            .bodyValue(bookToCreate)
            .exchange()
            .expectStatus().isCreated
            .expectBody<Book>().value { book -> assertThat(book).isNotNull() }
            .returnResult().responseBody as Book

        webTestClient
            .get()
            .uri("/books/$bookIsbn")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody<Book>().value { actualBook ->
                assertThat(actualBook).isNotNull
                assertThat(actualBook.isbn).isEqualTo(expectedBook.isbn)
            }
    }

    @Test
    fun `when post request then book created`() {
        val expectedBook = Book("1231231231", "Title", "Author", 9.90, "Polarsophia")
        webTestClient
            .post()
            .uri("/books")
            .bodyValue(expectedBook)
            .exchange()
            .expectStatus().isCreated
            .expectBody<Book>().value { actualBook ->
                assertThat(actualBook).isNotNull
                assertThat(actualBook.isbn).isEqualTo(expectedBook.isbn)
            }
    }

    @Test
    fun `when put request then book updated`() {
        val bookIsbn = "1231231232"
        val bookToCreate = Book(bookIsbn, "Title", "Author", 9.90, "Polarsophia")
        val createdBook = webTestClient
            .post()
            .uri("/books")
            .bodyValue(bookToCreate)
            .exchange()
            .expectStatus().isCreated
            .expectBody<Book>().value { book -> assertThat(book).isNotNull() }
            .returnResult().responseBody as Book

        val bookToUpdate = createdBook.copy(price = 7.95)

        webTestClient
            .put()
            .uri("/books/$bookIsbn")
            .bodyValue(bookToUpdate)
            .exchange()
            .expectStatus().isOk
            .expectBody<Book>().value { actualBook ->
                assertThat(actualBook).isNotNull
                assertThat(actualBook.price).isEqualTo(bookToUpdate.price)
            }
    }

    @Test
    fun `when delete request then book deleted`() {
        val bookIsbn = "1231231233"
        val bookToCreate = Book(bookIsbn, "Title", "Author", 9.90, "Polarsophia")
        with(webTestClient) {
            post()
                .uri("/books")
                .bodyValue(bookToCreate)
                .exchange()
                .expectStatus().isCreated

            delete()
                .uri("/books/$bookIsbn")
                .exchange()
                .expectStatus().isNoContent

            get()
                .uri("/books/$bookIsbn")
                .exchange()
                .expectStatus().isNotFound
                .expectBody<String>().value { errorMessage ->
                    assertThat(errorMessage).isEqualTo("The book with ISBN $bookIsbn was not found.")
                }
        }
    }


}
