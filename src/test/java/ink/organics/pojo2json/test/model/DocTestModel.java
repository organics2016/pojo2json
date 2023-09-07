package ink.organics.pojo2json.test.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.actionSystem.AnAction;
import ink.organics.pojo2json.test.MyTestCase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocTestModel extends TestModel {

    public DocTestModel(MyTestCase testCase) {
        super(testCase);
    }

    public void testJsonIgnorePropertiesDocTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertNotNull(result.get("roles").get(0).get("roleName"));
        assertNull(result.get("roles").get(0).get("users"));
    }

    public void testJsonIgnoreDocTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertNull(result.get("username"));
        assertNotNull(result.get("password"));
    }
}
