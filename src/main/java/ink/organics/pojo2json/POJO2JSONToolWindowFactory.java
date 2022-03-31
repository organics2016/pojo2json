package ink.organics.pojo2json;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class POJO2JSONToolWindowFactory implements ToolWindowFactory {


    @Override
    public boolean isApplicable(@NotNull Project project) {
        return ToolWindowFactory.super.isApplicable(project);
    }

    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        EditorTextField editorTextField = new EditorTextField(project, JsonFileType.INSTANCE);
        editorTextField.setOneLineMode(false);
        editorTextField.setFontInheritedFromLAF(false);
        Content content = ContentFactory.SERVICE.getInstance().createContent(editorTextField, null, false);
        toolWindow.getContentManager().addContent(content);
    }
}
