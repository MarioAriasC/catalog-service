package com.polarbookshop.catalogservice.web

import com.polarbookshop.catalogservice.domain.Book
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant

@JsonTest
class BookJsonTests {
    @Autowired
    private lateinit var json: JacksonTester<Book>

    @Test
    fun serialize() {
        val now = Instant.now()
        val book = Book(394, "1234567890", "Title", "Author", 9.90, "Polarsophia", now, now, 21)
        val jsonContent = json.write(book)
        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
            .isEqualTo(book.id?.toInt())
        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
            .isEqualTo(book.isbn)
        assertThat(jsonContent).extractingJsonPathStringValue("@.title")
            .isEqualTo(book.title)
        assertThat(jsonContent).extractingJsonPathStringValue("@.author")
            .isEqualTo(book.author)
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
            .isEqualTo(book.price)
        assertThat(jsonContent).extractingJsonPathStringValue("@.publisher")
            .isEqualTo(book.publisher)
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdDate")
            .isEqualTo(book.createdDate.toString())
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedDate")
            .isEqualTo(book.lastModifiedDate.toString())
        assertThat(jsonContent).extractingJsonPathNumberValue("@.version")
            .isEqualTo(book.version)

    }

    @Test
    fun deserialize() {
        val instant = Instant.parse("2021-09-07T22:50:37.135029Z")
        val content = """
                {
                  "id": 394,
                  "isbn": "1234567890",
                  "title": "Title",
                  "author": "Author",
                  "price": 9.90,
                  "publisher": "Polarsophia",
                  "createdDate": "2021-09-07T22:50:37.135029Z",
                  "lastModifiedDate": "2021-09-07T22:50:37.135029Z",
                  "version": 21
                }
                
                """.trimIndent()
        assertThat(json.parse(content))
            .usingRecursiveComparison()
            .isEqualTo(Book(394L, "1234567890", "Title", "Author", 9.90, "Polarsophia", instant, instant, 21))
    }
}