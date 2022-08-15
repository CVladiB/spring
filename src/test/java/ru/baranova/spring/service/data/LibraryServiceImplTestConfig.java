package ru.baranova.spring.service.data;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.book.BookService;
import ru.baranova.spring.service.data.genre.GenreService;

@TestConfiguration
public class LibraryServiceImplTestConfig {
    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;
    @SpyBean
    private LibraryService libraryService;

    @Bean
    public LibraryService libraryService(BookService bookService
            , AuthorService authorService
            , GenreService genreService) {
        return new LibraryServiceImpl(bookService, authorService, genreService);
    }
}
