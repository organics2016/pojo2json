package ink.organics.pojo2json;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.JavaAwareProjectJdkTableImpl;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.TestDataProvider;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class TestPOJO extends BasePlatformTestCase {




    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/java/testdata";
    }

    @Override
    public PsiFile createLightFile(String fileName, Language language, String text) {
        return super.createLightFile(fileName, language, text);
    }

    public void testF() throws IOException, UnsupportedFlavorException {

        AnAction action = new POJO2JsonDefaultAction();
        myFixture.configureByFiles("TestPOJO2.java");


        int offset = myFixture.findElementByText("class", PsiClass.class).getTextOffset();
        myFixture.getEditor().getCaretModel().moveToOffset(offset);
        DataContext context = TestDataProvider.withRules(myFixture.getProject());


        AnActionEvent e = AnActionEvent.createFromDataContext(ActionPlaces.EDITOR_POPUP, null, context);
        action.actionPerformed(e);

        Transferable result =  Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        System.out.println(result.getTransferData(DataFlavor.stringFlavor));

    }

    @Override
    protected LightProjectDescriptor getProjectDescriptor() {

        return new LightProjectDescriptor() {
            @Override
            public Sdk getSdk() {
                return JavaAwareProjectJdkTableImpl.getInstanceEx().getInternalJdk();
            }
        };
    }
}
