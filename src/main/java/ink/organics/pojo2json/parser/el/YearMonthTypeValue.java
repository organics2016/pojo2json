package ink.organics.pojo2json.parser.el;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class YearMonthTypeValue extends TemporalTypeValue {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    public Object getRandomValue() {
        return YearMonth.from(ZonedDateTime
                        .ofInstant(Instant.ofEpochMilli((long) super.getRandomValue()), ZoneId.systemDefault()))
                .format(formatter);
    }

    @Override
    public Object getValue() {
        return YearMonth.from(ZonedDateTime
                        .ofInstant(Instant.ofEpochMilli((long) super.getValue()), ZoneId.systemDefault()))
                .format(formatter);
    }
}
