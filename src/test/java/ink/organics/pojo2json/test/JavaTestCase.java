package ink.organics.pojo2json.test;


import ink.organics.pojo2json.POJO2JsonDefaultAction;


public class JavaTestCase extends TestCase {


    @Override
    protected String getTestDataPath() {
        return "src/test/java/testdata/java";
    }


    public void testCurrent() {
        annotationTestModel.testJsonPropertyTestPOJO("JsonPropertyTestPOJO.java", new POJO2JsonDefaultAction());
    }

    public void testJavaDateType() {
        dataTypeTestModel.testPrimitiveTestPOJO("PrimitiveTestPOJO.java", new POJO2JsonDefaultAction());
        dataTypeTestModel.testPrimitiveArrayTestPOJO("PrimitiveArrayTestPOJO.java", new POJO2JsonDefaultAction());
        dataTypeTestModel.testEnumTestPOJO("EnumTestPOJO.java", new POJO2JsonDefaultAction());
        dataTypeTestModel.testIterableTestPOJO("IterableTestPOJO.java", new POJO2JsonDefaultAction());
        dataTypeTestModel.testGenericTestPOJO("GenericTestPOJO.java", new POJO2JsonDefaultAction());
        dataTypeTestModel.testSpecialObjectTestPOJO("SpecialObjectTestPOJO.java", new POJO2JsonDefaultAction());
    }


    public void testJavaAnnotation() {
        annotationTestModel.testJsonPropertyTestPOJO("JsonPropertyTestPOJO.java", new POJO2JsonDefaultAction());
        annotationTestModel.testJsonIgnoreTestPOJO("JsonIgnoreTestPOJO.java", new POJO2JsonDefaultAction());
        annotationTestModel.testJsonIgnorePropertiesTestPOJO("JsonIgnorePropertiesTestPOJO.java", new POJO2JsonDefaultAction());
        annotationTestModel.testJsonIgnoreTypeTestPOJO("JsonIgnoreTypeTestPOJO.java", new POJO2JsonDefaultAction());
    }


    public void testJavaDoc() {
        docTestModel.testJsonIgnoreDocTestPOJO("JsonIgnoreDocTestPOJO.java", new POJO2JsonDefaultAction());
        docTestModel.testJsonIgnorePropertiesDocTestPOJO("JsonIgnorePropertiesDocTestPOJO.java", new POJO2JsonDefaultAction());
    }


    public void testJavaStaticField() {
        staticFieldTestModel.testStaticFieldPOJO("StaticFieldPOJO.java", new POJO2JsonDefaultAction());
    }

}
