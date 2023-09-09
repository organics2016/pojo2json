package ink.organics.pojo2json.parser.el;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTypeValue extends TemporalTypeValue {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Object getRandomValue() {
        return LocalDateTime
                .ofInstant(Instant.ofEpochMilli((long) super.getRandomValue()), ZoneId.systemDefault())
                .format(formatter);
    }

    @Override
    public Object getValue() {
        return LocalDateTime
                .ofInstant(Instant.ofEpochMilli((long) super.getValue()), ZoneId.systemDefault())
                .format(formatter);
    }
}
