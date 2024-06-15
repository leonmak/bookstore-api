package com.example.bookstore.mapper;

import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.dto.BookDto;
import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper implements Converter<Book,BookDto> {
    private final AuthorMapper authorMapper;

    public BookMapper(AuthorMapper authorMapper) {
        this.authorMapper = authorMapper;
    }

    @Override
    public BookDto convert(Book book) {
        List<AuthorDto> authors = book.getAuthors().stream()
                .map(authorMapper::convert)
                .collect(Collectors.toList());
        return new BookDto(
                book.getIsbn(), book.getTitle(), authors,
                book.getYear(), book.getPrice(), book.getGenre());
    }
}
