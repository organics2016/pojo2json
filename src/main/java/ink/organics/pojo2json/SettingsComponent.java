package ink.organics.pojo2json;

import com.intellij.lang.properties.PropertiesLanguage;
import com.intellij.ui.LanguageTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;

import javax.swing.*;

/**
 * https://jetbrains.github.io/ui/
 */
public class SettingsComponent {

    private final JPanel panel;

    private final LanguageTextField textField;

    public SettingsComponent() {

        textField = new LanguageTextField(PropertiesLanguage.INSTANCE, null, "", false);

        String comment = """
                <p>
                This is a .properties configuration.
                The Key is a Reference Class Path, and Value is a <a href="https://docs.spring.io/spring-framework/reference/core/expressions.html">SpEL expression.</a>
                When the plugin convert POJO into JSON, it will map the parsing result of the SpEL expression to the specified Class according to this configuration.
                <a href="https://github.com/organics2016/pojo2json#usage">More details.</a>
                </p>
                """;
        panel = FormBuilder.createFormBuilder()
                .addComponentFillVertically(textField, 0)
                .addComponent(
                        UI.PanelFactory.panel(new JPanel())
                                .withComment(comment)
                                .createPanel()
                )
                .getPanel();
    }

    public JPanel getPanel() {
        return panel;
    }

    public LanguageTextField getTextField() {
        return textField;
    }
}
