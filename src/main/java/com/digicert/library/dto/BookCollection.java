package com.digicert.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCollection {

    private int totalBooksFound;
    private List<Book> books = new ArrayList<>();
}
