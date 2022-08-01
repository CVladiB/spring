package ru.baranova.spring.service.data.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.service.data.LibraryService;
import ru.baranova.spring.service.data.LibraryServiceImpl;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.book.BookService;
import ru.baranova.spring.service.data.genre.GenreService;

@TestConfiguration
public class LibraryServiceImplTestConfig {
    @MockBean
    private BookService bookServiceImpl;
    @MockBean
    private AuthorService authorServiceImpl;
    @MockBean
    private GenreService genreServiceImpl;

    @Bean
    public LibraryService libraryServiceImpl(BookService bookServiceImpl
            , AuthorService authorServiceImpl
            , GenreService genreServiceImpl) {
        return new LibraryServiceImpl(bookServiceImpl, authorServiceImpl, genreServiceImpl);
    }
}
