package ink.organics.pojo2json.parser.type;

import org.apache.commons.lang3.RandomUtils;

public class BooleanType implements SpecifyType {

    @Override
    public Object def() {
        return false;
    }

    @Override
    public Object random() {
        return RandomUtils.nextBoolean();
    }
}
