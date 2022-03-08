package testdata.java;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonPropertyTestPOJO {

    @JsonProperty
    private String userId;

    @JsonProperty("name")
    private String username;

    @JSONField(name = "pass")
    private String password;

}
