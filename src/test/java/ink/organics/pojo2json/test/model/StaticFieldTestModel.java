package ink.organics.pojo2json.test.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.openapi.actionSystem.AnAction;
import ink.organics.pojo2json.test.MyTestCase;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class StaticFieldTestModel extends TestModel {

    public StaticFieldTestModel(MyTestCase testCase) {
        super(testCase);
    }

    public void testStaticFieldPOJO(String fileName, AnAction action) {
        JsonNode result = testCase.testAction(fileName, action);

        assertNotNull(result.get("thisFinal"));
        assertNotNull(result.get("thisTransient"));
        assertNotNull(result.get("thisFinalTransient"));

        assertNull(result.get("serialVersionUID"));
        assertNull(result.get("thisStatic"));
        assertNull(result.get("thisStaticFinal"));
        assertNull(result.get("thisStaticTransient"));
        assertNull(result.get("thisStaticFinalTransient"));
    }

}
