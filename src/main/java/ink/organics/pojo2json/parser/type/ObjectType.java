package ink.organics.pojo2json.parser.type;

import java.util.Map;

public class ObjectType implements SpecifyType {

    @Override
    public Object def() {
        return Map.of();
    }

    @Override
    public Object random() {
        return Map.of();
    }
}
