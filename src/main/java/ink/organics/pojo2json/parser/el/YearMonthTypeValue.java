package ink.organics.pojo2json.parser.el;

import java.time.format.DateTimeFormatter;

public class YearMonthTypeValue extends LocalDateTimeTypeValue {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    public Object getRandomValue() {
        return this.getRandomValue(formatter);
    }

    @Override
    public Object getValue() {
        return this.getValue(formatter);
    }
}
