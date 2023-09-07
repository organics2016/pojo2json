package ink.organics.pojo2json.test;


import ink.organics.pojo2json.EditorPopupMenuDefaultAction;


public class JavaTestCase extends MyTestCase {


    @Override
    protected String getTestDataPath() {
        return "src/test/java/testdata/java";
    }


    public void testCurrent() {
        // TODO 测试环境2022.3版本 MockJDK 不再包含java.time依赖包，可能是JB的BUG
        dataTypeTestModel.testSpecialObjectTestPOJO("SpecialObjectTestPOJO.java", new EditorPopupMenuDefaultAction());
    }

    public void testJavaVariable() {
        variableTestModel.testVariableTestPOJO("VariableTestPOJO.java", new EditorPopupMenuDefaultAction());
    }

    public void testJavaDateType() {
        dataTypeTestModel.testPrimitiveTestPOJO("PrimitiveTestPOJO.java", new EditorPopupMenuDefaultAction());
        dataTypeTestModel.testPrimitiveArrayTestPOJO("PrimitiveArrayTestPOJO.java", new EditorPopupMenuDefaultAction());
        dataTypeTestModel.testEnumTestPOJO("EnumTestPOJO.java", new EditorPopupMenuDefaultAction());
        dataTypeTestModel.testIterableTestPOJO("IterableTestPOJO.java", new EditorPopupMenuDefaultAction());
        dataTypeTestModel.testGenericTestPOJO("GenericTestPOJO.java", new EditorPopupMenuDefaultAction());
        dataTypeTestModel.testSpecialObjectTestPOJO("SpecialObjectTestPOJO.java", new EditorPopupMenuDefaultAction());
    }


    public void testJavaAnnotation() {
        annotationTestModel.testJsonPropertyTestPOJO("JsonPropertyTestPOJO.java", new EditorPopupMenuDefaultAction());
        annotationTestModel.testJsonIgnoreTestPOJO("JsonIgnoreTestPOJO.java", new EditorPopupMenuDefaultAction());
        annotationTestModel.testJsonIgnorePropertiesTestPOJO("JsonIgnorePropertiesTestPOJO.java", new EditorPopupMenuDefaultAction());
        annotationTestModel.testJsonIgnoreTypeTestPOJO("JsonIgnoreTypeTestPOJO.java", new EditorPopupMenuDefaultAction());
    }


    public void testJavaDoc() {
        docTestModel.testJsonIgnoreDocTestPOJO("JsonIgnoreDocTestPOJO.java", new EditorPopupMenuDefaultAction());
        docTestModel.testJsonIgnorePropertiesDocTestPOJO("JsonIgnorePropertiesDocTestPOJO.java", new EditorPopupMenuDefaultAction());
    }


    public void testJavaStaticField() {
        staticFieldTestModel.testStaticFieldPOJO("StaticFieldPOJO.java", new EditorPopupMenuDefaultAction());
    }


    public void testJavaMemberClass() {
        memberClassTestModel.testMemberClassTestPOJO("MemberClassTestPOJO.java", new EditorPopupMenuDefaultAction());
    }
}
