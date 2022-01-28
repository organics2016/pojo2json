package ink.organics.pojo2json.test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.JavaAwareProjectJdkTableImpl;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import ink.organics.pojo2json.test.model.AnnotationTestModel;
import ink.organics.pojo2json.test.model.DataTypeTestModel;
import ink.organics.pojo2json.test.model.DocTestModel;
import ink.organics.pojo2json.test.model.StaticFieldTestModel;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public abstract class TestCase extends BasePlatformTestCase {


    protected final ObjectMapper objectMapper = new ObjectMapper();


    protected final DataTypeTestModel dataTypeTestModel = new DataTypeTestModel(this);

    protected final AnnotationTestModel annotationTestModel = new AnnotationTestModel(this);

    protected final DocTestModel docTestModel = new DocTestModel(this);

    protected final StaticFieldTestModel staticFieldTestModel = new StaticFieldTestModel(this);


    public TestCase() {
        // https://github.com/FasterXML/jackson-databind/issues/2087
        this.objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        this.objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        this.objectMapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected abstract String getTestDataPath();

    @Override
    protected LightProjectDescriptor getProjectDescriptor() {
        return new LightProjectDescriptor() {
            @Override
            public Sdk getSdk() {
                return JavaAwareProjectJdkTableImpl.getInstanceEx().getInternalJdk();
            }
        };
    }

//    @Override
//    protected LightProjectDescriptor getProjectDescriptor() {
//        return new DefaultLightProjectDescriptor() {
//            @Override
//            public Sdk getSdk() {
//                Sdk sdk = IdeaTestUtil.getMockJdk(JavaVersion.compose(11));
//                System.out.println(sdk);
//                return sdk;
//            }
//
//            @Override
//            public void configureModule(@NotNull Module module, @NotNull ModifiableRootModel model, @NotNull ContentEntry contentEntry) {
//                MavenDependencyUtil.addFromMaven(model, "com.alibaba:fastjson:1.2.76");
//                model.getModuleExtension(LanguageLevelModuleExtension.class).setLanguageLevel(LanguageLevel.JDK_1_8);
//            }
//        };
//    }

    public JsonNode testAction(@NotNull String fileName, @NotNull AnAction action) {

        // Open file and simulate user cursor position to class scope.
        myFixture.configureByFile(fileName);

        myFixture.testAction(action);

        Transferable result = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        try {
            String jsonStr = String.valueOf(result.getTransferData(DataFlavor.stringFlavor));
            JsonNode jsonNode = objectMapper.readTree(jsonStr);
            System.out.println(jsonNode.toPrettyString());
            return jsonNode;
        } catch (UnsupportedFlavorException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
