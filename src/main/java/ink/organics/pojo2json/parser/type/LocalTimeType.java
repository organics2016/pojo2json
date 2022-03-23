package ink.organics.pojo2json.parser.type;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalTimeType extends TemporalType implements SpecifyType {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public Object random() {
        return LocalTime
                .ofInstant(Instant.ofEpochMilli((long) super.random()), ZoneId.systemDefault())
                .format(formatter);
    }

    @Override
    public Object def() {
        return LocalTime
                .ofInstant(Instant.ofEpochMilli((long) super.def()), ZoneId.systemDefault())
                .format(formatter);
    }
}
