package com.example.bookstore.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record BookDto(
        @NotBlank
        String isbn,

        @NotBlank
        String title,

        @Nullable @Valid
        List<AuthorDto> authors,

        @Min(0)
        Integer year,

        @Min(0)
        Double price,

        String genre
) {
}
