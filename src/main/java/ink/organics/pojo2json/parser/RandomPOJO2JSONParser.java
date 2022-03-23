package ink.organics.pojo2json.parser;

import ink.organics.pojo2json.parser.type.SpecifyType;

public class RandomPOJO2JSONParser extends POJO2JSONParser {

    @Override
    protected Object getFakeValue(SpecifyType specifyType) {
        return specifyType.random();
    }
}
