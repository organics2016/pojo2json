package ink.organics.pojo2json;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SettingsConfigurable implements Configurable {

    private SettingsComponent mySettingsComponent;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "xxxxhhhh";
    }

    @Override
    public @Nullable JComponent createComponent() {
        mySettingsComponent = new SettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        SettingsState settings = SettingsState.getInstance();
        boolean modified = !mySettingsComponent.getUserNameText().equals(settings.userId);
        modified |= mySettingsComponent.getIdeaUserStatus() != settings.ideaStatus;
        return modified;
    }

    @Override
    public void apply() {
        SettingsState settings = SettingsState.getInstance();
        settings.userId = mySettingsComponent.getUserNameText();
        settings.ideaStatus = mySettingsComponent.getIdeaUserStatus();
    }

    @Override
    public void reset() {
        SettingsState settings = SettingsState.getInstance();
        mySettingsComponent.setUserNameText(settings.userId);
        mySettingsComponent.setIdeaUserStatus(settings.ideaStatus);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
