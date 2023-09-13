package ink.organics.pojo2json;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.SettingsCategory;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import ink.organics.pojo2json.parser.el.EvaluationContextFactory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@State(name = "ink.organics.pojo2json.SettingsState",
        storages = @Storage("Pojo2jsonPlugin.xml"),
        category = SettingsCategory.TOOLS)
public class SettingsState implements PersistentStateComponent<SettingsState> {

    public Map<String, String> classNameSpELMap;

    public SettingsState() {
        classNameSpELMap = EvaluationContextFactory.initExpressionMap();
    }

    public static SettingsState getInstance() {
        return ApplicationManager.getApplication().getService(SettingsState.class);
    }

    @Override
    public @Nullable SettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public Map<String, String> fromProp(@NotNull String prop) {
        if (StringUtils.isBlank(prop)) {
            return new LinkedHashMap<>();
        }
        return Arrays.stream(prop.split("\n"))
                .filter(StringUtils::isNotBlank)
                .map(s -> s.split("="))
                .collect(Collectors.toMap(s -> s[0], s -> s[1], (ov, nv) -> ov, LinkedHashMap::new));
    }

    public String toProp() {
        if (classNameSpELMap.isEmpty()) {
            return "";
        }
        return classNameSpELMap.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));
    }
}
