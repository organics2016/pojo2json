package testdata.kotlin

class JsonPropertyTestPOJO {
    // IDEA 测试用例下好像不支持 import 第三方资源包
    @com.fasterxml.jackson.annotation.JsonProperty("name")
    private val username: String? = null

    @com.alibaba.fastjson.annotation.JSONField(name = "pass")
    private val password: String? = null
}