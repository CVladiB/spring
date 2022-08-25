package ru.baranova.spring.service.data.book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.repository.entity.BookRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {BookServiceImplTestConfig.class, StopSearchConfig.class})
class BookServiceImplDeleteTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, null, null);
        Genre insertGenre1 = new Genre(1, null, null);
        Genre insertGenre2 = new Genre(2, null, null);

        Book insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2), Collections.emptyList());
        Book insertBook2 = new Book(2, "Title2", insertAuthor1, List.of(insertGenre2), Collections.emptyList());
        bookList = List.of(insertBook1, insertBook2);
    }

    @Test
    void book__delete__true() {
        Integer inputId = bookList.size();
        Mockito.when(bookRepository.findById(inputId)).thenReturn(Optional.of(bookList.get(1)));
        Mockito.doNothing().when(bookRepository).delete(bookList.get(1));
        Assertions.assertTrue(bookService.delete(inputId));
    }

    @Test
    void book__delete_NonexistentId__false() {
        Integer inputId = bookList.size() + 1;
        Mockito.when(bookRepository.findById(inputId)).thenReturn(Optional.empty());
        Assertions.assertFalse(bookService.delete(inputId));
    }
}
