package ink.organics.pojo2json.parser.type;

import java.util.List;

public class ArrayType implements SpecifyType {


    @Override
    public Object def() {
        return List.of();
    }

    @Override
    public Object random() {
        return List.of();
    }
}
