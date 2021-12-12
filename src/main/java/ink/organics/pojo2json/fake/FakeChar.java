package ink.organics.pojo2json.fake;

public class FakeChar implements JsonFakeValuesService {

    @Override
    public Object random() {
        return 'c';
    }

    @Override
    public Object def() {
        return 'c';
    }

}
