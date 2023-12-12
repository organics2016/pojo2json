package ink.organics.pojo2json.parser.el;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalTypeValue implements RandomTypeValue {
    @Override
    public Object getRandomValue() {
        return BigDecimal.valueOf(random.nextDouble(0, 100)).setScale(2, RoundingMode.DOWN);
    }

    @Override
    public Object getValue() {
        return BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY);
    }
}
