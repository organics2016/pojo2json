package ink.organics.pojo2json.parser;

public class POJO2JSONParserFactory {

    private static final POJO2JSONParser pojo2JSONParser = new POJO2JSONParser();

    private POJO2JSONParserFactory() {
    }

    public static POJO2JSONParser getInstant() {
        return pojo2JSONParser;
    }
}
