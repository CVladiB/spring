package ru.baranova.spring.service.app;

import ru.baranova.spring.model.EntityObject;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface CheckService {

    default <T> boolean doCheck(T t, Function<T, List<String>>... checkFn) {
        List<String> checkResult = Arrays.stream(checkFn).flatMap(f -> f.apply(t).stream()).toList();
        checkResult.forEach(getLogger());
        return checkResult.isEmpty();
    }

    default <T> T correctOrDefault(T t, Function<T, List<String>> checkFn, Supplier<T> defaultValue) {
        return doCheck(t, checkFn) ? t : defaultValue.get();
    }

    Consumer<String> getLogger();

    List<String> checkCorrectInputStrLengthAndSymbols(String str, int min, int max);

    List<String> checkCorrectInputStrLength(String str, int min, int max);

    List<String> checkCorrectInputInt(Integer input);

    List<String> checkAllFieldsAreNotNull(Object obj);

    List<String> checkExist(EntityObject entity);

    List<String> checkIfNotExist(Supplier<List<? extends EntityObject>> supplier);

}
