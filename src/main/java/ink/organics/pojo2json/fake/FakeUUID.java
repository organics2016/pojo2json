package ink.organics.pojo2json.fake;

import java.util.UUID;

public class FakeUUID implements JsonFakeValuesService {

    @Override
    public Object random() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Object def() {
        return UUID.randomUUID().toString();
    }
}
