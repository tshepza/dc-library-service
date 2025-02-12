package com.digicert.library.mapper;

import com.digicert.library.dto.Book;
import com.digicert.library.model.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book toDTO(BookEntity bookEntity);
    BookEntity toEntity(Book book);
}
