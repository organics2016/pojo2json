package ink.organics.pojo2json.parser.type;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class YearMonthType extends TemporalType implements SpecifyType {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    public Object random() {
        return YearMonth.from(ZonedDateTime
                .ofInstant(Instant.ofEpochMilli((long) super.random()), ZoneId.systemDefault()))
                .format(formatter);
    }

    @Override
    public Object def() {
        return YearMonth.from(ZonedDateTime
                .ofInstant(Instant.ofEpochMilli((long) super.def()), ZoneId.systemDefault()))
                .format(formatter);
    }
}
