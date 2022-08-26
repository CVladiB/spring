package ru.baranova.spring.service.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.EntityObject;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.app.config.CheckServiceImplTestConfig;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootTest(classes = {CheckServiceImplTestConfig.class, StopSearchConfig.class})
class CheckServiceImplTest {
    @Autowired
    private CheckService checkService;
    @Autowired
    private CheckServiceImplTestConfig config;

    @ParameterizedTest
    @ValueSource(strings = {"azAZаяАЯёЁ-"})
    void string_Min_Max__isCorrectSymbolsInInputString__true(String inputStr) {
        int minSizeInputStr = 1;
        int maxSizeInputStr = 20;

        List<String> expected = Collections.emptyList();
        List<String> actual = checkService.checkCorrectInputStrLengthAndSymbols(inputStr, minSizeInputStr, maxSizeInputStr);
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a!zAZаяАЯёЁ-", "123", "a zAZаяАЯёЁ"})
    void string_Min_Max__isCorrectSymbolsInInputString_WrongSymbol__false(String inputStr) {
        int minSizeInputStr = 0;
        int maxSizeInputStr = 20;

        List<String> expected = List.of(config.getCHAR_OR_NUMBER_INPUT());
        List<String> actual = checkService.checkCorrectInputStrLengthAndSymbols(inputStr, minSizeInputStr, maxSizeInputStr);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void string_Min_Max__isCorrectSymbolsInInputString_WrongSymbol__false() {
        String inputStr = "";
        int minSizeInputStr = 0;
        int maxSizeInputStr = 20;

        List<String> expected = List.of(config.getNOTHING_INPUT());
        List<String> actual = checkService.checkCorrectInputStrLengthAndSymbols(inputStr, minSizeInputStr, maxSizeInputStr);
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "a, 1, 3",
            "aa, 1, 3"
    })
    void string_Min_Max__isCorrectInputString__true(String inputStr, int minSizeInputStr, int maxSizeInputStr) {
        List<String> expected = Collections.emptyList();
        List<String> actual = checkService.checkCorrectInputStrLength(inputStr, minSizeInputStr, maxSizeInputStr);
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "aaa, 0, 3",
            "aaaa, 0, 3"
    })
    void string_Min_Max__isCorrectInputString_MaxInput__false(String inputStr, int minSizeInputStr, int maxSizeInputStr) {
        List<String> expected = List.of(String.format(config.getLONG_INPUT(), maxSizeInputStr));
        List<String> actual = checkService.checkCorrectInputStrLength(inputStr, minSizeInputStr, maxSizeInputStr);
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "a, 2, 20"
    })
    void string_Min_Max__isCorrectInputString_MinInput__false(String inputStr, int minSizeInputStr, int maxSizeInputStr) {
        List<String> expected = List.of(String.format(config.getSHORT_INPUT(), minSizeInputStr));
        List<String> actual = checkService.checkCorrectInputStrLength(inputStr, minSizeInputStr, maxSizeInputStr);
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "aa, 0, 3",
            "aa, 2, 20",
            "aaa, 2, 20"
    })
    void string_Min_Max__isCorrectInputString_CorrectLengthInput__true(String inputStr, int minSizeInputStr, int maxSizeInputStr) {
        List<String> expected = Collections.emptyList();
        List<String> actual = checkService.checkCorrectInputStrLength(inputStr, minSizeInputStr, maxSizeInputStr);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void string_Min_Max__isCorrectInputString_NullInput__false() {
        String inputStr = null;
        int minSizeInputStr = 0;
        int maxSizeInputStr = 20;
        List<String> expected = List.of(config.getNOTHING_INPUT());
        List<String> actual = checkService.checkCorrectInputStrLengthAndSymbols(inputStr, minSizeInputStr, maxSizeInputStr);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void Integer__isCorrectInputInt__true() {
        Integer number = 1;
        List<String> expected = Collections.emptyList();
        List<String> actual = checkService.checkCorrectInputInt(number);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void Integer__isCorrectInputInt_NullInput__false() {
        Integer number = null;
        List<String> expected = List.of(config.getNOTHING_INPUT());
        List<String> actual = checkService.checkCorrectInputInt(number);
        Assertions.assertEquals(expected, actual);
    }


    @Test
    void book__isAllFieldsNotNull__true() {
        Author testAuthor = new Author(1, "SurnameTest", "NameTest");
        Genre testGenre = new Genre(1, "NameTest", "DescriptionTest");
        Comment testComment = new Comment(1, "TestCommentAuthor", "TestBlaBlaBla", LocalDate.now());
        Book testBook = new Book(1, "TitleTest", testAuthor, List.of(testGenre), List.of(testComment));
        List<String> expected = Collections.emptyList();
        List<String> actual = checkService.checkAllFieldsAreNotNull(testBook);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__isAllFieldsNotNull_NullFieldNestedField_AuthorId__true() {
        Author testAuthor = new Author(null, "SurnameTest", "NameTest");
        Genre testGenre = new Genre(1, "NameTest", null);
        Comment testComment = new Comment(1, "TestCommentAuthor", "TestBlaBlaBla", LocalDate.now());
        Book testBook = new Book(1, "TitleTest", testAuthor, List.of(testGenre), List.of(testComment));
        List<String> expected = Collections.emptyList();
        List<String> actual = checkService.checkAllFieldsAreNotNull(testBook);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__isAllFieldsNotNull_NullFieldNestedField_GenreId__true() {
        Author testAuthor = new Author(1, "SurnameTest", "NameTest");
        Genre testGenre = new Genre(null, "NameTest", "DescriptionTest");
        Comment testComment = new Comment(1, "TestCommentAuthor", "TestBlaBlaBla", LocalDate.now());
        Book testBook = new Book(1, "TitleTest", testAuthor, List.of(testGenre), List.of(testComment));
        List<String> expected = Collections.emptyList();
        List<String> actual = checkService.checkAllFieldsAreNotNull(testBook);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__isAllFieldsNotNull_NullField_AuthorNull__false() {
        Author testAuthor = null;
        Genre testGenre = new Genre(1, "NameTest", null);
        Comment testComment = new Comment(1, "TestCommentAuthor", "TestBlaBlaBla", LocalDate.now());
        Book testBook = new Book(1, "TitleTest", testAuthor, List.of(testGenre), List.of(testComment));
        List<String> expected = List.of(config.getNOTHING_INPUT());
        List<String> actual = checkService.checkAllFieldsAreNotNull(testBook);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__isAllFieldsNotNull_NullObject__false() {
        Book testBook = null;
        List<String> expected = List.of(config.getNOTHING_INPUT());
        List<String> actual = checkService.checkAllFieldsAreNotNull(testBook);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkExist_CorrectObject__returnEmptyList() {
        EntityObject inputEntity = Author.builder().build();
        List<String> expected = Collections.emptyList();
        List<String> actual = checkService.checkExist(inputEntity);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkExist_NullObject__returnListLog() {
        EntityObject inputEntity = null;
        List<String> expected = List.of(config.getSHOULD_EXIST_INPUT());
        List<String> actual = checkService.checkExist(inputEntity);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkIfNotExist_NullObject__returnEmptyList() {
        Book book = Book.builder().genreList(Collections.emptyList()).build();
        Supplier<List<? extends EntityObject>> inputSupplier = book::getGenreList;
        List<String> expected = Collections.emptyList();
        List<String> actual = checkService.checkIfNotExist(inputSupplier);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void checkIfNotExist_CorrectObject__returnListLog() {
        Book book = Book.builder().genreList(List.of(new Genre(1, null, null), new Genre(2, null, null))).build();
        Supplier<List<? extends EntityObject>> inputSupplier = book::getGenreList;
        List<String> expected = List.of(config.getWARNING_EXIST());
        List<String> actual = checkService.checkIfNotExist(inputSupplier);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void doCheck__true() {
        EntityObject author = Author.builder().build();
        Function<EntityObject, List<String>> inputFn = t -> Collections.emptyList();
        Assertions.assertTrue(checkService.doCheck(author, inputFn));
    }

    @Test
    void doCheck__false() {
        EntityObject author = null;
        Function<EntityObject, List<String>> inputFn = t -> List.of("");
        Assertions.assertFalse(checkService.doCheck(author, inputFn));
    }

    @Test
    void correctOrDefault__correct() {
        Book book = Book.builder().author(Author.builder().name("AuthorBookDTO").build()).build();
        EntityObject authorTest = Author.builder().build();
        Function<EntityObject, List<String>> inputFn = t -> Collections.emptyList();
        EntityObject expected = authorTest;
        EntityObject actual = checkService.correctOrDefault(authorTest, inputFn, book::getAuthor);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void correctOrDefault__default() {
        Book book = Book.builder().author(Author.builder().name("AuthorBookDTO").build()).build();
        EntityObject authorTest = Author.builder().build();
        Function<EntityObject, List<String>> inputFn = t -> List.of("");
        EntityObject expected = book.getAuthor();
        EntityObject actual = checkService.correctOrDefault(authorTest, inputFn, book::getAuthor);
        Assertions.assertEquals(expected, actual);
    }
}
