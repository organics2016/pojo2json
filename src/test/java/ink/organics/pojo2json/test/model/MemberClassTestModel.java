package ink.organics.pojo2json.test.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.actionSystem.AnAction;
import ink.organics.pojo2json.test.TestCase;

import static org.junit.Assert.assertTrue;

public class MemberClassTestModel extends TestModel {


    public MemberClassTestModel(TestCase testCase) {
        super(testCase);
    }

    public void testMemberClassTestPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action, "class");
        assertTrue(result.size() == 1 && result.get("test") != null);
        result = testCase.testAction(fileName, action, "A");
        assertTrue(result.size() == 1 && result.get("a") != null);
        result = testCase.testAction(fileName, action, "B");
        assertTrue(result.size() == 1 && result.get("b") != null);
    }
}
