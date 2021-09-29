package id.learn.dynamicwhere.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Ravikumar.Beli@blueyonder.com
 * @project spring-data-jpa-dynamic-where
 * @created on 16 Aug, 2021 7:45 AM
 **/

@UtilityClass
public class DateUtil {

    public String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static Date toDate(String dateStr, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime date = LocalDateTime.parse(dateStr, formatter);
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Pair<Date, Date> getDatePair(String value) {
        return Pair.of(toDate(value.split(",")[0], FORMAT),
                toDate(value.split(",")[1], FORMAT));
    }
}
