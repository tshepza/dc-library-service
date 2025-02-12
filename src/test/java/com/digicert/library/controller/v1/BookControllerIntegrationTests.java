
package com.digicert.library.controller.v1;
import com.digicert.library.api.exceptions.ErrorResponse;
import com.digicert.library.dto.Book;
import com.digicert.library.dto.BookCollection;
import com.digicert.library.model.BookEntity;
import com.digicert.library.respository.BooksRepository;
import org.junit.Before;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * This full integration test
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerIntegrationTests {

    private final String URI = "/api/v1/books/";
    private final String HOST = "http://localhost:";

    @LocalServerPort
    private int LOCAL_PORT;

    @Autowired
    BooksRepository booksRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void init(){
        booksRepository.save(new BookEntity(null,"Java17","111111","NON FICTION","Joe",320,"Tom"));

        booksRepository.save(new BookEntity(null,"Journey to the Unknown","222222","SCIENCE FICTION","Future Books",120,"Smith"));

        booksRepository.save(new BookEntity(null,"Java22","33423","TECH","Mokone",330,"Olerato"));

        booksRepository.save(new BookEntity(null,"Ghost City","442344","FICTION","Piet",210,"Olerato"));

    }


    @Test
    void shouldReturnAllBooksForWhenNoFilter() throws Exception {
        String hostUrl = HOST + LOCAL_PORT + URI;

        ResponseEntity<BookCollection> response = restTemplate.exchange(
                hostUrl , HttpMethod.GET, null, BookCollection.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTotalBooksFound()).isEqualTo(6);
    }

    @Test
    void shouldReturn404ForInvalidBookTittle() throws Exception {
        String byBookTitle = "byTitle/java for dummies";
        String hostUrl = HOST + LOCAL_PORT + URI;

        ResponseEntity<Book> response = restTemplate.exchange(
                hostUrl + byBookTitle, HttpMethod.GET, null, Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnBookDetailsForValidBookIsbn() throws Exception {
        String byBookIsbn = "byIsbn/222222";
        String hostUrl = HOST + LOCAL_PORT + URI;

        ResponseEntity<Book> response = restTemplate.exchange(
                hostUrl + byBookIsbn, HttpMethod.GET, null, Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("Journey to the Unknown");
        assertThat(response.getBody().getCategory()).isEqualTo("SCIENCE FICTION");
        assertThat(response.getBody().getPublisher()).isEqualTo("Future Books");
        assertThat(response.getBody().getNumberOfPages()).isEqualTo(120);
        assertThat(response.getBody().getAuthor()).isEqualTo("Smith");
    }

    @Order(4)
    @Test
    void shouldReturnBookDetailsForValidBookTittle() throws Exception {
        String byBookTitle = "byTitle/Java17";
        String hostUrl = HOST + LOCAL_PORT + URI;

        ResponseEntity<Book> response = restTemplate.exchange(
                hostUrl + byBookTitle, HttpMethod.GET, null, Book.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getIsbn()).isEqualTo("111111");
        assertThat(response.getBody().getCategory()).isEqualTo("NON FICTION");
        assertThat(response.getBody().getPublisher()).isEqualTo("Joe");
        assertThat(response.getBody().getNumberOfPages()).isEqualTo(320);
        assertThat(response.getBody().getAuthor()).isEqualTo("Tom");

    }




    @Test
    void shouldReturnZeroBooksForWhenAuthorIsValid() throws Exception {
        String filterByAuthor = "?author=Olerato";
        String hostUrl = HOST + LOCAL_PORT + URI;

        ResponseEntity<BookCollection> response = restTemplate.exchange(
                hostUrl + filterByAuthor, HttpMethod.GET, null, BookCollection.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTotalBooksFound()).isEqualTo(2);
    }


    @Test
    void shouldReturnZeroBooksForWhenAuthorIsInvalid() throws Exception {
        String filterByAuthor = "?author=Joe2";
        String hostUrl = HOST + LOCAL_PORT + URI;

        ResponseEntity<BookCollection> response = restTemplate.exchange(
                hostUrl + filterByAuthor, HttpMethod.GET, null, BookCollection.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTotalBooksFound()).isZero();
    }



    @Test
    void shouldReturnAllBooksForAuthorWhenFilterByAuthor() throws Exception {
        String hostUrl = HOST + LOCAL_PORT + URI;
        String filterByAuthor = "?author=Olerato";

        ResponseEntity<BookCollection> response = restTemplate.exchange(
                hostUrl + filterByAuthor , HttpMethod.GET, null, BookCollection.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTotalBooksFound()).isEqualTo(2);
    }


    @Test
    void whenPostBook_thenShouldReturnSavedBook() throws Exception {
        String hostUrl = HOST + LOCAL_PORT + URI;
        Book book = new Book(null, "Java 17","2222","Non Fiction","Jerry",320,"PSmith");

        ResponseEntity<Book> responseBook = restTemplate.postForEntity(hostUrl, book, Book.class);
        assertThat(responseBook.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseBook.getBody().getIsbn()).isEqualTo("2222");
        assertThat(responseBook.getBody().getCategory()).isEqualTo("Non Fiction");
        assertThat(responseBook.getBody().getPublisher()).isEqualTo("Jerry");
        assertThat(responseBook.getBody().getNumberOfPages()).isEqualTo(320);
        assertThat(responseBook.getBody().getAuthor()).isEqualTo("PSmith");

    }

    @Test
    void whenPostBookThatAlreadyExists_thenShouldReturnBadRequestWithError() throws Exception {
        String hostUrl = HOST + LOCAL_PORT + URI;

        Book book = new Book(null, "Java 17","9999968686","Non Fiction","Jerry",320,"PSmith");

        ResponseEntity<Book> responseBook = restTemplate.postForEntity(hostUrl, book, Book.class);
        assertThat(responseBook.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<ErrorResponse> responseError = restTemplate.postForEntity(hostUrl, book, ErrorResponse.class);
        assertThat(responseError.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseError.getBody().getError().size()).isEqualTo(1);
        assertThat(responseError.getBody().getError().get(0)).isEqualTo("The book with the same isbn already exist");

    }

    @Test
    void whenDeleteBookThatDoesNotExists_thenShouldReturnNOT_FOUND() throws Exception {
        String byId ="/{id}";
        String hostUrl = HOST + LOCAL_PORT + URI;

        ResponseEntity<Void> response = restTemplate.exchange(
                hostUrl+byId, HttpMethod.DELETE, null, Void.class, 9099L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void whenDeleteBookThatDoesNotExists_thenShouldReturnNO_CONTENT() throws Exception {
        String byId ="/{id}";
        String hostUrl = HOST + LOCAL_PORT + URI;



        Book book = new Book(null, "Microservice","9898","Non Fiction","Jerry",320,"PSmith");

        ResponseEntity<Book> responseBook = restTemplate.postForEntity(hostUrl, book, Book.class);
        assertThat(responseBook.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Long newlyCreatedBookId = responseBook.getBody().getId();

        ResponseEntity<Void> response = restTemplate.exchange(
                hostUrl+byId, HttpMethod.DELETE, null, Void.class, newlyCreatedBookId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    void whenUpateBook_thenShouldReturnUpdateBook() throws Exception {
        String byId ="/{id}";
        String hostUrl = HOST + LOCAL_PORT + URI;

        Book book = new Book(null, "Microservice","9898","Non Fiction","Jerry",320,"PSmith");
        ResponseEntity<Book> responseBook = restTemplate.postForEntity(hostUrl, book, Book.class);

        assertThat(responseBook.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long newlyCreatedBookId = responseBook.getBody().getId();

        Book updatedBook = responseBook.getBody();
        updatedBook.setTitle("Microservice Advanced");


        ResponseEntity<Book> responseUpdated = restTemplate.exchange(
                hostUrl+byId, HttpMethod.PUT, new HttpEntity<>(updatedBook), Book.class, newlyCreatedBookId);

        assertThat(responseUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseUpdated.getBody().getTitle()).isEqualTo("Microservice Advanced");
        assertThat(responseUpdated.getBody().getIsbn()).isEqualTo("9898");

   }




    @Test
    void whenPostBookWithMissingMandatoryFields_thenShouldReturnBadRequest() throws Exception {
        String hostUrl = HOST + LOCAL_PORT + URI;
        Book book = new Book(null, "Java 17",null,"Non Fiction","Jerry",320,"PSmith");

        ResponseEntity<Book> responseBook = restTemplate.postForEntity(hostUrl, book, Book.class);
        assertThat(responseBook.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


}

