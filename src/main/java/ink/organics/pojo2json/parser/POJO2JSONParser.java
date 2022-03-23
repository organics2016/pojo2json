package ink.organics.pojo2json.parser;

import com.google.gson.GsonBuilder;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiUtil;
import ink.organics.pojo2json.parser.type.*;
import org.apache.commons.lang3.StringUtils;

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

    public String psiClassToJSONString(PsiClass psiClass) {
        Map<String, Object> kv = parseClass(psiClass, 0, List.of());
        return gsonBuilder.create().toJson(kv);
    }

    private Map<String, Object> parseClass(PsiClass psiClass, int level, List<String> ignoreProperties) {
        PsiAnnotation annotation = psiClass.getAnnotation(com.fasterxml.jackson.annotation.JsonIgnoreType.class.getName());
        if (annotation != null) {
            return null;
        }
        return Arrays.stream(psiClass.getAllFields())
                .map(field -> parseField(field, level, ignoreProperties))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (ov, nv) -> ov, LinkedHashMap::new));
    }


    private Map.Entry<String, Object> parseField(PsiField field, int level, List<String> ignoreProperties) {
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
        Object fieldValue = parseFieldValue(field, level, ignoreProperties);
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

    private Object parseFieldValue(PsiField field, int level, List<String> ignoreProperties) {
        return parseFieldValueType(field.getType(), level, ignoreProperties);
    }

    private Object parseFieldValueType(PsiType type, int level, List<String> ignoreProperties) {

        level = ++level;

        if (type instanceof PsiPrimitiveType) {       //primitive Type

            return getPrimitiveTypeValue(type);

        } else if (type instanceof PsiArrayType) {   //array type

            PsiType deepType = type.getDeepComponentType();
            Object obj = parseFieldValueType(deepType, level, ignoreProperties);
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

                fieldTypeNames.add(type.getPresentableText());
                fieldTypeNames.addAll(Arrays.stream(type.getSuperTypes())
                        .map(PsiType::getPresentableText).collect(Collectors.toList()));


                boolean iterable = fieldTypeNames.stream().map(typeName -> {
                    int subEnd = typeName.indexOf("<");
                    return typeName.substring(0, subEnd > 0 ? subEnd : typeName.length());
                }).anyMatch(iterableTypes::contains);

                if (iterable) {// Iterable

                    PsiType deepType = PsiUtil.extractIterableTypeParameter(type, false);
                    Object obj = parseFieldValueType(deepType, level, ignoreProperties);
                    return obj != null ? List.of(obj) : List.of();

                } else { // Object

                    List<String> retain = new ArrayList<>(fieldTypeNames);
                    retain.retainAll(specifyTypes.keySet());
                    if (!retain.isEmpty()) {
                        return this.getFakeValue(specifyTypes.get(retain.get(0)));
                    } else {

                        if (level > 500) {
                            throw new KnownException("This class reference level exceeds maximum limit or has nested references!");
                        }

                        return parseClass(psiClass, level, ignoreProperties);
                    }
                }
            }
        }
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
