package ink.organics.pojo2json.test;


import ink.organics.pojo2json.POJO2JSONDefaultAction;


public class JavaTestCase extends TestCase {


    @Override
    protected String getTestDataPath() {
        return "src/test/java/testdata/java";
    }


    public void testCurrent() {
        annotationTestModel.testJsonPropertyTestPOJO("JsonPropertyTestPOJO.java", new POJO2JSONDefaultAction());
    }

    public void testJavaDateType() {
        dataTypeTestModel.testPrimitiveTestPOJO("PrimitiveTestPOJO.java", new POJO2JSONDefaultAction());
        dataTypeTestModel.testPrimitiveArrayTestPOJO("PrimitiveArrayTestPOJO.java", new POJO2JSONDefaultAction());
        dataTypeTestModel.testEnumTestPOJO("EnumTestPOJO.java", new POJO2JSONDefaultAction());
        dataTypeTestModel.testIterableTestPOJO("IterableTestPOJO.java", new POJO2JSONDefaultAction());
        dataTypeTestModel.testGenericTestPOJO("GenericTestPOJO.java", new POJO2JSONDefaultAction());
        dataTypeTestModel.testSpecialObjectTestPOJO("SpecialObjectTestPOJO.java", new POJO2JSONDefaultAction());
    }


    public void testJavaAnnotation() {
        annotationTestModel.testJsonPropertyTestPOJO("JsonPropertyTestPOJO.java", new POJO2JSONDefaultAction());
        annotationTestModel.testJsonIgnoreTestPOJO("JsonIgnoreTestPOJO.java", new POJO2JSONDefaultAction());
        annotationTestModel.testJsonIgnorePropertiesTestPOJO("JsonIgnorePropertiesTestPOJO.java", new POJO2JSONDefaultAction());
        annotationTestModel.testJsonIgnoreTypeTestPOJO("JsonIgnoreTypeTestPOJO.java", new POJO2JSONDefaultAction());
    }


    public void testJavaDoc() {
        docTestModel.testJsonIgnoreDocTestPOJO("JsonIgnoreDocTestPOJO.java", new POJO2JSONDefaultAction());
        docTestModel.testJsonIgnorePropertiesDocTestPOJO("JsonIgnorePropertiesDocTestPOJO.java", new POJO2JSONDefaultAction());
    }


    public void testJavaStaticField() {
        staticFieldTestModel.testStaticFieldPOJO("StaticFieldPOJO.java", new POJO2JSONDefaultAction());
    }


    public void testJavaMemberClass() {
        memberClassTestModel.testMemberClassTestPOJO("MemberClassTestPOJO.java", new POJO2JSONDefaultAction());
    }
}
