package ink.organics.pojo2json;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import ink.organics.pojo2json.parser.KnownException;
import ink.organics.pojo2json.parser.POJO2JSONParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.*;

public abstract class POJO2JSONAction extends AnAction {

    protected final POJO2JSONParser pojo2JSONParser;

    public POJO2JSONAction(POJO2JSONParser pojo2JSONParser) {
        this.pojo2JSONParser = pojo2JSONParser;
    }


    public void pojo2jsonAction(@NotNull final PsiFile psiFile) {
        pojo2jsonAction(psiFile, null, null);
    }

    public void pojo2jsonAction(@NotNull final PsiFile psiFile,
                                @Nullable final Editor editor,
                                @Nullable final PsiElement psiElement) {
        final Project project = psiFile.getProject();

        if (!uastSupported(psiFile)) {
            Notifier.notifyWarn("This file can't convert to json.", project);
            return;
        }

        UElement uElement = null;
        if (psiElement != null) {
            uElement = UastContextKt.toUElement(psiElement, UVariable.class);
        }

        if (uElement == null) {
            if (editor != null) {
                PsiElement elementAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
                uElement = UastUtils.findContaining(elementAt, UClass.class);
            }
        }

        if (uElement == null) {
            String fileText = psiFile.getText();
            int offset = fileText.contains("class") ? fileText.indexOf("class") : fileText.indexOf("record");
            if (offset < 0) {
                Notifier.notifyWarn("Can't find class scope.", project);
                return;
            }
            PsiElement elementAt = psiFile.findElementAt(offset);
            uElement = UastUtils.findContaining(elementAt, UClass.class);
        }

        try {
            String json = pojo2JSONParser.uElementToJSONString(uElement);

            ClipboardHandler.copyToClipboard(json);


            Notifier.notifyInfo("Convert " + psiFile.getName() + " to JSON success, copied to clipboard.", project);

        } catch (KnownException ex) {
            Notifier.notifyWarn(ex.getMessage(), project);
        }
    }


    public boolean uastSupported(@NotNull final PsiFile psiFile) {
        return UastLanguagePlugin.Companion.getInstances()
                .stream()
                .anyMatch(l -> l.isFileSupported(psiFile.getName()));
    }
}
