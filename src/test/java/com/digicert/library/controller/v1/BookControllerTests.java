
package com.digicert.library.controller.v1;

import com.digicert.library.dto.Book;
import com.digicert.library.dto.BookCollection;
import com.digicert.library.service.LibraryBookService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * This is to test the Croller only and is not doing the full integration test
 */
@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
class BookControllerTests {

    private final String URI = "/api/v1/books/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryBookService libraryBookService;

    @Test
    void shouldReturnBookByTitleWhenTitleIsValid() throws Exception {
        Book book = new Book(1L, "Java 17","2222","Non Fiction","Joe",320,"P Smith");
        Mockito.when(libraryBookService.getBookByTitle("Java 17")).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get(URI+"byTitle/Java 17"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java 17"))
                .andExpect(jsonPath("$.isbn").value("2222"))
                .andExpect(jsonPath("$.category").value("Non Fiction"))
                .andExpect(jsonPath("$.publisher").value("Joe"))
                .andExpect(jsonPath("$.author").value("P Smith"));
    }
    @Test
    void shouldReturnBookByIsbnWhenIsbnIsValid() throws Exception {
        Book book = new Book(2L, "Spring Boot","3333","Non Fiction","Tom",120,"P Tshabalala");
        Mockito.when(libraryBookService.getBookByIsbn("3333")).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get(URI+"byIsbn/3333"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring Boot"))
                .andExpect(jsonPath("$.isbn").value("3333"))
                .andExpect(jsonPath("$.category").value("Non Fiction"))
                .andExpect(jsonPath("$.publisher").value("Tom"))
                .andExpect(jsonPath("$.author").value("P Tshabalala"));
    }

    @Test
    void shouldReturn404WhenTitleMethodIsInvalid() throws Exception {
        Book book = new Book(1L, "Java 17","2222","Non Fiction","Joe",320,"P Smith");
        Mockito.when(libraryBookService.getBookByTitle("Java 17")).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get(URI+"byTittle/Java 17"))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldReturnAllBooksWithoutFilterWhenIsValid() throws Exception {
        BookCollection bc = new BookCollection();
        Book book1 = new Book(1L, "Java 17","2222","Non Fiction","Joe",320,"PSmith");

        List<Book> books = new ArrayList<>();
        books.add(book1);
        bc.setBooks(books);
        bc.setTotalBooksFound(books.size());

        Mockito.when(libraryBookService.getAllBooks(1,2)).thenReturn(bc);

        mockMvc.perform(MockMvcRequestBuilders.get(URI ))
                .andExpect(status().isOk());
    }

}
