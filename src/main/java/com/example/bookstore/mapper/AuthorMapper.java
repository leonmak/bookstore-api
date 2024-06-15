package com.example.bookstore.mapper;

import com.example.bookstore.dto.AuthorDto;
import com.example.bookstore.model.Author;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AuthorMapper implements Converter<Author, AuthorDto> {

    @Override
    public AuthorDto convert(Author source) {
        return new AuthorDto(source.getName(), source.getBirthday());
    }
}
