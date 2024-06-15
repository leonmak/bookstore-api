package com.example.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    private String isbn;

    private String title;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_isbn", nullable = false)
    private List<Author> authors;

    private Integer year;

    private Double price;

    private String genre;
}
