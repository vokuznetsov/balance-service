package cloud.evrone.balance.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

  private final Set<Integer> ids = new HashSet<>();

  public static Integer nextIntId() {
    int id;
    do {
      id = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE - 1);
    } while (!ids.add(id));
    return id;
  }

  public static Long nextLongId() {
    return Long.valueOf(nextIntId());
  }
}
