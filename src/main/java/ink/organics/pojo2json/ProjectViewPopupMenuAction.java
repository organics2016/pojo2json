package ink.organics.pojo2json;

import com.intellij.ide.scratch.ScratchFileService;
import com.intellij.ide.scratch.ScratchRootType;
import com.intellij.ide.util.PsiNavigationSupport;
import com.intellij.json.JsonFileType;
import com.intellij.json.JsonLanguage;
import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class ProjectViewPopupMenuAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(ProjectViewPopupMenuAction.class);

    private final NotificationGroup notificationGroup = NotificationGroupManager.getInstance()
            .getNotificationGroup("pojo2json.NotificationGroup");

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

        Arrays.stream(selectFiles)
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
                .collect(Collectors.toList()) // 这里归集以结束上一个表达式，否则 findFirst 在表达式的优先级会被提前
                .stream()
                .findFirst()
                .ifPresent(virtualFile ->
                        PsiNavigationSupport.getInstance().createNavigatable(project, virtualFile, 0).navigate(true));

        if (!warnMap.isEmpty()) {
            warnMap.keySet()
                    .stream()
                    .map(name -> name + "," + warnMap.get(name))
                    .forEach(errorMsg -> {
                        Notification warn = notificationGroup.createNotification(errorMsg, NotificationType.WARNING);
                        Notifications.Bus.notify(warn, project);
                    });
        }
    }
}
