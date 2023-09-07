package ink.organics.pojo2json;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.SettingsCategory;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "ink.organics.pojo2json.SettingsState",
        storages = @Storage("Pojo2jsonPlugin.xml"),
        category = SettingsCategory.TOOLS)
public class SettingsState implements PersistentStateComponent<SettingsState> {


    public String userId = "John Q. Public";
    public boolean ideaStatus = false;

    public static SettingsState getInstance() {
        return ApplicationManager.getApplication().getService(SettingsState.class);
    }

    @Override
    public @Nullable SettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsState state) {
        System.out.println("loadState");
        XmlSerializerUtil.copyBean(state, this);
    }
}
