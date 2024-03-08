package ink.organics.pojo2json.parser.el;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeTypeValue extends TemporalTypeValue {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public Object getRandomValue() {
        return this.getRandomValue(formatter);
    }

    @Override
    public Object getValue() {
        return this.getValue(formatter);
    }

    public Object getRandomValue(String format) {
        return this.getRandomValue(DateTimeFormatter.ofPattern(format));
    }

    public Object getValue(String format) {
        return this.getValue(DateTimeFormatter.ofPattern(format));
    }

    public Object getRandomValue(DateTimeFormatter formatter) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli((long) super.getRandomValue()), ZoneId.systemDefault()).format(formatter);
    }

    public Object getValue(DateTimeFormatter formatter) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli((long) super.getValue()), ZoneId.systemDefault()).format(formatter);
    }
}
