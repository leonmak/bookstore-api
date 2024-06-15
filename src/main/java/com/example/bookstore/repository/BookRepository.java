package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, String> {
    List<Book> findByTitleOrAuthorsName(String title, String authorName);
}
