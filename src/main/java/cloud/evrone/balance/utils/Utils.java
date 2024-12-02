package cloud.evrone.balance.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {


  public static double roundDown(double value, int decimalPlaces) {
    double scale = Math.pow(10, decimalPlaces);
    return Math.floor(value * scale) / scale;
  }

}
