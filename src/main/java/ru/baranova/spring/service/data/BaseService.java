package ru.baranova.spring.service.data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface BaseService {
    default <T> List<T> getOrEmptyList(List<T> list) {
        return Optional.ofNullable(list).orElse(Collections.emptyList());
    }

    default <T> Object getFnOrNull(T obj, Function<T, Object> fn) {
        return obj == null ? null : fn.apply(obj);
    }

    default <T, U> Object getObjectOrNull(T obj, U newObj) {
        return obj == null ? null : newObj;
    }
}
