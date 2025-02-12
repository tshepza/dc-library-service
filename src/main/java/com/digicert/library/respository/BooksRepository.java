package com.digicert.library.respository;

import com.digicert.library.model.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.domain.Pageable;


public interface BooksRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findBookEntityByIsbn(String isbn);

    Optional<BookEntity> findBookEntityByTitle(String title);

    Page<BookEntity> findBooksByAuthor(String author,Pageable pageable);
    Page<BookEntity> findAll(Pageable pageable);

}
