package ru.baranova.spring.dao.book;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.baranova.spring.domain.Book;

import java.util.List;

@Repository
public class BookDaoJdbc implements BookDao {
    @Override
    public void create(@NonNull Book book) {

    }

    @Override
    public Book read(int id) {
        return null;
    }

    @Override
    public List<Book> read() {
        return null;
    }

    @Override
    public void update(@NonNull Book book) {

    }

    @Override
    public void delete(int id) {

    }
}
