package ru.baranova.spring.service.data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface BaseService {
    default <T> List<T> getOrEmptyList(List<T> list) {
        return Optional.ofNullable(list).orElse(Collections.emptyList());
    }
}
