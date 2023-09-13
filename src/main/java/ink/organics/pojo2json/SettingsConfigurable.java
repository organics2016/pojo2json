package ink.organics.pojo2json;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import ink.organics.pojo2json.parser.el.EvaluationContextFactory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SettingsConfigurable implements Configurable {

    private SettingsComponent settingsComponent;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "POJO To JSON";
    }

    @Override
    public @Nullable JComponent createComponent() {
        settingsComponent = new SettingsComponent();
        return settingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        SettingsState settings = SettingsState.getInstance();
        return !settingsComponent.getTextField().getText().equals(settings.toProp());
    }

    @Override
    public void apply() {
        SettingsState settings = SettingsState.getInstance();
        String prop = settingsComponent.getTextField().getText();
        if (StringUtils.isBlank(prop)) {
            settings.classNameSpELMap = EvaluationContextFactory.initExpressionMap();
        } else {
            settings.classNameSpELMap = settings.fromProp(settingsComponent.getTextField().getText());
        }
    }

    @Override
    public void reset() {
        SettingsState settings = SettingsState.getInstance();
        settingsComponent.getTextField().setText(settings.toProp());
    }


    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }
}
