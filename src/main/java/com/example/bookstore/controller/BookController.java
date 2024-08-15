package com.example.bookstore.controller;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.exception.BookQueryFormatException;
import com.example.bookstore.service.ExternalApiService;
import com.example.bookstore.service.BookService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final ExternalApiService externalApiService;

    public BookController(BookService bookService, ExternalApiService externalApiService) {
        this.bookService = bookService;
        this.externalApiService = externalApiService;
    }

    @GetMapping
    @Operation(summary = "Fetch books", description = "Fetch books that match title and/or author.")
    public List<BookDto> fetchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author
    ) {
        if (StringUtils.isBlank(title) && StringUtils.isBlank(author)) {
            throw new BookQueryFormatException("Either title or author must be provided");
        }

        return bookService.fetchBooksByTitleAuthor(title, author);
    }

    @PostMapping
    @Operation(summary = "Add a book", description = "Creates a new book in the bookstore.")
    public void addBook(@Valid @RequestBody BookDto bookRequest) {
        bookService.addBook(bookRequest);
    }

    @PutMapping("/{isbn}")
    @Operation(summary = "Update a book by ISBN", description = "Update an existing book in the bookstore by its ISBN.")
    public void updateBook(
            @PathVariable @NotBlank String isbn,
            @Valid @RequestBody BookDto bookRequest
    ) {
        bookService.updateBook(isbn, bookRequest);
    }

    @DeleteMapping("/{isbn}")
    @Operation(summary = "Delete a book by ISBN",
            description = "Deletes a book from the bookstore by its ISBN. Requires ADMIN role.",
            security = @SecurityRequirement(name = "basicAuth"))
    public void removeBook(@PathVariable @NotBlank String isbn) {
        bookService.deleteBook(isbn);
    }

    @GetMapping("/test")
    @Operation(summary = "External Test", description = "Test.")
    public ResponseEntity<String> test() {
        return externalApiService.call("http://localhost:8080/api/books?title=34");
    }
}
