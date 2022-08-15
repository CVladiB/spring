package ru.baranova.spring.service.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BookDTO;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.config.CheckServiceImplTestConfig;

import java.util.Collections;
import java.util.List;

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

    // todo
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
        BookEntity testBookEntity = new BookEntity(1, "TitleTest", testAuthor.getId(), List.of(testGenre.getId()));
        List<String> expected = Collections.emptyList();
        List<String> actual = checkService.checkAllFieldsAreNotNull(testBookEntity);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__isAllFieldsNotNull_NullFieldNestedField_AuthorId__true() {
        Author testAuthor = new Author(null, "SurnameTest", "NameTest");
        Genre testGenre = new Genre(1, "NameTest", null);
        BookDTO testBook = new BookDTO(1, "TitleTest", testAuthor, List.of(testGenre));
        List<String> expected = Collections.emptyList();
        List<String> actual = checkService.checkAllFieldsAreNotNull(testBook);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__isAllFieldsNotNull_NullFieldNestedField_GenreId__true() {
        Author testAuthor = new Author(1, "SurnameTest", "NameTest");
        Genre testGenre = new Genre(null, "NameTest", "DescriptionTest");
        BookDTO testBook = new BookDTO(1, "TitleTest", testAuthor, List.of(testGenre));
        List<String> expected = Collections.emptyList();
        List<String> actual = checkService.checkAllFieldsAreNotNull(testBook);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__isAllFieldsNotNull_NullField_AuthorNull__false() {
        Author testAuthor = null;
        Genre testGenre = new Genre(1, "NameTest", null);
        BookDTO testBook = new BookDTO(1, "TitleTest", testAuthor, List.of(testGenre));
        List<String> expected = List.of(config.getNOTHING_INPUT());
        List<String> actual = checkService.checkAllFieldsAreNotNull(testBook);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__isAllFieldsNotNull_NullObject__false() {
        BookEntity testBookEntity = null;
        List<String> expected = List.of(config.getNOTHING_INPUT());
        List<String> actual = checkService.checkAllFieldsAreNotNull(testBookEntity);
        Assertions.assertEquals(expected, actual);
    }
}