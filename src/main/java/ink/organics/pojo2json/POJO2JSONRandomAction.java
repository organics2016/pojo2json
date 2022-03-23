package ink.organics.pojo2json;

import ink.organics.pojo2json.parser.POJO2JSONParser;
import ink.organics.pojo2json.parser.RandomPOJO2JSONParser;

public class POJO2JSONRandomAction extends POJO2JSONAction {

    private static final POJO2JSONParser POJO_2_JSON_PARSER = new RandomPOJO2JSONParser();

    public POJO2JSONRandomAction() {
        super(POJO_2_JSON_PARSER);
    }

}
