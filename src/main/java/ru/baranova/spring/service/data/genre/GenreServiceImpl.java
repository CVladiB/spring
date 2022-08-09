package ru.baranova.spring.service.data.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.genre.GenreDao;
import ru.baranova.spring.domain.BusinessConstants;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final static int MIN_INPUT = 3;
    private final static int MAX_INPUT_NAME = 20;
    private final static int MAX_INPUT_DESCRIPTION = 200;
    private final GenreDao genreDaoJdbc;
    private final CheckService checkServiceImpl;

    @Nullable
    @Override
    public Genre create(@NonNull String name, String description) {
        Genre genre = null;
        try {
            if (!checkExist(name) && checkServiceImpl.isCorrectSymbolsInInputString(name, MIN_INPUT, MAX_INPUT_NAME)) {
                description = checkServiceImpl.isCorrectSymbolsInInputString(description, MIN_INPUT, MAX_INPUT_DESCRIPTION) ?
                        description : null;
                genre = genreDaoJdbc.create(name, description);
            }
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
        }
        return genre;
    }

    @Nullable
    @Override
    public Genre readById(@NonNull Integer id) {
        try {
            return genreDaoJdbc.getById(id);
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
            return null;
        }
    }

    @Nullable
    @Override
    public Genre readByName(@NonNull String name) {
        try {
            return genreDaoJdbc.getByName(name);
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
            return null;
        }
    }

    @Override
    public List<Genre> readAll() {
        try {
            return genreDaoJdbc.getAll();
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
            return new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public Genre update(@NonNull Integer id, String name, String description) {
        Genre genre = null;
        try {
            if (checkExist(id) && !checkExist(name)) {
                if (!checkServiceImpl.isCorrectSymbolsInInputString(name, MIN_INPUT, MAX_INPUT_NAME)) {
                    name = genreDaoJdbc.getById(id).getName();
                }
                if (!checkServiceImpl.isCorrectSymbolsInInputString(description, MIN_INPUT, MAX_INPUT_DESCRIPTION)) {
                    description = genreDaoJdbc.getById(id).getDescription();
                }
                genre = genreDaoJdbc.update(id, name, description);
            }
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
        }
        return genre;
    }

    @Nullable
    @Override
    public boolean delete(@NonNull Integer id) {
        try {
            return checkExist(id) && genreDaoJdbc.delete(id) > 0;
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
            return false;
        }
    }

    @Override
    public boolean checkExist(Integer id) {
        boolean isExist = false;
        try {
            if (readById(id) == null) {
                log.info(BusinessConstants.CheckServiceLog.SHOULD_EXIST_INPUT);
            } else {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
        }
        return isExist;
    }

    @Override
    public boolean checkExist(String name) {
        boolean isExist = true;
        try {
            if (readByName(name) == null) {
                isExist = false;
            } else {
                log.info(BusinessConstants.EntityServiceLog.WARNING_EXIST);
            }
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
        }
        return isExist;
    }
}
