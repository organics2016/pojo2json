package ink.organics.pojo2json.parser.el;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalTypeValue implements RandomTypeValue {
    @Override
    public Object getRandomValue() {
        return this.getRandomValue(0, 100, 2);
    }

    @Override
    public Object getValue() {
        return this.getValue(2);
    }

    public Object getRandomValue(int scale) {
        return this.getRandomValue(0, 100, scale);
    }

    public Object getRandomValue(double origin, double bound, int scale) {
        return BigDecimal.valueOf(random.nextDouble(origin, bound)).setScale(scale, RoundingMode.DOWN);
    }

    public Object getValue(int scale) {
        return BigDecimal.ZERO.setScale(scale, RoundingMode.UNNECESSARY);
    }


}
