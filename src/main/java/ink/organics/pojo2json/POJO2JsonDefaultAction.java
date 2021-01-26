package ink.organics.pojo2json;

import ink.organics.pojo2json.fake.JsonFakeValuesService;

public class POJO2JsonDefaultAction extends POJO2JsonAction {

    @Override
    protected Object getFakeValue(JsonFakeValuesService jsonFakeValuesService) {
        return jsonFakeValuesService.def();
    }
}
