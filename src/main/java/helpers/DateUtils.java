package helpers;

import com.sun.istack.internal.NotNull;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

public class DateUtils {
  public enum TypeOperation {
    ADD, SUBSTRACT
  }
  public static void modifyTimestamp(@NotNull Timestamp time, TypeOperation type, int number_days) {
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
    calendar.setTimeInMillis(time.getTime());
    switch (type) {
      case ADD: {
        calendar.add(Calendar.DAY_OF_YEAR, number_days);

        time.setTime(calendar.getTime().getTime());
        break;
      }

      case SUBSTRACT: {
        calendar.add(Calendar.DAY_OF_YEAR, -number_days);

        time.setTime(calendar.getTime().getTime());
        break;
      }
      default: {
        break;
      }
    }
  }

  public static Timestamp getTimeStampFromDateData(int year, int month, int day) {
    Calendar cal = Calendar.getInstance();
    cal.set(year, month, day);
    Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
    return timestamp;
  }

}
