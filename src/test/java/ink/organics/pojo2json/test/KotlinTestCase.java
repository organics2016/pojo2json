package ink.organics.pojo2json.test;

import ink.organics.pojo2json.POJO2JsonDefaultAction;
import ink.organics.pojo2json.test.model.AnnotationTestModel;
import ink.organics.pojo2json.test.model.DataTypeTestModel;

public class KotlinTestCase extends TestCase {

    private final DataTypeTestModel dataTypeTestModel = new DataTypeTestModel(this);

    private final AnnotationTestModel annotationTestModel = new AnnotationTestModel(this);


    @Override
    protected String getTestDataPath() {
        return "src/test/java/testdata/kotlin";
    }


    public void testIt() {
        annotationTestModel.testJsonPropertyTestPOJO("JsonPropertyTestPOJO.kt", new POJO2JsonDefaultAction());
    }


    public void testKotlinDateType() {
        dataTypeTestModel.testPrimitiveTestPOJO("PrimitiveTestPOJO.kt", new POJO2JsonDefaultAction());
        dataTypeTestModel.testPrimitiveArrayTestPOJO("PrimitiveArrayTestPOJO.kt", new POJO2JsonDefaultAction());
        dataTypeTestModel.testEnumTestPOJO("EnumTestPOJO.kt", new POJO2JsonDefaultAction());
        dataTypeTestModel.testIterableTestPOJO("IterableTestPOJO.kt", new POJO2JsonDefaultAction());
        dataTypeTestModel.testGenericTestPOJO("GenericTestPOJO.kt", new POJO2JsonDefaultAction());
        dataTypeTestModel.testSpecialObjectTestPOJO("SpecialObjectTestPOJO.kt", new POJO2JsonDefaultAction());
    }


    public void testKotlinAnnotation() {
        annotationTestModel.testJsonPropertyTestPOJO("JsonPropertyTestPOJO.kt", new POJO2JsonDefaultAction());
        annotationTestModel.testJsonIgnoreTestPOJO("JsonIgnoreTestPOJO.kt", new POJO2JsonDefaultAction());
        annotationTestModel.testJsonIgnorePropertiesTestPOJO("JsonIgnorePropertiesTestPOJO.kt", new POJO2JsonDefaultAction());
        annotationTestModel.testJsonIgnorePropertiesDocTestPOJO("JsonIgnorePropertiesDocTestPOJO.kt", new POJO2JsonDefaultAction());
        annotationTestModel.testJsonIgnoreTypeTestPOJO("JsonIgnoreTypeTestPOJO.kt", new POJO2JsonDefaultAction());
    }

}
