package ink.organics.pojo2json;

import com.intellij.lang.properties.PropertiesLanguage;
import com.intellij.ui.LanguageTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;

import javax.swing.*;


public class SettingsComponent {

    private final JPanel panel;

    private final LanguageTextField textField;

    public SettingsComponent() {

        textField = new LanguageTextField(PropertiesLanguage.INSTANCE, null, "", false);

        panel = FormBuilder.createFormBuilder()
                .addComponentFillVertically(textField, 0)
                .addComponent(
                        UI.PanelFactory.panel(new JPanel())
                                .withComment("<p>Please note that this will not include personal data or any sensitive information, such as source code, file names, etc. The data sent complies with the <a href=\"http://jetbrains.com\">JetBrains Privacy Policy</a></p>")
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
