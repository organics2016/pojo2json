package ink.organics.pojo2json.test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.IdeaTestUtil;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import ink.organics.pojo2json.test.model.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public abstract class TestCase extends LightJavaCodeInsightFixtureTestCase {

    public static final DefaultLightProjectDescriptor MOCK_JAVA_11 = new DefaultLightProjectDescriptor(IdeaTestUtil::getMockJdk11)
            .withRepositoryLibrary("com.alibaba:fastjson:1.2.76")
            .withRepositoryLibrary("com.fasterxml.jackson.core:jackson-annotations:2.11.0");

    protected final ObjectMapper objectMapper = new ObjectMapper();


    protected final DataTypeTestModel dataTypeTestModel = new DataTypeTestModel(this);

    protected final AnnotationTestModel annotationTestModel = new AnnotationTestModel(this);

    protected final DocTestModel docTestModel = new DocTestModel(this);

    protected final StaticFieldTestModel staticFieldTestModel = new StaticFieldTestModel(this);

    protected final MemberClassTestModel memberClassTestModel = new MemberClassTestModel(this);


    protected final VariableTestModel variableTestModel = new VariableTestModel(this);

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
    protected @NotNull LightProjectDescriptor getProjectDescriptor() {
        return MOCK_JAVA_11;
    }

    public JsonNode testAction(@NotNull String fileName, @NotNull AnAction action) {
        return this.testAction(fileName, action, "class");
    }

    public JsonNode testAction(@NotNull String fileName, @NotNull AnAction action, String cursorPositionByText) {

        // Open file and simulate user cursor position to class scope.
        myFixture.configureByFile(fileName);
        PsiElement psiElement = myFixture.findElementByText(cursorPositionByText, PsiElement.class);
        int offset = psiElement.getTextOffset();
        myFixture.getEditor().getCaretModel().moveToOffset(offset);

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
