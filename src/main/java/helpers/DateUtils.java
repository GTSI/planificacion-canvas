package helpers;

import com.sun.istack.internal.NotNull;

import java.sql.Timestamp;
import java.util.Calendar;

public class DateUtils {
  public enum TypeOperation {
    ADD, SUBSTRACT
  }
  public static void modifyTimestamp(@NotNull Timestamp time, TypeOperation type, int number_days) {
    Calendar calendar = Calendar.getInstance();
    switch (type) {
      case ADD: {
        calendar.add(Calendar.DAY_OF_YEAR, number_days);

        time.setTime(calendar.getTime().getTime());
        break;
      }

      case SUBSTRACT: {
        break;
      }
      default: {
        break;
      }
    }
  }
}
