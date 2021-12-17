package testdata.java;


public class JsonPropertyTestPOJO {

    // IDEA 测试用例下好像不支持 import 第三方资源包
    @com.fasterxml.jackson.annotation.JsonProperty("name")
    private String username;

    @com.alibaba.fastjson.annotation.JSONField(name = "pass")
    private String password;
}
