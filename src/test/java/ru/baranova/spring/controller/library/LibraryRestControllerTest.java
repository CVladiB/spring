package ru.baranova.spring.controller.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.data.LibraryService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {StopSearchConfig.class, LibraryRestController.class})
class LibraryRestControllerTest {
    @MockBean
    private LibraryService libraryService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private Genre genre1;
    private Genre genre2;
    private Book book;
    private Book insertBook1;

    @BeforeEach
    void setUp() {
        LocalDate localDate = LocalDate.now();
        Author insertAuthor1 = new Author(1, "Surname1", "Name1");
        Genre insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");
        Comment insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", localDate);
        Comment insertComment3 = new Comment(3, "CommentAuthor2", "BlaBlaBla", localDate);

        insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2), List.of(insertComment1, insertComment3));

        Author author = new Author(7, "surname", "name");
        genre1 = new Genre(7, "name1", "description");
        genre2 = new Genre(8, "name2", "description");
        Comment comment = new Comment(7, "CommentAuthor", "BlaBlaBla", LocalDate.now());
        book = new Book(7, "title", author, List.of(genre1, genre2), List.of(comment));
    }

    @Test
    void create_correct() throws Exception {
        Mockito.when(libraryService.create(book.getTitle()
                        , book.getAuthor().getSurname()
                        , book.getAuthor().getName()
                        , List.of(genre1.getName(), genre2.getName())))
                .thenReturn(book);

        mvc.perform(put("/book/bc")
                        .param("title", book.getTitle())
                        .param("authorSurname", book.getAuthor().getSurname())
                        .param("authorName", book.getAuthor().getName())
                        .param("genreNames", book.getGenreList().get(0).getName(), book.getGenreList().get(1).getName()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    void createById_correct() throws Exception {
        Mockito.when(libraryService.create(book.getTitle()
                        , book.getAuthor().getId()
                        , List.of(genre1.getId(), genre2.getId())))
                .thenReturn(book);

        mvc.perform(put("/book/bc-id")
                        .param("title", book.getTitle())
                        .param("authorId", book.getAuthor().getId().toString())
                        .param("genreIds", book.getGenreList().get(0).getId().toString(), book.getGenreList().get(1).getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    void readById_correct() throws Exception {
        Integer id = book.getId();
        Mockito.when(libraryService.readById(id)).thenReturn(book);

        mvc.perform(get("/book/br-id")
                        .param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }


    @Test
    void readByTitle_correct() throws Exception {
        Book book1 = book;
        Book book2 = new Book(8, "title", new Author(8, "surname1", "name1"), List.of(genre1), Collections.emptyList());
        String inputTitle = book.getTitle();
        Mockito.when(libraryService.readByTitle(inputTitle)).thenReturn(List.of(book1, book2));

        mvc.perform(get("/book/br")
                        .param("title", inputTitle))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(book1, book2))));
    }

    @Test
    void readAll_correct() throws Exception {
        Book book1 = book;
        Book book2 = new Book(8, "title", new Author(8, "surname1", "name1"), List.of(genre1), Collections.emptyList());
        Mockito.when(libraryService.readAll()).thenReturn(List.of(book1, book2));
        mvc.perform(get("/book/br-all"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(book1, book2))));
    }

    @Test
    void update_correct() throws Exception {
        Mockito.when(libraryService.update(book.getId()
                        , book.getTitle()
                        , book.getAuthor().getSurname()
                        , book.getAuthor().getName()
                        , List.of(genre1.getName(), genre2.getName())))
                .thenReturn(book);
        mvc.perform(patch("/book/bu")
                        .param("id", book.getId().toString())
                        .param("title", book.getTitle())
                        .param("authorSurname", book.getAuthor().getSurname())
                        .param("authorName", book.getAuthor().getName())
                        .param("genreNames", book.getGenreList().get(0).getName(), book.getGenreList().get(1).getName()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    void updateById_correct() throws Exception {
        Mockito.when(libraryService.update(book.getId()
                        , book.getTitle()
                        , book.getAuthor().getId()
                        , List.of(genre1.getId(), genre2.getId())))
                .thenReturn(book);

        mvc.perform(patch("/book/bu-id")
                        .param("id", book.getId().toString())
                        .param("title", book.getTitle())
                        .param("authorId", book.getAuthor().getId().toString())
                        .param("genreIds", book.getGenreList().get(0).getId().toString(), book.getGenreList().get(1).getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    void updateByIdAddComment_correct() throws Exception {
        Mockito.when(libraryService.updateAddCommentToBook(book.getId()
                        , book.getCommentList().get(0).getAuthor()
                        , book.getCommentList().get(0).getText()))
                .thenReturn(book);

        mvc.perform(patch("/book/bu-id-cc")
                        .param("bookId", book.getId().toString())
                        .param("commentAuthor", book.getCommentList().get(0).getAuthor())
                        .param("commentText", book.getCommentList().get(0).getText()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    void updateByIdAddCommentById_correct() throws Exception {
        Mockito.when(libraryService.updateAddCommentByIdToBook(book.getId()
                        , book.getCommentList().get(0).getId()))
                .thenReturn(book);

        mvc.perform(patch("/book/bu-id-cc-id")
                        .param("bookId", book.getId().toString())
                        .param("commentId", book.getCommentList().get(0).getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    void updateByIdUpdateComment_correct() throws Exception {
        Mockito.when(libraryService.updateUpdateCommentToBook(book.getId()
                        , book.getCommentList().get(0).getId()
                        , book.getCommentList().get(0).getText()))
                .thenReturn(book);

        mvc.perform(patch("/book/bu-id-cu")
                        .param("bookId", book.getId().toString())
                        .param("commentId", book.getCommentList().get(0).getId().toString())
                        .param("commentText", book.getCommentList().get(0).getText()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    void delete_correct() throws Exception {
        mvc.perform(delete("/book/bd").param("id", insertBook1.getId().toString()))
                .andExpect(status().isOk());
        Mockito.verify(libraryService, Mockito.times(1)).delete(insertBook1.getId());
    }
}
