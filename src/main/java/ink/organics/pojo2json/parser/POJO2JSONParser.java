package ink.organics.pojo2json.parser;

import com.google.gson.GsonBuilder;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiUtil;
import ink.organics.pojo2json.parser.type.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UVariable;

import java.util.*;
import java.util.stream.Collectors;

public abstract class POJO2JSONParser {


    private final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();

    private final Map<String, SpecifyType> specifyTypes = new HashMap<>();

    private final List<String> iterableTypes = List.of(
            "java.lang.Iterable",
            "java.util.Collection",
            "java.util.List",
            "java.util.Set");

    public POJO2JSONParser() {

        DecimalType decimalType = new DecimalType();
        LocalDateTimeType localDateTimeType = new LocalDateTimeType();
        ObjectType objectType = new ObjectType();

        specifyTypes.put("java.lang.Boolean", new BooleanType());
        specifyTypes.put("java.lang.Float", decimalType);
        specifyTypes.put("java.lang.Double", decimalType);
        specifyTypes.put("java.math.BigDecimal", decimalType);
        specifyTypes.put("java.lang.Number", new IntegerType());
        specifyTypes.put("java.lang.Character", new CharType());
        specifyTypes.put("java.lang.CharSequence", new StringType());
        specifyTypes.put("java.util.Date", localDateTimeType);
        specifyTypes.put("java.time.temporal.Temporal", new TemporalType());
        specifyTypes.put("java.time.LocalDateTime", localDateTimeType);
        specifyTypes.put("java.time.LocalDate", new LocalDateType());
        specifyTypes.put("java.time.LocalTime", new LocalTimeType());
        specifyTypes.put("java.time.ZonedDateTime", new ZonedDateTimeType());
        specifyTypes.put("java.time.YearMonth", new YearMonthType());
        specifyTypes.put("java.util.UUID", new UUIDType());
        specifyTypes.put("com.fasterxml.jackson.databind.JsonNode", objectType);
        specifyTypes.put("com.fasterxml.jackson.databind.node.ObjectNode", objectType);
        specifyTypes.put("com.fasterxml.jackson.databind.node.ArrayNode", new ArrayType());
    }

    protected abstract Object getFakeValue(SpecifyType specifyType);

    public String uElementToJSONString(@NotNull final UElement uElement,
                                       // {"Character":"#{#fieldName}_#{#uuid}_test"}
                                       final Map<String, String> psiTypeExpression) {

        return null;
    }

    public String uElementToJSONString(@NotNull final UElement uElement) {

        Object result = null;

        if (uElement instanceof UVariable) {
            UVariable variable = (UVariable) uElement;
            result = parseFieldValue((PsiVariable) variable.getJavaPsi(), null, 0, List.of(), getPsiClassGenerics(variable.getType()));
        } else if (uElement instanceof UClass) {
            // UClass.getJavaPsi() IDEA 21* and last version recommend
            result = parseClass(((UClass) uElement).getJavaPsi(), 0, List.of(), Map.of());
        }

        return gsonBuilder.create().toJson(result);
    }

    private Map<String, Object> parseClass(PsiClass psiClass, int level, List<String> ignoreProperties, Map<String, PsiType> psiClassGenerics) {
        PsiAnnotation annotation = psiClass.getAnnotation(com.fasterxml.jackson.annotation.JsonIgnoreType.class.getName());
        if (annotation != null) {
            return null;
        }
        return Arrays.stream(psiClass.getAllFields())
                .map(field -> parseField(field, level, ignoreProperties, psiClassGenerics))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (ov, nv) -> ov, LinkedHashMap::new));
    }


    private Map.Entry<String, Object> parseField(PsiField field, int level, List<String> ignoreProperties, Map<String, PsiType> psiClassGenerics) {
        // 移除所有 static 属性，这其中包括 kotlin 中的 companion object 和 INSTANCE
        if (field.hasModifierProperty(PsiModifier.STATIC)) {
            return null;
        }

        if (ignoreProperties.contains(field.getName())) {
            return null;
        }

        PsiAnnotation annotation = field.getAnnotation(com.fasterxml.jackson.annotation.JsonIgnore.class.getName());
        if (annotation != null) {
            return null;
        }

        PsiDocComment docComment = field.getDocComment();
        if (docComment != null) {
            PsiDocTag psiDocTag = docComment.findTagByName("JsonIgnore");
            if (psiDocTag != null && "JsonIgnore".equals(psiDocTag.getName())) {
                return null;
            }

            ignoreProperties = POJO2JSONParserUtils.docTextToList("@JsonIgnoreProperties", docComment.getText());
        } else {
            annotation = field.getAnnotation(com.fasterxml.jackson.annotation.JsonIgnoreProperties.class.getName());
            if (annotation != null) {
                ignoreProperties = POJO2JSONParserUtils.arrayTextToList(annotation.findAttributeValue("value").getText());
            }
        }

        String fieldKey = parseFieldKey(field);
        if (fieldKey == null) {
            return null;
        }
        Object fieldValue = parseFieldValue(field, null, level, ignoreProperties, psiClassGenerics);
        if (fieldValue == null) {
            return null;
        }
        return Map.entry(fieldKey, fieldValue);
    }

    private String parseFieldKey(PsiField field) {

        PsiAnnotation annotation = field.getAnnotation(com.fasterxml.jackson.annotation.JsonProperty.class.getName());
        if (annotation != null) {
            String fieldName = POJO2JSONParserUtils.psiTextToString(annotation.findAttributeValue("value").getText());
            if (StringUtils.isNotBlank(fieldName)) {
                return fieldName;
            }
        }

        annotation = field.getAnnotation("com.alibaba.fastjson.annotation.JSONField");
        if (annotation != null) {
            String fieldName = POJO2JSONParserUtils.psiTextToString(annotation.findAttributeValue("name").getText());
            if (StringUtils.isNotBlank(fieldName)) {
                return fieldName;
            }
        }
        return field.getName();
    }

    /**
     * 解析变量值
     *
     * @param variable         当前变量
     * @param deepType         当前变量的深度类型，如果为null则deepType就是当前variable的Type
     * @param level            当前转换层级。当递归层级过深时会导致stack overflow，这个参数用于控制递归层级
     * @param ignoreProperties 过滤的属性，这个参数只在这里使用 {@link ink.organics.pojo2json.parser.POJO2JSONParser#parseField}
     *                         用于过滤用户指定移除的属性
     * @param psiClassGenerics 当前PsiType的Class所拥有的泛型Map，Map中包含当前PsiClass所定义的 泛型 和 泛型对应的用户指定类型 (E=CustomObject)
     *                         并在解析当前PsiClass所包含的Field时，尝试获取这个Field所定义的泛型Map，然后传入下一层
     * @return JSON Value所期望的Object
     */
    private Object parseFieldValue(PsiVariable variable,
                                   PsiType deepType,
                                   int level,
                                   List<String> ignoreProperties,
                                   Map<String, PsiType> psiClassGenerics) {

        level = ++level;

        PsiType type = deepType != null ? deepType : variable.getType();

        if (type instanceof PsiPrimitiveType) {       //primitive Type

            return getPrimitiveTypeValue(type);

        } else if (type instanceof PsiArrayType) {   //array type

            PsiType typeToDeepType = type.getDeepComponentType();
            Object obj = parseFieldValue(variable, typeToDeepType, level, ignoreProperties, getPsiClassGenerics(typeToDeepType));
            return obj != null ? List.of(obj) : List.of();

        } else {    //reference Type

            PsiClass psiClass = PsiUtil.resolveClassInClassTypeOnly(type);

            if (psiClass == null) {
                return new LinkedHashMap<>();
            }

            if (psiClass.isEnum()) { // enum

                return this.getFakeValue(new EnumType(psiClass));

            } else {

                List<String> fieldTypeNames = new ArrayList<>();
                fieldTypeNames.add(psiClass.getQualifiedName());
                fieldTypeNames.addAll(Arrays.stream(psiClass.getSupers())
                        .map(PsiClass::getQualifiedName).collect(Collectors.toList()));
                fieldTypeNames = fieldTypeNames.stream().filter(Objects::nonNull).collect(Collectors.toList());

                List<String> retain = new ArrayList<>(fieldTypeNames);
                retain.retainAll(specifyTypes.keySet());
                if (!retain.isEmpty()) {  // Object Test<String,String>
                    return this.getFakeValue(specifyTypes.get(retain.get(0)));
                } else {

                    boolean iterable = fieldTypeNames.stream().anyMatch(iterableTypes::contains);

                    if (iterable) {// Iterable List<Test<String>>

                        PsiType typeToDeepType = PsiUtil.extractIterableTypeParameter(type, false);
                        Object obj = parseFieldValue(variable, typeToDeepType, level, ignoreProperties, getPsiClassGenerics(typeToDeepType));
                        return obj != null ? List.of(obj) : List.of();

                    } else {

                        if (level > 500) {
                            throw new KnownException("This class reference level exceeds maximum limit or has nested references!");
                        }

                        PsiType typeToDeepType = psiClassGenerics.get(psiClass.getName());
                        if (typeToDeepType != null) {
                            return parseFieldValue(variable, typeToDeepType, level, ignoreProperties, getPsiClassGenerics(typeToDeepType));
                        }

                        return parseClass(psiClass, level, ignoreProperties, getPsiClassGenerics(type));
                    }
                }

            }
        }
    }

    private Map<String, PsiType> getPsiClassGenerics(PsiType type) {
        PsiClass psiClass = PsiUtil.resolveClassInClassTypeOnly(type);
        if (psiClass != null) {
            return Arrays.stream(psiClass.getTypeParameters())
                    .filter(p -> PsiUtil.substituteTypeParameter(type, psiClass, p.getIndex(), false) != null)
                    .collect(Collectors.toMap(NavigationItem::getName,
                            p -> PsiUtil.substituteTypeParameter(type, psiClass, p.getIndex(), false)));
        }
        return Map.of();
    }

    private Object getPrimitiveTypeValue(PsiType type) {
        switch (type.getCanonicalText()) {
            case "boolean":
                return this.getFakeValue(specifyTypes.get("java.lang.Boolean"));
            case "byte":
            case "short":
            case "int":
            case "long":
                return this.getFakeValue(specifyTypes.get("java.lang.Number"));
            case "float":
            case "double":
                return this.getFakeValue(specifyTypes.get("java.math.BigDecimal"));
            case "char":
                return this.getFakeValue(specifyTypes.get("java.lang.Character"));
            default:
                return null;
        }
    }

}
