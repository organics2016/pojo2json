package ink.organics.pojo2json.test;


import ink.organics.pojo2json.POJO2JsonDefaultAction;
import ink.organics.pojo2json.test.model.AnnotationTestModel;
import ink.organics.pojo2json.test.model.DataTypeTestModel;
import ink.organics.pojo2json.test.model.StaticFieldTestModel;

public class JavaTestCase extends TestCase {


    private final DataTypeTestModel dataTypeTestModel = new DataTypeTestModel(this);

    private final AnnotationTestModel annotationTestModel = new AnnotationTestModel(this);

    private final StaticFieldTestModel staticFieldTestModel = new StaticFieldTestModel(this);


    @Override
    protected String getTestDataPath() {
        return "src/test/java/testdata/java";
    }

    public void testThis() {
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
        annotationTestModel.testJsonIgnorePropertiesDocTestPOJO("JsonIgnorePropertiesDocTestPOJO.java", new POJO2JsonDefaultAction());
        annotationTestModel.testJsonIgnoreTypeTestPOJO("JsonIgnoreTypeTestPOJO.java", new POJO2JsonDefaultAction());
    }


    public void testJavaStaticField() {
        staticFieldTestModel.testStaticFieldPOJO("StaticFieldPOJO.java", new POJO2JsonDefaultAction());
    }

}
