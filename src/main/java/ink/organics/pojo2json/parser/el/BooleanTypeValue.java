package ink.organics.pojo2json.parser.el;

import org.apache.commons.lang3.RandomUtils;

public class BooleanTypeValue implements RandomTypeValue {

    @Override
    public Object getRandomValue() {
        return RandomUtils.nextBoolean();
    }

    @Override
    public Object getValue() {
        return false;
    }
}
