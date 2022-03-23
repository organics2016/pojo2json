package ink.organics.pojo2json.parser;

import ink.organics.pojo2json.parser.type.SpecifyType;

public class DefaultPOJO2JSONParser extends POJO2JSONParser {

    @Override
    protected Object getFakeValue(SpecifyType specifyType) {
        return specifyType.def();
    }
}
