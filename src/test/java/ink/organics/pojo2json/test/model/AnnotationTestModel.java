package ink.organics.pojo2json.test.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.actionSystem.AnAction;
import ink.organics.pojo2json.test.MyTestCase;

import static junit.framework.Assert.*;

public class AnnotationTestModel extends TestModel {


    public AnnotationTestModel(MyTestCase testCase) {
        super(testCase);
    }

    public void testJsonPropertyTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertTrue(result.get("name").isTextual());
        assertTrue(result.get("pass").isTextual());
        assertTrue(result.get("userId").isTextual());
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

    public void testJsonNamingTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action, "LowerCamelCaseStrategyTestPOJO");
        assertNotNull(result.get("firstName"));
        result = testCase.testAction(fileName, action, "UpperCamelCaseStrategyTestPOJO");
        assertNotNull(result.get("FirstName"));
        result = testCase.testAction(fileName, action, "SnakeCaseStrategyTestPOJO");
        assertNotNull(result.get("first_name"));
        result = testCase.testAction(fileName, action, "UpperSnakeCaseStrategyTestPOJO");
        assertNotNull(result.get("FIRST_NAME"));
        result = testCase.testAction(fileName, action, "KebabCaseStrategyTestPOJO");
        assertNotNull(result.get("first-name"));
        result = testCase.testAction(fileName, action, "LowerCaseStrategyTestPOJO");
        assertNotNull(result.get("firstname"));
        result = testCase.testAction(fileName, action, "LowerDotCaseStrategyTestPOJO");
        assertNotNull(result.get("first.name"));
    }
}
