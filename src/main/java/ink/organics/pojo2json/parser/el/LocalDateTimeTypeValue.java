package ink.organics.pojo2json.parser.el;

import java.time.format.DateTimeFormatter;

public class LocalDateTimeTypeValue extends ZonedDateTimeTypeValue {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Object getRandomValue() {
        return this.getRandomValue(formatter);
    }

    @Override
    public Object getValue() {
        return this.getValue(formatter);
    }

}
