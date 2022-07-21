package ink.organics.pojo2json.parser.type;

import java.util.UUID;

public class StringType implements SpecifyType {

    public StringType() {
    }

    @Override
    public Object def() {
        return "";
    }

    @Override
    public Object random() {
        String uuid = UUID.randomUUID().toString();
        return "test_" + uuid.substring(uuid.lastIndexOf("-") + 1);
    }
}
