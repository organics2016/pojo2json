package ink.organics.pojo2json.test;

import ink.organics.pojo2json.EditorPopupMenuDefaultAction;

public class KotlinTestCase extends MyTestCase {


    @Override
    protected String getTestDataPath() {
        return "src/test/java/testdata/kotlin";
    }


    public void testKotlinVariable() {
        variableTestModel.testVariableTestPOJO("VariableTestPOJO.kt", new EditorPopupMenuDefaultAction());
    }

    public void testKotlinDateType() {
        dataTypeTestModel.testPrimitiveTestPOJO("PrimitiveTestPOJO.kt", new EditorPopupMenuDefaultAction());
        dataTypeTestModel.testPrimitiveArrayTestPOJO("PrimitiveArrayTestPOJO.kt", new EditorPopupMenuDefaultAction());
        dataTypeTestModel.testEnumTestPOJO("EnumTestPOJO.kt", new EditorPopupMenuDefaultAction());
        dataTypeTestModel.testIterableTestPOJO("IterableTestPOJO.kt", new EditorPopupMenuDefaultAction());
        dataTypeTestModel.testGenericTestPOJO("GenericTestPOJO.kt", new EditorPopupMenuDefaultAction());
        dataTypeTestModel.testSpecialObjectTestPOJO("SpecialObjectTestPOJO.kt", new EditorPopupMenuDefaultAction());
    }


    public void testKotlinAnnotation() {
        annotationTestModel.testJsonPropertyTestPOJO("JsonPropertyTestPOJO.kt", new EditorPopupMenuDefaultAction());
        annotationTestModel.testJsonIgnoreTestPOJO("JsonIgnoreTestPOJO.kt", new EditorPopupMenuDefaultAction());
        annotationTestModel.testJsonIgnorePropertiesTestPOJO("JsonIgnorePropertiesTestPOJO.kt", new EditorPopupMenuDefaultAction());
        annotationTestModel.testJsonIgnoreTypeTestPOJO("JsonIgnoreTypeTestPOJO.kt", new EditorPopupMenuDefaultAction());
    }


//    public void testKotlinDoc() {
//        docTestModel.testJsonIgnoreDocTestPOJO("JsonIgnoreDocTestPOJO.kt", new POJO2JsonDefaultAction());
//        docTestModel.testJsonIgnorePropertiesDocTestPOJO("JsonIgnorePropertiesDocTestPOJO.kt", new POJO2JsonDefaultAction());
//    }


    public void testKotlinStaticField() {
        staticFieldTestModel.testStaticFieldPOJO("StaticFieldPOJO.kt", new EditorPopupMenuDefaultAction());
    }

    public void testKotlinMemberClass() {
        memberClassTestModel.testMemberClassTestPOJO("MemberClassTestPOJO.kt", new EditorPopupMenuDefaultAction());
    }
}
