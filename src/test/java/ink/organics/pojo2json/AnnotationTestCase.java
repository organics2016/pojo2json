package ink.organics.pojo2json;

import com.fasterxml.jackson.databind.JsonNode;

public class AnnotationTestCase extends POJO2JsonJavaTestCase {


    public void testJsonPropertyTestPOJO() {
        JsonNode result = this.testAction("JsonPropertyTestPOJO.java", new POJO2JsonDefaultAction());

        assertNotNull(result.get("name"));
        assertNotNull(result.get("pass"));
    }

    public void testJsonIgnoreTestPOJO() {
        JsonNode result = this.testAction("JsonIgnoreTestPOJO.java", new POJO2JsonDefaultAction());

        assertNull(result.get("username"));
        assertNotNull(result.get("password"));
    }

    public void testJsonIgnorePropertiesTestPOJO() {
        JsonNode result = this.testAction("JsonIgnorePropertiesTestPOJO.java", new POJO2JsonDefaultAction());

        assertNotNull(result.get("roles").get(0).get("roleName"));
        assertNull(result.get("roles").get(0).get("users"));
    }

    public void testJsonIgnorePropertiesDocTestPOJO() {
        JsonNode result = this.testAction("JsonIgnorePropertiesDocTestPOJO.java", new POJO2JsonDefaultAction());

        assertNotNull(result.get("roles").get(0).get("roleName"));
        assertNull(result.get("roles").get(0).get("users"));
    }

    public void testJsonIgnoreTypeTestPOJO() {
        JsonNode result = this.testAction("JsonIgnoreTypeTestPOJO.java", new POJO2JsonDefaultAction());

        assertTrue(result.get("roles").isEmpty());
    }
}
