package ink.organics.pojo2json;

import com.intellij.lang.properties.PropertiesLanguage;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.LanguageTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;

import javax.swing.*;

/**
 * <a href="https://jetbrains.github.io/ui/">...</a>
 */
public class SettingsComponent {

    private final JPanel panel;

    private final LanguageTextField classNameSpELEditor;

    private final EditorTextField fieldNameSpELEditor;


    public SettingsComponent() {

        classNameSpELEditor = new LanguageTextField(PropertiesLanguage.INSTANCE, null, "", false);
        fieldNameSpELEditor = new EditorTextField();

        String comment = """
                <p>
                This is a .properties configuration.
                The Key is Reference Class, and Value is <a href="https://docs.spring.io/spring-framework/reference/core/expressions.html">SpEL expression.</a>
                Clear the configuration and save, the default configuration will be initialized.
                <a href="https://github.com/organics2016/pojo2json#configure-spel-expression">More details.</a>
                </p>
                """;

        panel = FormBuilder.createFormBuilder()
                .addComponentFillVertically(classNameSpELEditor, 0)
                .addComponent(
                        UI.PanelFactory.panel(new JPanel())
                                .withComment(comment)
                                .createPanel()
                )
                .addVerticalGap(10)
                .addComponent(
                        UI.PanelFactory.panel(fieldNameSpELEditor)
                                .withLabel("Global JSON keys style")
                                .withComment("""
                                        <p>
                                        <a href="https://github.com/organics2016/pojo2json#json-keys-format-configuration">More details.</a>
                                        </p>
                                        """)
                                .createPanel()
                )
                .getPanel();

    }

    public JPanel getPanel() {
        return panel;
    }

    public LanguageTextField getClassNameSpELEditor() {
        return classNameSpELEditor;
    }

    public EditorTextField getFieldNameSpELEditor() {
        return fieldNameSpELEditor;
    }
}
