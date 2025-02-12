package com.digicert.library.service;

import com.digicert.library.api.exceptions.BadRequestException;
import com.digicert.library.api.exceptions.InternalServerException;
import com.digicert.library.api.exceptions.ResourceNotFoundException;
import com.digicert.library.dto.Book;
import com.digicert.library.dto.BookCollection;
import com.digicert.library.mapper.BookMapper;
import com.digicert.library.model.BookEntity;
import com.digicert.library.respository.BooksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LibraryBookServiceImpl implements LibraryBookService{

    private BooksRepository booksRepository;

    @Autowired
    public LibraryBookServiceImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public BookCollection getBooksByAuthor(String author, Integer offset, Integer limit) {
        Page<BookEntity> bookList ;
        Pageable pageable = PageRequest.of(offset, limit);

        try {
            bookList = booksRepository.findBooksByAuthor(author,pageable);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerException("Unknown exception occurred while getting  books ",author);
        }
        return getBookCollection(bookList.getContent());
    }

    @Override
    public BookCollection getAllBooks(Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);

        Page<BookEntity> bookList;
        try {
            bookList = booksRepository.findAll(pageable);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerException("Unknown exception occurred while getting  books ",null);
        }

        return getBookCollection(bookList.getContent());

    }

    private static BookCollection getBookCollection(List<BookEntity> bookList) {
        List<Book> books;
        BookCollection collection = new BookCollection();
        collection.setTotalBooksFound(bookList.size());
        if(!bookList.isEmpty()){
            books = new ArrayList<>();
            bookList.stream().map(BookMapper.INSTANCE::toDTO).forEach(books::add);
            collection.getBooks().addAll(books);
        }

        return collection;
    }

    @Override
    public Book getBookByTitle(String bookTitle) {
        Optional<BookEntity> response;
        try{
           response = booksRepository.findBookEntityByTitle(bookTitle);
       }catch (Exception e){
            log.error(e.getMessage());
           throw new InternalServerException("Unknown exception occurred while getting a book ",bookTitle);
       }

        if(!response.isPresent()){
            throw new ResourceNotFoundException("Book not found",bookTitle);
        }

        return BookMapper.INSTANCE.toDTO(response.get());
    }
    @Override
    public Book getBookByIsbn(String isbn) {
        Optional<BookEntity> response;
        try{
            response = booksRepository.findBookEntityByIsbn(isbn);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerException("Unknown exception occurred while getting a book ",isbn);
        }

        if(!response.isPresent()){
            throw new ResourceNotFoundException("Book not found",isbn);
        }

        return BookMapper.INSTANCE.toDTO(response.get());
    }

    @Override
    public Book addBook(Book book) {
        BookEntity bookEntity;
        try {
            bookEntity = booksRepository.save(BookMapper.INSTANCE.toEntity(book));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("The book with the same isbn already exist",book.toString());
        } catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerException("Unknown exception occurred while adding a book ",book.toString());
        }

        return BookMapper.INSTANCE.toDTO(bookEntity);
    }

    @Override
    public Book updateBook(Book book) {


        Optional<BookEntity> oldBookEntity;
        try{
            oldBookEntity= booksRepository.findById(book.getId());
            if(oldBookEntity.isPresent()){
                BookEntity updateBooEntity =BookMapper.INSTANCE.toEntity(book);
                booksRepository.save(updateBooEntity);            }
        }catch (Exception ex){
            log.error(ex.getMessage());
            throw new InternalServerException("Unknown exception occurred while updating a book ",book.toString());
        }

        if(!oldBookEntity.isPresent()){
            throw  new ResourceNotFoundException("Book not found for Id " + book.getId() ,book.toString());
        }


        return book;
    }

    @Override
    public void deleteBook(Long bookId) {
        Optional<BookEntity> bookEntity;
        try{
            bookEntity= booksRepository.findById(bookId);
            if(bookEntity.isPresent()){
                booksRepository.delete(bookEntity.get());
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
            throw new InternalServerException("Unknown exception occurred while deleting a book ",bookId.toString());
        }

        if(!bookEntity.isPresent()){
            throw  new ResourceNotFoundException("Book not found for Id " + bookId,bookId.toString());
        }

    }
}
