package ink.organics.pojo2json.parser.el;

import ink.organics.pojo2json.parser.model.POJOVariable;
import org.reflections.Reflections;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
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

    public static EvaluationContext newEvaluationContext(POJOVariable rootObject) {
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("field", rootObject);
        context.setVariable("boolean", presetTypeValueMap.get(BooleanTypeValue.class));
        context.setVariable("array", presetTypeValueMap.get(ArrayTypeValue.class));
        context.setVariable("decimal", presetTypeValueMap.get(DecimalTypeValue.class));
        context.setVariable("integer", presetTypeValueMap.get(IntegerTypeValue.class));
        context.setVariable("localdatetime", presetTypeValueMap.get(LocalDateTimeTypeValue.class));
        context.setVariable("localdate", presetTypeValueMap.get(LocalDateTypeValue.class));
        context.setVariable("localtime", presetTypeValueMap.get(LocalTimeTypeValue.class));
        context.setVariable("object", presetTypeValueMap.get(ObjectTypeValue.class));
        context.setVariable("temporal", presetTypeValueMap.get(TemporalTypeValue.class));
        context.setVariable("uuid", presetTypeValueMap.get(UUIDTypeValue.class));
        context.setVariable("shortuuid", presetTypeValueMap.get(ShortUUIDTypeValue.class));
        context.setVariable("yearmonth", presetTypeValueMap.get(YearMonthTypeValue.class));
        context.setVariable("zoneddatetime", presetTypeValueMap.get(ZonedDateTimeTypeValue.class));

        return context;
    }

    public static Map<String, String> initExpressionMap() {
        Map<String, String> map = new LinkedHashMap<>();

        map.put("com.fasterxml.jackson.databind.JsonNode", "#{#object.getValue()}");
        map.put("com.fasterxml.jackson.databind.node.ArrayNode", "#{#array.getValue()}");
        map.put("com.fasterxml.jackson.databind.node.ObjectNode", "#{#object.getValue()}");
        map.put("java.lang.Boolean", "#{#boolean.getValue()}");
        map.put("java.lang.CharSequence", "#{#field.getName() + '_' + #shortuuid.getValue()}");
        map.put("java.lang.Character", "#{'c'}");
        map.put("java.lang.Double", "#{#decimal.getValue()}");
        map.put("java.lang.Float", "#{#decimal.getValue()}");
        map.put("java.lang.Number", "#{#integer.getValue()}");
        map.put("java.math.BigDecimal", "#{#decimal.getValue()}");
        map.put("java.time.LocalDate", "#{#localdate.getValue()}");
        map.put("java.time.LocalDateTime", "#{#localdatetime.getValue()}");
        map.put("java.time.LocalTime", "#{#localtime.getValue()}");
        map.put("java.time.YearMonth", "#{#yearmonth.getValue()}");
        map.put("java.time.ZonedDateTime", "#{#zoneddatetime.getValue()}");
        map.put("java.time.temporal.Temporal", "#{#temporal.getValue()}");
        map.put("java.util.Date", "#{#localdatetime.getValue()}");
        map.put("java.util.UUID", "#{#uuid.getValue()}");

        return map;
    }
}
