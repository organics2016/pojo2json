package ink.organics.pojo2json.parser.type;

import org.apache.commons.lang3.RandomUtils;

public class IntegerType implements SpecifyType {


    @Override
    public Object def() {
        return 0;
    }

    @Override
    public Object random() {
        return RandomUtils.nextInt(0, 100);
    }
}
