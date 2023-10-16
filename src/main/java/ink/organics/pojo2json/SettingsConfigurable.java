package ink.organics.pojo2json;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import ink.organics.pojo2json.parser.SettingsState;
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
        return !settingsComponent.getClassNameSpELEditor().getText().equals(settings.toProp()) ||
                !settingsComponent.getFieldNameSpELEditor().getText().equals(settings.fieldNameSpEL);
    }

    @Override
    public void apply() {
        SettingsState settings = SettingsState.getInstance();
        String prop = settingsComponent.getClassNameSpELEditor().getText();
        if (StringUtils.isBlank(prop)) {
            settings.classNameSpELMap = EvaluationContextFactory.initExpressionMap();
        } else {
            settings.classNameSpELMap = settings.fromProp(prop);
        }

        String prop2 = settingsComponent.getFieldNameSpELEditor().getText();
        if (StringUtils.isBlank(prop2)) {
            settings.fieldNameSpEL = EvaluationContextFactory.initJsonKeysExpression();
        } else {
            settings.fieldNameSpEL = prop2;
        }
    }

    @Override
    public void reset() {
        SettingsState settings = SettingsState.getInstance();
        settingsComponent.getClassNameSpELEditor().setText(settings.toProp());
        settingsComponent.getFieldNameSpELEditor().setText(settings.fieldNameSpEL);
    }


    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }
}
