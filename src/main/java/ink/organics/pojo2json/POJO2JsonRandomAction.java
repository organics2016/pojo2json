package ink.organics.pojo2json;

import ink.organics.pojo2json.fake.JsonFakeValuesService;

public class POJO2JsonRandomAction extends POJO2JsonAction {

    @Override
    protected Object getFakeValue(JsonFakeValuesService jsonFakeValuesService) {
        return jsonFakeValuesService.random();
    }
}
