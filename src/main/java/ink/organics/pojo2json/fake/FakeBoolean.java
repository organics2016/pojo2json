package ink.organics.pojo2json.fake;

import java.util.Random;

public class FakeBoolean implements JsonFakeValuesService {

    private final Random random = new Random();

    @Override
    public Object random() {
        return random.nextBoolean();
    }

    @Override
    public Object def() {
        return false;
    }
}
