package ink.organics.pojo2json;

import com.intellij.ide.scratch.ScratchFileService;
import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.ide.util.PsiNavigationSupport;
import com.intellij.json.JsonFileType;
import com.intellij.json.JsonLanguage;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtil;
import ink.organics.pojo2json.parser.KnownException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UastUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProjectViewPopupMenuAction extends POJO2JSONAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        final VirtualFile[] selectFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        boolean menuAllowed =
                selectFiles != null
                        && selectFiles.length > 0
                        && Arrays.stream(selectFiles).noneMatch(VirtualFile::isDirectory)
                        && project != null;

        e.getPresentation().setEnabledAndVisible(menuAllowed);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        final VirtualFile[] selectFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        if (selectFiles.length == 1) {
            pojo2jsonAction(PsiUtil.getPsiFile(project, selectFiles[0]));
            return;
        }

        final Map<String, String> warnMap = new LinkedHashMap<>();

        Arrays.stream(selectFiles)
                .map(virtualFile -> {

                    final PsiFile psiFile = PsiUtil.getPsiFile(project, virtualFile);
                    if (!uastSupported(psiFile)) {
                        warnMap.put(psiFile.getName(), "This file can't convert to json.");
                        return null;
                    }

                    final String fileText = psiFile.getText();
                    final int offset = fileText.contains("class") ? fileText.indexOf("class") : fileText.indexOf("record");

                    if (offset < 0) {
                        warnMap.put(psiFile.getName(), "Can't find class scope.");
                        return null;
                    }

                    PsiElement elementAt = psiFile.findElementAt(offset);
                    UClass uClass = UastUtils.findContaining(elementAt, UClass.class);
                    try {
                        String json = pojo2JSONParser.uElementToJSONString(uClass);

                        return ScratchRootType.getInstance().createScratchFile(
                                project,
                                psiFile.getName() + "." + JsonFileType.INSTANCE.getDefaultExtension(),
                                JsonLanguage.INSTANCE,
                                json,
                                ScratchFileService.Option.create_if_missing);

                    } catch (KnownException ex) {
                        warnMap.put(psiFile.getName(), ex.getMessage());
                        return null;
                    }

                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()) // 这里归集以结束上一个表达式，否则 findFirst 在表达式的优先级会被提前
                .stream()
                .findFirst()
                .ifPresent(virtualFile -> {
                    PsiNavigationSupport.getInstance().createNavigatable(project, virtualFile, 0).navigate(true);
                    Notifier.notifyInfo("Convert all POJO to JSON finish and created json files to Scratches folder.", project);
                });

        warnMap.keySet()
                .stream()
                .map(fileName -> fileName + "," + warnMap.get(fileName))
                .forEach(errorMsg -> Notifier.notifyWarn(errorMsg, project));
    }
}
