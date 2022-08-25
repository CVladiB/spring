package ru.baranova.spring.service.data.comment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.repository.entity.CommentRepository;
import ru.baranova.spring.service.app.CheckService;

@TestConfiguration
@ConfigurationProperties(prefix = "app.service.comment-service")
public class CommentServiceImplTestConfig {
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private CheckService checkService;

    @Bean
    public CommentService commentService(CommentRepository commentRepository, CheckService checkService) {
        return new CommentServiceImpl(commentRepository, checkService);
    }
}
