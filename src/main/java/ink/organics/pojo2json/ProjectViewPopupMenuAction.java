package ink.organics.pojo2json;

import com.intellij.ide.scratch.ScratchFileService;
import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.ide.util.PsiNavigationSupport;
import com.intellij.json.JsonFileType;
import com.intellij.json.JsonLanguage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtil;
import ink.organics.pojo2json.parser.KnownException;
import ink.organics.pojo2json.parser.POJO2JSONParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UastLanguagePlugin;
import org.jetbrains.uast.UastUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ProjectViewPopupMenuAction extends AnAction {

    private final POJO2JSONParser pojo2JSONParser;

    public ProjectViewPopupMenuAction(POJO2JSONParser pojo2JSONParser) {
        this.pojo2JSONParser = pojo2JSONParser;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        final VirtualFile[] selectFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        boolean menuAllowed = selectFiles != null && selectFiles.length > 0 && project != null;

        e.getPresentation().setEnabledAndVisible(menuAllowed);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        final VirtualFile[] selectFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        final Map<String, String> warnMap = new LinkedHashMap<>();

        List<VirtualFile> efficientFiles = Arrays.stream(selectFiles)
                .filter(virtualFile ->
                        UastLanguagePlugin.Companion.getInstances().stream().anyMatch(l -> l.isFileSupported(virtualFile.getName())))
                .map(virtualFile -> {

                    final PsiFile psiFile = PsiUtil.getPsiFile(project, virtualFile);
                    final String fileText = psiFile.getText();
                    int offset = fileText.contains("class") ? fileText.indexOf("class") : fileText.indexOf("record");

                    if (offset < 0) {
                        warnMap.put(virtualFile.getName(), "Can't find class scope.");
                        return null;
                    }

                    PsiElement elementAt = psiFile.findElementAt(offset);
                    UClass uClass = UastUtils.findContaining(elementAt, UClass.class);
                    try {
                        String json = pojo2JSONParser.psiClassToJSONString(uClass.getJavaPsi());

                        return ScratchRootType.getInstance().createScratchFile(
                                project,
                                psiFile.getName() + "." + JsonFileType.INSTANCE.getDefaultExtension(),
                                JsonLanguage.INSTANCE,
                                json,
                                ScratchFileService.Option.create_if_missing);

                    } catch (KnownException ex) {
                        warnMap.put(virtualFile.getName(), ex.getMessage());
                        return null;
                    }

                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()); // 这里归集以结束上一个表达式，否则 findFirst 在表达式的优先级会被提前


        if (efficientFiles.isEmpty()) {
            Notifier.notifyWarn("No convertible files.", project);
        } else {
            VirtualFile virtualFile = efficientFiles.get(0);

            try {
                ClipboardHandler.copyToClipboard(new String(virtualFile.contentsToByteArray(), StandardCharsets.UTF_8));
            } catch (IOException ex) {
                ex.printStackTrace();
                Notifier.notifyError(ex.getMessage(), project);
            }

            if (efficientFiles.size() > 1) {
                PsiNavigationSupport.getInstance().createNavigatable(project, virtualFile, 0).navigate(true);
            }

            Notifier.notifyInfo("Convert all POJO to JSON success and create json files to Scratches folder, " + virtualFile.getName() + " copied to clipboard.", project);
        }

        warnMap.keySet()
                .stream()
                .map(fileName -> fileName + "," + warnMap.get(fileName))
                .forEach(errorMsg -> Notifier.notifyWarn(errorMsg, project));
    }
}
