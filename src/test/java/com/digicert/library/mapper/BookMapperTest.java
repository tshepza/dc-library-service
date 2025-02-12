
package com.digicert.library.mapper;

import com.digicert.library.dto.Book;
import com.digicert.library.model.BookEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

 class BookMapperTest {
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @Test
    void whenMappingBookToEntity_thenExpectCorrectMappingResult() {
        Book book = new Book(1L,"Java 17","2222","NON-FICTION","Joe",23, "Smith");

        BookEntity result = bookMapper.toEntity(book);

        assertThat(result.getTitle()).isEqualTo("Java 17");
        assertThat(result.getAuthor()).isEqualTo("Smith");
        assertThat(result.getIsbn()).isEqualTo("2222");
        assertThat(result.getCategory()).isEqualTo("NON-FICTION");
        assertThat(result.getNumberOfPages()).isEqualTo(23);
    }


    @Test
    void whenMappingBookEntityToDTO_thenExpectCorrectMappingResult() {
        BookEntity bookEntity = new BookEntity(1L,"Java 24","3333","NON-FICTION","Joe",230, "Smith");

        Book result = bookMapper.toDTO(bookEntity);

        assertThat(result.getTitle()).isEqualTo("Java 24");
        assertThat(result.getAuthor()).isEqualTo("Smith");
        assertThat(result.getIsbn()).isEqualTo("3333");
        assertThat(result.getCategory()).isEqualTo("NON-FICTION");
        assertThat(result.getNumberOfPages()).isEqualTo(230);
    }
}

