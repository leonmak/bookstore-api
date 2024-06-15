package com.example.bookstore.mapper;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import jakarta.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookDtoMapper implements Converter<BookDto, Book> {
    private final AuthorDtoMapper authorDtoMapper;

    public BookDtoMapper(AuthorDtoMapper authorDtoMapper) {
        this.authorDtoMapper = authorDtoMapper;
    }

    @Override
    @Nonnull
    public Book convert(BookDto bookDto) {
        List<Author> authors = bookDto.authors() == null ? List.of() : bookDto.authors().stream()
                .map(authorDtoMapper::convert)
                .toList();
        Book book = new Book(
                bookDto.isbn(), bookDto.title(), authors,
                bookDto.year(), bookDto.price(), bookDto.genre());
        return book;
    }
}
