package testdata.kotlin

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonProperty

class JsonPropertyTestPOJO {

    @JsonProperty
    private val userId: String? = null

    @JsonProperty("name")
    private val username: String? = null

    @JSONField(name = "pass")
    private val password: String? = null
}