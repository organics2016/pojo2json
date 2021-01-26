package ink.organics.pojo2json.fake;

import java.util.Random;

public class FakeInteger implements JsonFakeValuesService{

    private final Random random = new Random();

    @Override
    public Object random() {
        return random.nextInt(100);
    }

    @Override
    public Object def() {
        return 0;
    }
}
