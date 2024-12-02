package cloud.evrone.balance.utils;

import static cloud.evrone.balance.utils.Utils.roundDown;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UtilsTest {

  @Test
  void round() {
    assertEquals(roundDown(10.0000, 2), 10.00);
    assertEquals(roundDown(10.3321, 2), 10.33);
    assertEquals(roundDown(10.5993, 2), 10.59);
    assertEquals(roundDown(10, 2), 10);
  }
}
