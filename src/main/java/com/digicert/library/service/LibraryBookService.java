package com.digicert.library.service;

import com.digicert.library.dto.Book;
import com.digicert.library.dto.BookCollection;


public interface LibraryBookService {

    BookCollection getBooksByAuthor(String author, Integer offset, Integer limit);

    BookCollection getAllBooks(Integer offset, Integer limit);

    Book getBookByIsbn(String isbn);

    Book getBookByTitle(String title);

    Book addBook(Book book);
    Book updateBook(Book book);
    void deleteBook(Long bookId);


}
