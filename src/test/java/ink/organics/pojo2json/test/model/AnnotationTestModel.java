package ink.organics.pojo2json.test.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.actionSystem.AnAction;
import ink.organics.pojo2json.test.TestCase;

import static org.junit.Assert.*;

public class AnnotationTestModel extends TestModel {


    public AnnotationTestModel(TestCase testCase) {
        super(testCase);
    }

    public void testJsonPropertyTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertNotNull(result.get("name"));
        assertNotNull(result.get("pass"));
        assertNotNull(result.get("userId"));
    }

    public void testJsonIgnoreTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertNull(result.get("username"));
        assertNotNull(result.get("password"));
    }

    public void testJsonIgnorePropertiesTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertNotNull(result.get("roles").get(0).get("roleName"));
        assertNull(result.get("roles").get(0).get("users"));
    }

    public void testJsonIgnoreTypeTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertTrue(result.get("roles").isEmpty());
    }
}
