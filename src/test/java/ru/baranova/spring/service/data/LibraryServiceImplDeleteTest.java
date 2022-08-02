package ru.baranova.spring.service.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.service.data.book.BookService;

@SpringBootTest(classes = {LibraryServiceImplTestConfig.class, StopSearchConfig.class})
class LibraryServiceImplDeleteTest {
    @Autowired
    private BookService bookServiceImpl;
    @Autowired
    private LibraryService libraryServiceImpl;

    @Test
    void book__delete__true() {
        Integer deleteId = 1;
        Mockito.when(bookServiceImpl.delete(deleteId)).thenReturn(true);
        Assertions.assertTrue(libraryServiceImpl.delete(deleteId));
    }

    @Test
    void book__delete__false() {
        Integer deleteId = -1;
        Mockito.when(bookServiceImpl.delete(deleteId)).thenReturn(false);
        Assertions.assertFalse(libraryServiceImpl.delete(deleteId));
    }
}