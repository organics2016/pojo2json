package ink.organics.pojo2json.test.model;

import ink.organics.pojo2json.test.MyTestCase;

import java.util.Arrays;

import static junit.framework.Assert.assertTrue;

public class TestModel {

    protected final MyTestCase testCase;

    public TestModel(MyTestCase testCase) {
        this.testCase = testCase;
    }

    public static void assertArrayEquals(Object[] expected, Object[] actual) {
        assertTrue(Arrays.equals(expected, actual));
    }
}
