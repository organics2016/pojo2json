package ink.organics.pojo2json.parser.el;


public class IntegerTypeValue implements RandomTypeValue {
    @Override
    public Object getRandomValue() {
        return this.getRandomValue(0, 100);
    }

    @Override
    public Object getValue() {
        return 0;
    }

    public Object getRandomValue(int origin, int bound) {
        return random.nextInt(origin, bound);
    }
}
