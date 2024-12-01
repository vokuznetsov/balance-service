package cloud.evrone.balance.utils;

import java.util.Collection;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConsumerUtils {

  public static <T> void acceptIfNotNull(T value, Consumer<T> setter) {
    if (value != null) {
      setter.accept(value);
    }
  }

  public static <T, R> void acceptIfNotNull(T value, Consumer<R> setter, Function<T, R> mapper) {
    if (value != null) {
      setter.accept(mapper.apply(value));
    }
  }

  public <T, R> void acceptIfNotNull(BooleanSupplier conditionSupplier,
                                     Supplier<T> valueSupplier,
                                     Consumer<R> action,
                                     Function<T, R> mapper) {
    if (conditionSupplier.getAsBoolean()) {
      T value = valueSupplier.get();
      if (value != null) {
        action.accept(mapper.apply(value));
      }
    }
  }

  public <T> void acceptIfNotNull(BooleanSupplier conditionSupplier, Supplier<T> valueSupplier,
      Consumer<T> action) {
    if (conditionSupplier.getAsBoolean()) {
      T value = valueSupplier.get();
      if (value != null) {
        action.accept(value);
      }
    }
  }

  public static <T> void acceptAllIfNotEmpty(Collection<T> collection,
      Consumer<Collection<T>> adder) {
    if (collection != null && !collection.isEmpty()) {
      adder.accept(collection);
    }
  }

  public static <T, E> void acceptAllIfNotEmpty(Map<T, E> map, Consumer<Map<T, E>> adder) {
    if (map != null && !map.isEmpty()) {
      adder.accept(map);
    }
  }
}
