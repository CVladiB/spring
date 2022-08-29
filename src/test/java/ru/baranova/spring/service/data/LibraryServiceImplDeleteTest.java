package ru.baranova.spring.service.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.aspect.ThrowingAspect;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.service.data.book.BookService;

@SpringBootTest(classes = {LibraryServiceImplTestConfig.class, StopSearchConfig.class, ThrowingAspect.class})
class LibraryServiceImplDeleteTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private LibraryServiceImpl libraryService;

    @Test
    void book__delete__true() {
        Integer deleteId = 1;
        Mockito.when(bookService.delete(deleteId)).thenReturn(Boolean.TRUE);
        Assertions.assertTrue(libraryService.delete(deleteId));
    }

    @Test
    void book__delete__false() {
        Integer deleteId = -1;
        Mockito.when(bookService.delete(deleteId)).thenReturn(Boolean.FALSE);
        Assertions.assertThrows(NullPointerException.class, () -> libraryService.delete(deleteId));
    }
}