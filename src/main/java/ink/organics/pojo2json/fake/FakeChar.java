package ink.organics.pojo2json.fake;

public class FakeChar implements JsonFakeValuesService {

    @Override
    public Object random() {
        return '0';
    }

    @Override
    public Object def() {
        return '0';
    }

}
