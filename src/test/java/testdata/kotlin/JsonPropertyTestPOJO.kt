package testdata.kotlin

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonProperty

class JsonPropertyTestPOJO {
    // IDEA 测试用例下好像不支持 import 第三方资源包
    @JsonProperty("name")
    private val username: String? = null

    @JSONField(name = "pass")
    private val password: String? = null
}