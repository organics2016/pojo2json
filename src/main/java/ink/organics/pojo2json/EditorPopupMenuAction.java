package ink.organics.pojo2json;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import ink.organics.pojo2json.parser.POJO2JSONParser;
import org.jetbrains.annotations.NotNull;

public abstract class EditorPopupMenuAction extends POJO2JSONAction {

    public EditorPopupMenuAction(POJO2JSONParser pojo2JSONParser) {
        super(pojo2JSONParser);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        boolean menuAllowed = false;
        if (psiFile != null && editor != null && project != null) {
            menuAllowed = uastSupported(psiFile);
        }
        e.getPresentation().setEnabledAndVisible(menuAllowed);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        pojo2jsonAction(psiFile, editor);
    }
}


