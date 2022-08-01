package ru.baranova.spring.service.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.dao.book.BookDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.book.BookService;
import ru.baranova.spring.service.data.config.BookServiceImplTestConfig;

import java.util.List;

@SpringBootTest(classes = {BookServiceImplTestConfig.class, StopSearchConfig.class})
class BookServiceImplTest {
    @Autowired
    private BookDao bookDaoJdbc;
    @Autowired
    private CheckService checkServiceImpl;
    @Autowired
    private ParseService parseServiceImpl;
    @Autowired
    private BookService bookServiceImpl;
    private int minInput;
    private int maxInput;
    private Book insertBook1;
    private Book insertBook2;
    private Book testBook;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, null, null);
        Author insertAuthor2 = new Author(2, null, null);
        Genre insertGenre1 = new Genre(1, null, null);
        Genre insertGenre2 = new Genre(2, null, null);

        insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2));
        insertBook2 = new Book(2, "Title2", insertAuthor1, List.of(insertGenre2));
        testBook = new Book(null, "TitleTest", insertAuthor2, List.of(insertGenre2));
        bookList = List.of(insertBook1, insertBook2);

        minInput = 3;
        maxInput = 40;
    }

    @Test
    void book__create__correctReturnNewObject() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();
        Integer newId = bookList.size() + 1;

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput)).thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.create(inputTitle, inputAuthorId, inputGenreIdList)).thenReturn(newId);

        testBook.setId(newId);
        Book expected = testBook;
        Book actual = bookServiceImpl.create(inputTitle, inputAuthorId, inputGenreIdList);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__create_IncorrectTitle__returnNull() {
        String inputTitle = "smth";
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput)).thenReturn(Boolean.FALSE);

        Assertions.assertNull(bookServiceImpl.create(inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__create_IncorrectAuthorId__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = 100;
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput)).thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.create(inputTitle, inputAuthorId, inputGenreIdList)).thenThrow(DataIntegrityViolationException.class);

        Assertions.assertNull(bookServiceImpl.create(inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__create_IncorrectGenreId__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = List.of(100);

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput)).thenReturn(Boolean.FALSE);
        Mockito.when(bookDaoJdbc.create(inputTitle, inputAuthorId, inputGenreIdList)).thenThrow(DataIntegrityViolationException.class);

        Assertions.assertNull(bookServiceImpl.create(inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__create_IncorrectExistTitleAndAuthorId__returnNull() {
        String inputTitle = insertBook1.getTitle();
        Integer inputAuthorId = insertBook1.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();

        Assertions.assertNull(bookServiceImpl.create(inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__readById__correctReturnObject() {
        Integer inputId = bookList.size();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.getById(inputId)).thenReturn(bookList.get(inputId - 1));

        Book expected = bookList.get(inputId - 1);
        Book actual = bookServiceImpl.readById(inputId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readById_NonexistentId__returnNull() {
        Integer inputId = bookList.size() + 1;

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Assertions.assertNull(bookServiceImpl.readById(inputId));
    }


    @Test
    void book__readByTitle__correctReturnListObject() {
        insertBook2.setTitle(insertBook1.getTitle());
        String inputTitle = insertBook1.getTitle();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputTitle), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.getByTitle(inputTitle)).thenReturn(List.of(insertBook1, insertBook2));

        List<Book> expected = List.of(insertBook1, insertBook2);
        List<Book> actual = bookServiceImpl.readByTitle(inputTitle);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitle_NonexistentTitle__returnNull() {
        String inputTitle = testBook.getTitle();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputTitle), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(bookDaoJdbc.getByTitle(inputTitle)).thenReturn(List.of(insertBook1, insertBook2));

        Assertions.assertNull(bookServiceImpl.readByTitle(inputTitle));
    }

    @Test
    void book__readAll__correctReturnListObject() {
        Mockito.doReturn(bookList).when(bookDaoJdbc).getAll();

        List<Book> expected = bookList;
        List<Book> actual = bookServiceImpl.readAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update__correctReturnObject() {
        int countAffectedRows = 1;
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();
        Integer inputId = insertBook1.getId();

        Mockito.when(parseServiceImpl.parseDashToNull(inputTitle)).thenReturn(inputTitle);
        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.getById(inputId)).thenReturn(insertBook1);
        Mockito.when(bookDaoJdbc.update(inputId, inputTitle, inputAuthorId, inputGenreIdList))
                .thenReturn(countAffectedRows);

        testBook.setId(insertBook1.getId());
        Book expected = testBook;
        Book actual = bookServiceImpl.update(inputId, inputTitle, inputAuthorId, inputGenreIdList);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update_IncorrectTitle__correctReturnObject() {
        int countAffectedRows = 1;
        String inputTitle = "smth";
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();
        Integer inputId = insertBook1.getId();

        Mockito.when(parseServiceImpl.parseDashToNull(inputTitle)).thenReturn(inputTitle);
        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.getById(inputId)).thenReturn(insertBook1);
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput))
                .thenReturn(Boolean.FALSE);
        Mockito.when(bookDaoJdbc.update(inputId, insertBook1.getTitle(), inputAuthorId, inputGenreIdList))
                .thenReturn(countAffectedRows);

        testBook.setId(insertBook1.getId());
        testBook.setTitle(insertBook1.getTitle());
        Book expected = testBook;
        Book actual = bookServiceImpl.update(inputId, inputTitle, inputAuthorId, inputGenreIdList);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update_NullTitle__correctReturnObject() {
        int countAffectedRows = 1;
        String inputTitle = "-";
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();
        Integer inputId = insertBook1.getId();

        Mockito.when(parseServiceImpl.parseDashToNull(inputTitle)).thenReturn(null);
        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.getById(inputId)).thenReturn(insertBook1);
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput))
                .thenReturn(Boolean.FALSE);
        Mockito.when(bookDaoJdbc.getById(inputId)).thenReturn(insertBook1);
        Mockito.when(bookDaoJdbc.update(inputId, insertBook1.getTitle(), inputAuthorId, inputGenreIdList))
                .thenReturn(countAffectedRows);

        testBook.setId(insertBook1.getId());
        testBook.setTitle(insertBook1.getTitle());
        Book expected = testBook;
        Book actual = bookServiceImpl.update(inputId, inputTitle, inputAuthorId, inputGenreIdList);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update_IncorrectAuthorId__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = List.of(100);
        Integer inputId = insertBook1.getId();

        Mockito.when(parseServiceImpl.parseDashToNull(inputTitle)).thenReturn(inputTitle);
        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.getById(inputId)).thenReturn(insertBook1);
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.update(inputId, inputTitle, inputAuthorId, inputGenreIdList))
                .thenThrow(DataIntegrityViolationException.class);

        Assertions.assertNull(bookServiceImpl.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__update_NonexistentId__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();
        Integer inputId = insertBook1.getId() + 1;

        Mockito.when(parseServiceImpl.parseDashToNull(inputTitle)).thenReturn(inputTitle);
        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Assertions.assertNull(bookServiceImpl.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__update_ExistNonexistentId__returnNull() {
        int countAffectedRows = 0;
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();
        Integer inputId = insertBook1.getId() + 1;

        Mockito.when(parseServiceImpl.parseDashToNull(inputTitle)).thenReturn(inputTitle);
        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.getById(inputId)).thenReturn(insertBook1);
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.update(inputId, inputTitle, inputAuthorId, inputGenreIdList))
                .thenReturn(countAffectedRows);

        Assertions.assertNull(bookServiceImpl.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }


    @Test
    void book__update_IncorrectGenreId__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = 100;
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();
        Integer inputId = insertBook1.getId();

        Mockito.when(parseServiceImpl.parseDashToNull(inputTitle)).thenReturn(inputTitle);
        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.getById(inputId)).thenReturn(insertBook1);
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.update(inputId, inputTitle, inputAuthorId, inputGenreIdList))
                .thenThrow(DataIntegrityViolationException.class);

        Assertions.assertNull(bookServiceImpl.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__delete__true() {
        int countAffectedRows = 1;
        Integer inputId = bookList.size();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.delete(inputId)).thenReturn(countAffectedRows);

        Assertions.assertTrue(bookServiceImpl.delete(inputId));
    }

    @Test
    void book__delete_NonexistentId__false() {
        Integer inputId = bookList.size() + 1;

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Assertions.assertFalse(bookServiceImpl.delete(inputId));
    }

    @Test
    void book__delete_ExistNonexistentId__false() {
        int countAffectedRows = 0;
        Integer inputId = bookList.size();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.delete(inputId)).thenReturn(countAffectedRows);


        Assertions.assertFalse(bookServiceImpl.delete(inputId));
    }
}