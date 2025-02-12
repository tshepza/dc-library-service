package com.digicert.library.mapper;

import com.digicert.library.dto.Book;
import com.digicert.library.model.BookEntity;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-12T23:27:26+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Ubuntu)"
)
public class BookMapperImpl implements BookMapper {

    @Override
    public Book toDTO(BookEntity bookEntity) {
        if ( bookEntity == null ) {
            return null;
        }

        Book book = new Book();

        book.setId( bookEntity.getId() );
        book.setTitle( bookEntity.getTitle() );
        book.setIsbn( bookEntity.getIsbn() );
        book.setCategory( bookEntity.getCategory() );
        book.setPublisher( bookEntity.getPublisher() );
        book.setNumberOfPages( bookEntity.getNumberOfPages() );
        book.setAuthor( bookEntity.getAuthor() );

        return book;
    }

    @Override
    public BookEntity toEntity(Book book) {
        if ( book == null ) {
            return null;
        }

        BookEntity bookEntity = new BookEntity();

        bookEntity.setId( book.getId() );
        bookEntity.setTitle( book.getTitle() );
        bookEntity.setIsbn( book.getIsbn() );
        bookEntity.setCategory( book.getCategory() );
        bookEntity.setPublisher( book.getPublisher() );
        bookEntity.setNumberOfPages( book.getNumberOfPages() );
        bookEntity.setAuthor( book.getAuthor() );

        return bookEntity;
    }
}
