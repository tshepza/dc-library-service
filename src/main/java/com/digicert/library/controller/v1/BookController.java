package com.digicert.library.controller.v1;

import com.digicert.library.api.exceptions.BadRequestException;
import com.digicert.library.dto.Book;
import com.digicert.library.dto.BookCollection;
import com.digicert.library.service.LibraryBookService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "Library Service Application")
@RestController
@RequestMapping("/api/v1/books/")
public class BookController {


    private LibraryBookService libraryBookService;

    @Autowired
    public BookController(LibraryBookService libraryBookService) {
        this.libraryBookService = libraryBookService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookCollection> getAllBooks(@RequestParam(value = "author", required = false) String author,
                                      @RequestParam(value = "limit", required = false,defaultValue = "10") Integer limit ,
                                      @RequestParam(value = "offset", required = false,defaultValue = "0") Integer offset){


        BookCollection collection;

        //Page size must not be less than one
        limit = limit < 1 ? 1:limit;

        if ( author != null) {
            collection = libraryBookService.getBooksByAuthor(author,offset,limit);
        } else{
            collection = libraryBookService.getAllBooks(offset,limit);
        }


        return  ResponseEntity.ok(collection);
    }

    @GetMapping(value = "byTitle/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBookByTitle(@PathVariable( value = "title") String title) {

        if(Strings.isBlank(title)){
            throw new BadRequestException("Book title is required",title);
        }

        return  ResponseEntity.ok(libraryBookService.getBookByTitle(title));
    }

    @GetMapping(value = "byIsbn/{isbn}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBookByIsbn(@PathVariable( value = "isbn") String isbn) {


        if(Strings.isBlank(isbn)){
            throw new BadRequestException("Book isbn is required",isbn);
        }

        return new ResponseEntity<>(libraryBookService.getBookByIsbn(isbn), HttpStatus.OK);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book){

        return new ResponseEntity<>(libraryBookService.addBook(book), HttpStatus.CREATED);
    }


    @PutMapping("{id}")
    public ResponseEntity<Book> updateBook(@Valid @RequestBody Book book,@PathVariable("id") Long bookId){

        if(bookId == null ){
            throw new BadRequestException("Book id is required","n/a");
        }

        book.setId(bookId);

        return new ResponseEntity<>(libraryBookService.updateBook(book), HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable("id") Long bookId){

        libraryBookService.deleteBook(bookId);

        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
