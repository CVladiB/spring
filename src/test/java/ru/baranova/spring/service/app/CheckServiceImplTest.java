package ru.baranova.spring.service.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.config.CheckServiceImplTestConfig;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(classes = {CheckServiceImplTestConfig.class, StopSearchConfig.class})
class CheckServiceImplTest {
    @Autowired
    private CheckService checkServiceImpl;

    @Test
    void string_Min_Max__isCorrectSymbolsInInputString__true() {
        String inputStr = "azAZаяАЯёЁ-";
        int minSizeInputStr = 1;
        int maxSizeInputStr = 20;
        Assertions.assertTrue(checkServiceImpl.isCorrectSymbolsInInputString(inputStr, minSizeInputStr, maxSizeInputStr));
    }

    @Test
    void string_Min_Max__isCorrectSymbolsInInputString_WrongSymbol__false() {
        String inputStr = "a!zAZаяАЯёЁ-";
        int minSizeInputStr = 1;
        int maxSizeInputStr = 20;
        Assertions.assertFalse(checkServiceImpl.isCorrectSymbolsInInputString(inputStr, minSizeInputStr, maxSizeInputStr));
    }

    @Test
    void string_Min_Max__isCorrectSymbolsInInputString_Number__false() {
        String inputStr = "123";
        int minSizeInputStr = 1;
        int maxSizeInputStr = 20;
        Assertions.assertFalse(checkServiceImpl.isCorrectSymbolsInInputString(inputStr, minSizeInputStr, maxSizeInputStr));
    }

    @Test
    void string_Min_Max__isCorrectSymbolsInInputString_Backspace__false() {
        String inputStr = "a zAZаяАЯёЁ";
        int minSizeInputStr = 1;
        int maxSizeInputStr = 20;
        Assertions.assertFalse(checkServiceImpl.isCorrectSymbolsInInputString(inputStr, minSizeInputStr, maxSizeInputStr));
    }

    @Test
    void string_Min_Max__isCorrectInputString_MinInput__true() {
        String inputStr = "a";
        int minSizeInputStr = 1;
        int maxSizeInputStr = 20;
        Assertions.assertTrue(checkServiceImpl.isCorrectSymbolsInInputString(inputStr, minSizeInputStr, maxSizeInputStr));
    }

    @Test
    void string_Min_Max__isCorrectInputString_MaxInput__true() {
        String inputStr = "aa";
        int minSizeInputStr = 1;
        int maxSizeInputStr = 3;
        Assertions.assertTrue(checkServiceImpl.isCorrectSymbolsInInputString(inputStr, minSizeInputStr, maxSizeInputStr));
    }

    @Test
    void string_Min_Max__isCorrectInputString_EmptyInput__false() {
        String inputStr = "";
        int minSizeInputStr = 0;
        int maxSizeInputStr = 20;
        Assertions.assertFalse(checkServiceImpl.isCorrectSymbolsInInputString(inputStr, minSizeInputStr, maxSizeInputStr));
    }

    @Test
    void string_Min_Max__isCorrectInputString_NullInput__false() {
        String inputStr = null;
        int minSizeInputStr = 0;
        int maxSizeInputStr = 20;
        Assertions.assertFalse(checkServiceImpl.isCorrectSymbolsInInputString(inputStr, minSizeInputStr, maxSizeInputStr));
    }

    @Test
    void Integer__isCorrectInputInt__true() {
        Integer number = 1;
        Assertions.assertTrue(checkServiceImpl.isCorrectInputInt(number));
    }

    @Test
    void Integer__isCorrectInputInt_NullInput__false() {
        Integer number = null;
        Assertions.assertFalse(checkServiceImpl.isCorrectInputInt(number));
    }

    @Test
    void String_Stream_FlagTrue__isInputExist__true() {
        Stream<String> streamOfInput = Stream.of("aa", "bb", "cc");
        String inputStr = "aa";
        Boolean isShouldExist = true;
        Assertions.assertTrue(checkServiceImpl.isInputExist(inputStr, streamOfInput, isShouldExist));
    }

    @Test
    void String_Stream_FlagFalse__isInputExist__true() {
        Stream<String> streamOfInput = Stream.of("aa", "bb", "cc");
        String inputStr = "aa";
        Boolean isShouldExist = false;
        Assertions.assertTrue(checkServiceImpl.isInputExist(inputStr, streamOfInput, isShouldExist));
    }

    @Test
    void String_Stream_FlagNull__isInputExist__true() {
        Stream<String> streamOfInput = Stream.of("aa", "bb", "cc");
        String inputStr = "aa";
        Boolean isShouldExist = null;
        Assertions.assertTrue(checkServiceImpl.isInputExist(inputStr, streamOfInput, isShouldExist));
    }

    @Test
    void String_Stream_Flag__isInputExist__false() {
        Stream<String> streamOfInput = Stream.of("aa", "bb", "cc");
        String inputStr = "aabbcc";
        Boolean isShouldExist = true;
        Assertions.assertFalse(checkServiceImpl.isInputExist(inputStr, streamOfInput, isShouldExist));
    }

    @Test
    void Integer_Stream_Flag__isInputExist__true() {
        Stream<Integer> streamOfInput = Stream.of(1, 2, 3);
        Integer inputNumber = 1;
        Boolean isShouldExist = true;
        Assertions.assertTrue(checkServiceImpl.isInputExist(inputNumber, streamOfInput, isShouldExist));
    }

    @Test
    void book__isAllFieldsNotNull__true() {
        Author testAuthor = new Author(1, "SurnameTest", "NameTest");
        Genre testGenre = new Genre(1, "NameTest", "DescriptionTest");
        Book testBook = new Book(1, "TitleTest", testAuthor, List.of(testGenre));
        Assertions.assertTrue(checkServiceImpl.isAllFieldsNotNull(testBook));
    }

    @Test
    void book__isAllFieldsNotNull_NullFieldNestedField_AuthorId__true() {
        Author testAuthor = new Author(null, "SurnameTest", "NameTest");
        Genre testGenre = new Genre(1, "NameTest", null);
        Book testBook = new Book(1, "TitleTest", testAuthor, List.of(testGenre));
        Assertions.assertTrue(checkServiceImpl.isAllFieldsNotNull(testBook));
    }

    @Test
    void book__isAllFieldsNotNull_NullFieldNestedField_GenreId__true() {
        Author testAuthor = new Author(1, "SurnameTest", "NameTest");
        Genre testGenre = new Genre(null, "NameTest", "DescriptionTest");
        Book testBook = new Book(1, "TitleTest", testAuthor, List.of(testGenre));
        Assertions.assertTrue(checkServiceImpl.isAllFieldsNotNull(testBook));
    }

    @Test
    void book__isAllFieldsNotNull_NullField_AuthorNull__false() {
        Author testAuthor = null;
        Genre testGenre = new Genre(1, "NameTest", null);
        Book testBook = new Book(1, "TitleTest", testAuthor, List.of(testGenre));
        Assertions.assertFalse(checkServiceImpl.isAllFieldsNotNull(testBook));
    }

    @Test
    void book__isAllFieldsNotNull_NullObject__false() {
        Book testBook = null;
        Assertions.assertFalse(checkServiceImpl.isAllFieldsNotNull(testBook));
    }

}