package ru.baranova.spring.service.data.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.book.BookDao;
import ru.baranova.spring.dao.book.book_genre.BookGenreDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.genre.GenreService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDaoJdbc;
    private final BookGenreDao bookGenreDaoJdbc;
    private final AuthorService authorServiceImpl;
    private final GenreService genreServiceImpl;

    @Override
    public Book create(String title, String authorSurname, String authorName, String... genreArg) {
//        Book book = new Book();
//        Author author;
//        List<Genre> genreList = new ArrayList<>();
//        String result = BusinessConstants.BookService.WARNING;
//
//        if (authorServiceImpl.create(authorSurname, authorName).equals(BusinessConstants.AuthorService.COMPLETE_CREATE)) {
//            for (String genre : genreArg) {
//                genreServiceImpl.create(genre, null);
//                genreList.add(new Genre(genreServiceImpl.findId(genre), genre, null));
//            }
//
//            author = new Author();
//            List<Integer> authorIdList = authorServiceImpl.findIds(authorSurname, authorName);
//            author.setId(authorIdList.get(0));
//            author.setSurname(authorSurname);
//            author.setName(authorName);
//
//            book.setTitle(title);
//            book.setAuthor(author);
//            book.setGenre(genreList);
//            Integer id = bookDaoJdbc.create(book);
//            bookGenreDaoJdbc.createBookGenreByBookId(genreList, id);
//            result = BusinessConstants.BookService.COMPLETE_CREATE;
//        }
//
//        return result;

        Book book = new Book();
        book.setTitle(title);
        Author author = authorServiceImpl.create(authorSurname, authorName);
        book.setAuthor(author);
        List<Genre> genreList = new ArrayList<>();
        for (String genre : genreArg) {
            genreList.add(genreServiceImpl.create(genre, null));
        }
        book.setGenre(genreList);

        Integer id = bookDaoJdbc.create(book);
        bookGenreDaoJdbc.createBookGenreByBookId(genreList, id);
        book.setId(id);
        return book;
    }


    @Override
    public Book read(Integer id) {
        Book book = bookDaoJdbc.getById(id);
        return book;
    }

    @Override
    public List<Book> getByTitle(String title) {
        List<Book> bookList = bookDaoJdbc.getByTitle(title);
        return bookList;
    }

    @Override
    public List<Book> readAll() {
        List<Book> bookList = bookDaoJdbc.getAll();
        return bookList;
    }

    @Override
    public Book update(Integer id, String title, String authorSurname, String authorName, String... genreArg) {
        Book book = bookDaoJdbc.getById(id);
        book.setTitle(title);
        Author author = authorServiceImpl.create(authorSurname, authorName);
        book.setAuthor(author);
        List<Genre> genreList = new ArrayList<>();
        for (String genre : genreArg) {
            genreList.add(genreServiceImpl.create(genre, null));
        }
        book.setGenre(genreList);

        bookDaoJdbc.update(book);
        bookGenreDaoJdbc.createBookGenreByBookId(genreList, id);
        return book;
    }

    @Override
    public void delete(Integer id) {
        bookDaoJdbc.delete(id);
    }

}
