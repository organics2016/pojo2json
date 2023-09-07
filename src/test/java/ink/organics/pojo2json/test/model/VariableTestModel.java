package ink.organics.pojo2json.test.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.actionSystem.AnAction;
import ink.organics.pojo2json.test.MyTestCase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class VariableTestModel extends TestModel {

    public VariableTestModel(MyTestCase testCase) {
        super(testCase);
    }

    public void testVariableTestPOJO(String fileName, AnAction action) {

        JsonNode result = testCase.testAction(fileName, action, "listField");
        assertTrue(result.isArray());
        result = result.get(0);
        test(result);
        assertTrue(result.get("data").isTextual());


        result = testCase.testAction(fileName, action, "cParameter");
        assertTrue(result.isObject());
        test(result);
        assertEquals(0, result.get("data").asInt());


        result = testCase.testAction(fileName, action, "mParameter");
        assertTrue(result.isObject());
        test(result);
        assertEquals("", result.get("data").asText());


        result = testCase.testAction(fileName, action, "localVariable");
        assertTrue(result.isObject());
        test(result);
        assertNull(result.get("data").get("username"));
        assertEquals("", result.get("data").get("password").asText());
    }

    private void test(JsonNode result) {

        assertEquals(0, result.get("anInt").asInt());
        assertTrue(result.get("string").isTextual());
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.UNNECESSARY), result.get("bigDecimal").decimalValue());
        assertArrayEquals(new Integer[]{0},
                StreamSupport.stream(result.get("ints").spliterator(), false).map(JsonNode::intValue).toArray(Integer[]::new));
        assertArrayEquals(new Integer[]{0},
                StreamSupport.stream(result.get("linkedHashSet").spliterator(), false).map(JsonNode::intValue).toArray(Integer[]::new));
    }
}
