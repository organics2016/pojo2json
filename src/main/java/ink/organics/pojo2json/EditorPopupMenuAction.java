package ink.organics.pojo2json;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import ink.organics.pojo2json.parser.KnownException;
import ink.organics.pojo2json.parser.POJO2JSONParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UastLanguagePlugin;
import org.jetbrains.uast.UastUtils;

public abstract class EditorPopupMenuAction extends AnAction {

    private final POJO2JSONParser pojo2JSONParser;

    public EditorPopupMenuAction(POJO2JSONParser pojo2JSONParser) {
        this.pojo2JSONParser = pojo2JSONParser;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        boolean menuAllowed = false;
        if (psiFile != null && editor != null && project != null) {
            menuAllowed = UastLanguagePlugin.Companion.getInstances()
                    .stream()
                    .anyMatch(l -> l.isFileSupported(psiFile.getName()));
        }
        e.getPresentation().setEnabledAndVisible(menuAllowed);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        final String fileText = psiFile.getText();

        PsiElement elementAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
        // ADAPTS to all JVM platform languages
        UClass uClass = UastUtils.findContaining(elementAt, UClass.class);
        if (uClass == null) {
            int offset = fileText.contains("class") ? fileText.indexOf("class") : fileText.indexOf("record");
            if (offset < 0) {
                Notifier.notifyWarn("Can't find class scope.", project);
                return;
            }
            elementAt = psiFile.findElementAt(offset);
            uClass = UastUtils.findContaining(elementAt, UClass.class);
        }

        try {
            String json = pojo2JSONParser.psiClassToJSONString(uClass.getJavaPsi());

            ClipboardHandler.copyToClipboard(json);

            Notifier.notifyInfo("Convert " + uClass.getName() + " to JSON success, copied to clipboard.", project);

        } catch (KnownException ex) {
            Notifier.notifyWarn(ex.getMessage(), project);
        }
    }
}


