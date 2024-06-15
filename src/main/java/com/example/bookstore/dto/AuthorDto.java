package com.example.bookstore.dto;

import com.example.bookstore.validator.ValidDateOfBirth;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record AuthorDto(
        @NotBlank
        String name,

        @Nullable @ValidDateOfBirth
        String birthday
) {
}
