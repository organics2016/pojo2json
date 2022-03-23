package ink.organics.pojo2json.parser.type;

public class StringType implements SpecifyType {

    @Override
    public Object def() {
        return "";
    }

    @Override
    public Object random() {
        return "fake_data";
    }
}
