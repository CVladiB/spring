package ru.baranova.spring.service.data.comment;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.entity.comment.CommentDao;
import ru.baranova.spring.service.app.CheckService;

@TestConfiguration
public class CommentServiceImplTestConfig {
    @MockBean
    private CommentDao commentDao;
    @MockBean
    private CheckService checkService;

    @Bean
    public CommentService commentService(CommentDao commentDao, CheckService checkService) {
        return new CommentServiceImpl(commentDao, checkService);
    }
}
