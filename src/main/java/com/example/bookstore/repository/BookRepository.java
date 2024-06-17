package com.example.bookstore.repository;

import com.example.bookstore.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, String> {

    @Query("select b from Book b left join fetch b.authors a where b.title = :title or a.name = :name")
    List<Book> findByTitleOrAuthorsName(@Param("title") String title, @Param("name") String name);
}
