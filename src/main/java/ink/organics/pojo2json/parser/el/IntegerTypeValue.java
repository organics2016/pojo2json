package ink.organics.pojo2json.parser.el;

import org.apache.commons.lang3.RandomUtils;

public class IntegerTypeValue implements RandomTypeValue {
    @Override
    public Object getRandomValue() {
        return RandomUtils.nextInt(0, 100);
    }

    @Override
    public Object getValue() {
        return 0;
    }
}
