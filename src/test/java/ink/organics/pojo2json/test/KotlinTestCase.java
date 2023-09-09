package ink.organics.pojo2json.test;

import ink.organics.pojo2json.EditorPopupMenuAction;

public class KotlinTestCase extends MyTestCase {


    @Override
    protected String getTestDataPath() {
        return "src/test/java/testdata/kotlin";
    }


    public void testKotlinVariable() {
        variableTestModel.testVariableTestPOJO("VariableTestPOJO.kt", new EditorPopupMenuAction());
    }

    public void testKotlinDateType() {
        dataTypeTestModel.testPrimitiveTestPOJO("PrimitiveTestPOJO.kt", new EditorPopupMenuAction());
        dataTypeTestModel.testPrimitiveArrayTestPOJO("PrimitiveArrayTestPOJO.kt", new EditorPopupMenuAction());
        dataTypeTestModel.testEnumTestPOJO("EnumTestPOJO.kt", new EditorPopupMenuAction());
        dataTypeTestModel.testIterableTestPOJO("IterableTestPOJO.kt", new EditorPopupMenuAction());
        dataTypeTestModel.testGenericTestPOJO("GenericTestPOJO.kt", new EditorPopupMenuAction());
        dataTypeTestModel.testSpecialObjectTestPOJO("SpecialObjectTestPOJO.kt", new EditorPopupMenuAction());
    }


    public void testKotlinAnnotation() {
        annotationTestModel.testJsonPropertyTestPOJO("JsonPropertyTestPOJO.kt", new EditorPopupMenuAction());
        annotationTestModel.testJsonIgnoreTestPOJO("JsonIgnoreTestPOJO.kt", new EditorPopupMenuAction());
        annotationTestModel.testJsonIgnorePropertiesTestPOJO("JsonIgnorePropertiesTestPOJO.kt", new EditorPopupMenuAction());
        annotationTestModel.testJsonIgnoreTypeTestPOJO("JsonIgnoreTypeTestPOJO.kt", new EditorPopupMenuAction());
    }


//    public void testKotlinDoc() {
//        docTestModel.testJsonIgnoreDocTestPOJO("JsonIgnoreDocTestPOJO.kt", new POJO2JsonDefaultAction());
//        docTestModel.testJsonIgnorePropertiesDocTestPOJO("JsonIgnorePropertiesDocTestPOJO.kt", new POJO2JsonDefaultAction());
//    }


    public void testKotlinStaticField() {
        staticFieldTestModel.testStaticFieldPOJO("StaticFieldPOJO.kt", new EditorPopupMenuAction());
    }

    public void testKotlinMemberClass() {
        memberClassTestModel.testMemberClassTestPOJO("MemberClassTestPOJO.kt", new EditorPopupMenuAction());
    }
}
