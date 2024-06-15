package com.example.bookstore.service;

import com.example.bookstore.dto.BookDto;

import java.util.List;

public interface BookService {

    void addBook(BookDto createRequest);

    void updateBook(String isbn, BookDto updateRequest);

    List<BookDto> fetchBooksByTitleAuthor(String title, String author);

    void deleteBook(String id);
}
