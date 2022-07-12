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
            "Iterable",
            "Collection",
            "List",
            "Set");

    public POJO2JSONParser() {

        DecimalType decimalType = new DecimalType();
        LocalDateTimeType localDateTimeType = new LocalDateTimeType();

        specifyTypes.put("Boolean", new BooleanType());
        specifyTypes.put("Float", decimalType);
        specifyTypes.put("Double", decimalType);
        specifyTypes.put("BigDecimal", decimalType);
        specifyTypes.put("Number", new IntegerType());
        specifyTypes.put("Character", new CharType());
        specifyTypes.put("CharSequence", new StringType());
        specifyTypes.put("Date", localDateTimeType);
        specifyTypes.put("Temporal", new TemporalType());
        specifyTypes.put("LocalDateTime", localDateTimeType);
        specifyTypes.put("LocalDate", new LocalDateType());
        specifyTypes.put("LocalTime", new LocalTimeType());
        specifyTypes.put("ZonedDateTime", new ZonedDateTimeType());
        specifyTypes.put("YearMonth", new YearMonthType());
        specifyTypes.put("UUID", new UUIDType());
    }

    protected abstract Object getFakeValue(SpecifyType specifyType);

    public String uElementToJSONString(@NotNull final UElement uElement) {

        Object result = null;

        if (uElement instanceof UVariable) {
            PsiType type = ((UVariable) uElement).getType();
            result = parseFieldValueType(type, 0, List.of(), getPsiClassGenerics(type));
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
        Object fieldValue = parseFieldValue(field, level, ignoreProperties, psiClassGenerics);
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

    private Object parseFieldValue(PsiField field, int level, List<String> ignoreProperties, Map<String, PsiType> psiClassGenerics) {
        return parseFieldValueType(field.getType(), level, ignoreProperties, psiClassGenerics);
    }

    private Object parseFieldValueType(PsiType type,
                                       int level,
                                       List<String> ignoreProperties,
                                       Map<String, PsiType> psiClassGenerics) {

        level = ++level;

        if (type instanceof PsiPrimitiveType) {       //primitive Type

            return getPrimitiveTypeValue(type);

        } else if (type instanceof PsiArrayType) {   //array type

            PsiType deepType = type.getDeepComponentType();
            Object obj = parseFieldValueType(deepType, level, ignoreProperties, getPsiClassGenerics(deepType));
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

                fieldTypeNames.add(psiClass.getName());
                fieldTypeNames.addAll(Arrays.stream(psiClass.getSupers())
                        .map(PsiClass::getName).collect(Collectors.toList()));

                boolean iterable = fieldTypeNames.stream().anyMatch(iterableTypes::contains);

                if (iterable) {// Iterable List<Test<String>>

                    PsiType deepType = PsiUtil.extractIterableTypeParameter(type, false);
                    Object obj = parseFieldValueType(deepType, level, ignoreProperties, getPsiClassGenerics(deepType));
                    return obj != null ? List.of(obj) : List.of();

                } else { // Object Test<String,String>

                    List<String> retain = new ArrayList<>(fieldTypeNames);
                    retain.retainAll(specifyTypes.keySet());
                    if (!retain.isEmpty()) {
                        return this.getFakeValue(specifyTypes.get(retain.get(0)));
                    } else {

                        if (level > 500) {
                            throw new KnownException("This class reference level exceeds maximum limit or has nested references!");
                        }

                        PsiType deepType = psiClassGenerics.get(psiClass.getName());
                        if (deepType != null) {
                            return parseFieldValueType(deepType, level, ignoreProperties, getPsiClassGenerics(deepType));
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
                    .collect(Collectors.toMap(NavigationItem::getName,
                            p -> PsiUtil.substituteTypeParameter(type, psiClass, p.getIndex(), false)));
        }
        return Map.of();
    }

    private Object getPrimitiveTypeValue(PsiType type) {
        switch (type.getCanonicalText()) {
            case "boolean":
                return this.getFakeValue(specifyTypes.get("Boolean"));
            case "byte":
            case "short":
            case "int":
            case "long":
                return this.getFakeValue(specifyTypes.get("Number"));
            case "float":
            case "double":
                return this.getFakeValue(specifyTypes.get("BigDecimal"));
            case "char":
                return this.getFakeValue(specifyTypes.get("Character"));
            default:
                return null;
        }
    }

}
