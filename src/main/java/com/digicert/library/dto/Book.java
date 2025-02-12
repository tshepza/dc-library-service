package com.digicert.library.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book implements Serializable {
    private Long id;
    @NotBlank(message = "Book title is mandatory")
    private String title;
    @NotBlank(message = "Book ISBN is mandatory")
    private String isbn;
    private String category;
    private String publisher;
    private int numberOfPages;
    private String author;

}
