package ink.organics.pojo2json.parser.el;

import java.time.format.DateTimeFormatter;

public class LocalTimeTypeValue extends LocalDateTimeTypeValue {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public Object getRandomValue() {
        return this.getRandomValue(formatter);
    }

    @Override
    public Object getValue() {
        return this.getValue(formatter);
    }
}
