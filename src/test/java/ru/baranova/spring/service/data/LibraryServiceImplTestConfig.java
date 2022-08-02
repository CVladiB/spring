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
    private BookService bookServiceImpl;
    @MockBean
    private AuthorService authorServiceImpl;
    @MockBean
    private GenreService genreServiceImpl;
    @SpyBean
    private LibraryService libraryServiceImpl;

    @Bean
    public LibraryService libraryServiceImpl(BookService bookServiceImpl
            , AuthorService authorServiceImpl
            , GenreService genreServiceImpl) {
        return new LibraryServiceImpl(bookServiceImpl, authorServiceImpl, genreServiceImpl);
    }
}
