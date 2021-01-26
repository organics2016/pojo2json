package ink.organics.pojo2json.fake;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class FakeDecimal implements JsonFakeValuesService {


    private final Random random = new Random();

    @Override
    public Object random() {
        return BigDecimal.valueOf(random.nextFloat() * 100L).setScale(2, RoundingMode.DOWN);
    }

    @Override
    public Object def() {
        return BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY);
    }

}
