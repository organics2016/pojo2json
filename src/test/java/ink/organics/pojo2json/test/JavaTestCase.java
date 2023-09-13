package ink.organics.pojo2json.test;


import ink.organics.pojo2json.EditorPopupMenuAction;


public class JavaTestCase extends MyTestCase {


    @Override
    protected String getTestDataPath() {
        return "src/test/java/testdata/java";
    }


    public void testCurrent() {
        dataTypeTestModel.testSpecialObjectTestPOJO("SpecialObjectTestPOJO.java", new EditorPopupMenuAction());
//        dataTypeTestModel.testGenericTestPOJO("GenericTestPOJO.java", new EditorPopupMenuAction());
    }

    public void testJavaVariable() {
        variableTestModel.testVariableTestPOJO("VariableTestPOJO.java", new EditorPopupMenuAction());
    }

    public void testJavaDateType() {
        dataTypeTestModel.testPrimitiveTestPOJO("PrimitiveTestPOJO.java", new EditorPopupMenuAction());
        dataTypeTestModel.testPrimitiveArrayTestPOJO("PrimitiveArrayTestPOJO.java", new EditorPopupMenuAction());
        dataTypeTestModel.testEnumTestPOJO("EnumTestPOJO.java", new EditorPopupMenuAction());
        dataTypeTestModel.testIterableTestPOJO("IterableTestPOJO.java", new EditorPopupMenuAction());
        dataTypeTestModel.testGenericTestPOJO("GenericTestPOJO.java", new EditorPopupMenuAction());
        dataTypeTestModel.testSpecialObjectTestPOJO("SpecialObjectTestPOJO.java", new EditorPopupMenuAction());
    }


    public void testJavaAnnotation() {
        annotationTestModel.testJsonPropertyTestPOJO("JsonPropertyTestPOJO.java", new EditorPopupMenuAction());
        annotationTestModel.testJsonIgnoreTestPOJO("JsonIgnoreTestPOJO.java", new EditorPopupMenuAction());
        annotationTestModel.testJsonIgnorePropertiesTestPOJO("JsonIgnorePropertiesTestPOJO.java", new EditorPopupMenuAction());
        annotationTestModel.testJsonIgnoreTypeTestPOJO("JsonIgnoreTypeTestPOJO.java", new EditorPopupMenuAction());
    }


    public void testJavaDoc() {
        docTestModel.testJsonIgnoreDocTestPOJO("JsonIgnoreDocTestPOJO.java", new EditorPopupMenuAction());
        docTestModel.testJsonIgnorePropertiesDocTestPOJO("JsonIgnorePropertiesDocTestPOJO.java", new EditorPopupMenuAction());
    }


    public void testJavaStaticField() {
        staticFieldTestModel.testStaticFieldPOJO("StaticFieldPOJO.java", new EditorPopupMenuAction());
    }


    public void testJavaMemberClass() {
        memberClassTestModel.testMemberClassTestPOJO("MemberClassTestPOJO.java", new EditorPopupMenuAction());
    }
}
