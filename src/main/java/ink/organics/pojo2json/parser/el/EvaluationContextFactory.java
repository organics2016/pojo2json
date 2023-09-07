package ink.organics.pojo2json.parser.el;

import com.intellij.psi.PsiVariable;
import org.reflections.Reflections;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;

public class EvaluationContextFactory {

    private static final Map<Class<?>, PresetTypeValue> presetTypeValueMap;

    static {

        Reflections reflections = new Reflections(PresetTypeValue.class.getPackageName());

        presetTypeValueMap = reflections.getSubTypesOf(PresetTypeValue.class)
                .stream()
                .filter(c -> !RandomTypeValue.class.equals(c))
                .collect(Collectors.toMap(c -> c, c -> {
                    try {
                        return c.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }

    public static EvaluationContext newEvaluationContext(PsiVariable rootObject) {
        EvaluationContext context = new StandardEvaluationContext(rootObject);
        context.setVariable("boolean", presetTypeValueMap.get(BooleanTypeValue.class));
        context.setVariable("arr", presetTypeValueMap.get(ArrayTypeValue.class));

//        if (rootObject.isEnum()) {
//            context.setVariable("enum",);
//        }
        return context;
    }
}
