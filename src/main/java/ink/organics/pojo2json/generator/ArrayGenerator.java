package ink.organics.pojo2json.generator;

import java.util.List;

public class ArrayGenerator extends JSONValueGenerator {

    @Override
    public Object generator() {
        return List.of();
    }
}
