package ink.organics.pojo2json.test;

import ink.organics.pojo2json.POJO2JSONDefaultAction;

public class KotlinTestCase extends TestCase {


    @Override
    protected String getTestDataPath() {
        return "src/test/java/testdata/kotlin";
    }


    public void testKotlinDateType() {
        dataTypeTestModel.testPrimitiveTestPOJO("PrimitiveTestPOJO.kt", new POJO2JSONDefaultAction());
        dataTypeTestModel.testPrimitiveArrayTestPOJO("PrimitiveArrayTestPOJO.kt", new POJO2JSONDefaultAction());
        dataTypeTestModel.testEnumTestPOJO("EnumTestPOJO.kt", new POJO2JSONDefaultAction());
        dataTypeTestModel.testIterableTestPOJO("IterableTestPOJO.kt", new POJO2JSONDefaultAction());
        dataTypeTestModel.testGenericTestPOJO("GenericTestPOJO.kt", new POJO2JSONDefaultAction());
        dataTypeTestModel.testSpecialObjectTestPOJO("SpecialObjectTestPOJO.kt", new POJO2JSONDefaultAction());
    }


    public void testKotlinAnnotation() {
        annotationTestModel.testJsonPropertyTestPOJO("JsonPropertyTestPOJO.kt", new POJO2JSONDefaultAction());
        annotationTestModel.testJsonIgnoreTestPOJO("JsonIgnoreTestPOJO.kt", new POJO2JSONDefaultAction());
        annotationTestModel.testJsonIgnorePropertiesTestPOJO("JsonIgnorePropertiesTestPOJO.kt", new POJO2JSONDefaultAction());
        annotationTestModel.testJsonIgnoreTypeTestPOJO("JsonIgnoreTypeTestPOJO.kt", new POJO2JSONDefaultAction());
    }


//    public void testKotlinDoc() {
//        docTestModel.testJsonIgnoreDocTestPOJO("JsonIgnoreDocTestPOJO.kt", new POJO2JsonDefaultAction());
//        docTestModel.testJsonIgnorePropertiesDocTestPOJO("JsonIgnorePropertiesDocTestPOJO.kt", new POJO2JsonDefaultAction());
//    }


    public void testKotlinStaticField() {
        staticFieldTestModel.testStaticFieldPOJO("StaticFieldPOJO.kt", new POJO2JSONDefaultAction());
    }

    public void testKotlinMemberClass() {
        memberClassTestModel.testMemberClassTestPOJO("MemberClassTestPOJO.kt", new POJO2JSONDefaultAction());
    }
}
