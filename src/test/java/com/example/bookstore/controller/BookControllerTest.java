package com.example.bookstore.controller;

import com.example.bookstore.BookstoreApplication;
import com.example.bookstore.configuration.JacksonConfig;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@ContextConfiguration(classes = {JacksonConfig.class, BookstoreApplication.class})
class BookControllerTest {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    String testIsbn = "testIsbn";
    String testTitle = "Test Book";
    String testAuthorName = "test Author Name";
    Double price = 19.99;
    Integer year = 1999;
    String genre = "testGenre";
    Map<String, Object> body = Map.of(
            "title", testTitle,
            "isbn", testIsbn,
            "price", price,
            "year", year,
            "genre", genre,
            "authors", List.of(Map.of(
                    "name", testAuthorName
            ))
    );

    @Test
    void createBook() throws Exception {
        String requestJson = objectMapper.writeValueAsString(body);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();


        mockMvc.perform(get("/api/books")
                        .param("title", testTitle)
                        .param("author", testAuthorName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].isbn").value(testIsbn))
                .andExpect(jsonPath("$[0].title").value(testTitle))
                .andExpect(jsonPath("$[0].year").value(year))
                .andExpect(jsonPath("$[0].price").value(price))
                .andExpect(jsonPath("$[0].genre").value(genre))
                .andExpect(jsonPath("$[0].authors[0].name").value(testAuthorName))
                .andExpect(jsonPath("$[0].authors[0].birthday").doesNotExist());
    }

    @Test
    void updateBook() throws Exception {
        var newTestTitle = "newTitle";
        var newAuthorName = "newAuthor";
        var newBody = new HashMap<>(body);
        newBody.replace("title", newTestTitle);
        var birthday = "1990-12-13";
        newBody.replace("authors", List.of(Map.of("name", newAuthorName, "birthday", birthday)));
        var editRequestJson = objectMapper.writeValueAsString(newBody);
        mockMvc.perform(put("/api/books/{isbn}", testIsbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editRequestJson))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc.perform(get("/api/books")
                        .param("title", newTestTitle)
                        .param("author", testAuthorName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].isbn").value(testIsbn))
                .andExpect(jsonPath("$[0].title").value(newTestTitle))
                .andExpect(jsonPath("$[0].authors[0].name").value(newAuthorName))
                .andExpect(jsonPath("$[0].authors[0].birthday").value(birthday));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBook() throws Exception {
        var book = new Book();
        book.setTitle("test");
        book.setIsbn("isbn");
        bookRepository.save(book);
        mockMvc.perform(delete("/api/books/{isbn}", "isbn"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/books")
                        .param("title", "test")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void invalidData() throws Exception {
        var blankString = " ";
        var wrongBirthday = "2024-06-19";
        var invalidNumber = -1;
        var body = Map.of(
                "title", blankString,
                "isbn", blankString,
                "year", invalidNumber,
                "price", invalidNumber,
                "authors", List.of(Map.of(
                        "name", blankString,
                        "birthday", wrongBirthday
                ))
        );
        var json = objectMapper.writeValueAsString(body);
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(6));
    }
}