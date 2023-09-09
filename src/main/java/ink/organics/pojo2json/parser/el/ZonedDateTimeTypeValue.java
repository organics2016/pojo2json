package ink.organics.pojo2json.parser.el;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeTypeValue extends TemporalTypeValue {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public Object getRandomValue() {
        return ZonedDateTime
                .ofInstant(Instant.ofEpochMilli((long) super.getRandomValue()), ZoneId.systemDefault())
                .format(formatter);
    }

    @Override
    public Object getValue() {
        return ZonedDateTime
                .ofInstant(Instant.ofEpochMilli((long) super.getValue()), ZoneId.systemDefault())
                .format(formatter);
    }
}
