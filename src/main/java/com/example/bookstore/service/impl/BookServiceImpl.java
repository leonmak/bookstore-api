package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookDto;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.exception.DuplicateBookException;
import com.example.bookstore.mapper.BookDtoMapper;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookDtoMapper bookDtoMapper;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookDtoMapper bookDtoMapper, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookDtoMapper = bookDtoMapper;
        this.bookMapper = bookMapper;
    }

    @Override
    public void addBook(BookDto createRequest) {
        Book book = bookDtoMapper.convert(createRequest);
        var isbn = book.getIsbn();
        if (bookRepository.existsById(isbn)) {
            throw new DuplicateBookException("isbn already exists");
        }
        book.getAuthors().forEach(author -> author.setBook(book));
        bookRepository.save(book);
    }

    @Override
    public List<BookDto> fetchBooksByTitleAuthor(String title, String author) {
        List<Book> books = bookRepository.findByTitleOrAuthorsName(title, author);
        return books.stream()
                .map(bookMapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void updateBook(String isbn, BookDto updateRequest) {
        if (!bookRepository.existsById(isbn)) {
            throw new BookNotFoundException();
        }
        bookRepository.deleteById(isbn);
        Book book = bookDtoMapper.convert(updateRequest);
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(String id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException();
        }
        bookRepository.deleteById(id);
    }
}
