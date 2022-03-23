package ink.organics.pojo2json;

import ink.organics.pojo2json.parser.DefaultPOJO2JSONParser;

public class POJO2JSONDefaultAction extends POJO2JSONAction {


    private static final ink.organics.pojo2json.parser.POJO2JSONParser POJO_2_JSON_PARSER = new DefaultPOJO2JSONParser();

    public POJO2JSONDefaultAction() {
        super(POJO_2_JSON_PARSER);
    }

}
