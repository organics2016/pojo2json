package ink.organics.pojo2json.parser.type;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateType extends TemporalType implements SpecifyType {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Object random() {
        return LocalDate
                .ofInstant(Instant.ofEpochMilli((long) super.random()), ZoneId.systemDefault())
                .format(formatter);
    }

    @Override
    public Object def() {
        return LocalDate
                .ofInstant(Instant.ofEpochMilli((long) super.def()), ZoneId.systemDefault())
                .format(formatter);
    }

}
