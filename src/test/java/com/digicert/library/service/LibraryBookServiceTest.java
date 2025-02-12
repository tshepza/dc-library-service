
package com.digicert.library.service;

import com.digicert.library.api.exceptions.ResourceNotFoundException;
import com.digicert.library.dto.Book;
import com.digicert.library.dto.BookCollection;
import com.digicert.library.mapper.BookMapper;
import com.digicert.library.model.BookEntity;
import com.digicert.library.respository.BooksRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class LibraryBookServiceTest {
    @Mock
    private BooksRepository booksRepository;
    @InjectMocks
    private LibraryBookService libraryBookService =new LibraryBookServiceImpl(booksRepository);

    private BookEntity bookEntity;

    private Book book;

    private BookCollection bookCollection;

    @BeforeEach
    public void setUp(){
        bookEntity = new BookEntity(999L,"Java 17","2323","NON FICTION","Joe",320,"Tom");
        book = new Book(888L,"Spring Boot","2020","NON FICTION","Joe",120,"Tom");

        bookCollection = new BookCollection(1, List.of(book));
    }




/*
    @Test
    void shouldReturnBooks(){
        BookEntity book2=  new BookEntity(988L,"Java 24","4343","NON FICTION","Joe",20,"Mike");
        List<BookEntity> books = Lists.list(bookEntity,book2);
        when(booksRepository.findAll()).thenReturn(books);

        BookCollection result = libraryBookService.getAllBooks(0,2);
        assertNotNull(result);
        assertEquals(2,result.getTotalBooksFound());
    }
*/


    @Test
    void shouldReturnBookByTitleWhenBookExist(){
        when(booksRepository.findBookEntityByTitle("Java 17")).thenReturn(Optional.of(bookEntity));

        Book result = libraryBookService.getBookByTitle("Java 17");

        assertNotNull(result);
        assertEquals("Java 17",result.getTitle());

    }

    @Test
    void shouldThrowResourceNotFoundWhenBookDoesNotExist(){
        assertThrows(ResourceNotFoundException.class, () -> libraryBookService.getBookByIsbn("90909"));
    }

    @Test
    void testCreateBookGiven() {

        given(booksRepository.save(bookEntity)).willReturn(bookEntity);

        Book savedBook = libraryBookService.addBook(BookMapper.INSTANCE.toDTO(bookEntity));

        assertNotNull(savedBook);
        assertEquals("Java 17", savedBook.getTitle());
        verify(booksRepository).save(bookEntity);

    }


    @Test
    void testUpdateBook_WhenBookExists() {

        given(booksRepository.findById(bookEntity.getId())).willReturn(Optional.of(bookEntity));
        bookEntity.setTitle("Java Exceptions");
        given(booksRepository.save(bookEntity)).willReturn(bookEntity);
        Book updatedBook = libraryBookService.updateBook(BookMapper.INSTANCE.toDTO(bookEntity));

        assertThat(updatedBook.getTitle()).isEqualTo("Java Exceptions");

        verify(booksRepository).save(bookEntity);

    }

    @Test
    void testDeleteBook_WhenBookDoesNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> libraryBookService.deleteBook(976L));
    }


    @Test
    void testUpdateBook_WhenBookNotFound() throws Exception{
        assertThrows(ResourceNotFoundException.class, () -> libraryBookService.updateBook(book));
    }

}

