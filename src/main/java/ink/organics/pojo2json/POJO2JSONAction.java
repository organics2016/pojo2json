package ink.organics.pojo2json;

import com.intellij.json.JsonFileType;
import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import ink.organics.pojo2json.parser.KnownException;
import ink.organics.pojo2json.parser.POJO2JSONParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UastLanguagePlugin;
import org.jetbrains.uast.UastUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public abstract class POJO2JSONAction extends AnAction {


    private final NotificationGroup notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("pojo2json.NotificationGroup");

    private final POJO2JSONParser pojo2JSONParser;

    public POJO2JSONAction(POJO2JSONParser pojo2JSONParser) {
        this.pojo2JSONParser = pojo2JSONParser;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        boolean menuAllowed = false;
        if (psiFile != null && editor != null && project != null) {
            menuAllowed = UastLanguagePlugin.Companion.getInstances().stream().anyMatch(l -> l.isFileSupported(psiFile.getName()));
        }
        e.getPresentation().setEnabledAndVisible(menuAllowed);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        try {
            final Editor editor = e.getData(CommonDataKeys.EDITOR);
            final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
            final String fileText = psiFile.getText();

            PsiElement elementAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
            // ADAPTS to all JVM platform languages
            UClass uClass = UastUtils.findContaining(elementAt, UClass.class);
            if (uClass == null) {
                int offset = fileText.contains("class") ? fileText.indexOf("class") : fileText.indexOf("record");
                if (offset < 0) {
                    throw new KnownException("Can't find class scope.");
                }
                elementAt = psiFile.findElementAt(offset);
                uClass = UastUtils.findContaining(elementAt, UClass.class);
            }

            String json = pojo2JSONParser.psiClassToJSONString(uClass.getJavaPsi());

            outputClipboard(json);
            outputToolWindow(json, project, editor, uClass);

            String message = "Convert " + uClass.getName() + " to JSON success, copied to clipboard.";
            Notification success = notificationGroup.createNotification(message, NotificationType.INFORMATION);
            Notifications.Bus.notify(success, project);

        } catch (KnownException ex) {
            Notification warn = notificationGroup.createNotification(ex.getMessage(), NotificationType.WARNING);
            Notifications.Bus.notify(warn, project);
        } catch (Exception ex) {
            Notification error = notificationGroup.createNotification("Convert to JSON failed. " + ex.getMessage(), NotificationType.ERROR);
            Notifications.Bus.notify(error, project);
        }
    }

    private void outputClipboard(String jsonResult) {
        StringSelection selection = new StringSelection(jsonResult);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    private void outputToolWindow(String jsonResult, Project project, Editor editor, UClass uClass) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("POJO to JSON");
        if (toolWindow == null) {
            return;
        }

        EditorTextField editorTextField = new EditorTextField(jsonResult, project, JsonFileType.INSTANCE);
        editorTextField.setFontInheritedFromLAF(false);
        editorTextField.setFont(editor.getContentComponent().getFont());

        ContentManager contentManager = toolWindow.getContentManager();
        Content content = contentManager.findContent(uClass.getName());
        if (content != null) {
            content.setComponent(editorTextField);
            contentManager.setSelectedContent(content);
        } else {
            // TODO 这里有个问题。第一次创建的content 无法设置焦点
            content = ContentFactory.SERVICE.getInstance().createContent(editorTextField, uClass.getName(), false);
            contentManager.addContent(content);
        }
        contentManager.requestFocus(content, true);
    }
}


