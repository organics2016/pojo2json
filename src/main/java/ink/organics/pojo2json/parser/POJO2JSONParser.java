package ink.organics.pojo2json.parser;

import com.google.gson.GsonBuilder;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiUtil;
import ink.organics.pojo2json.parser.el.EvaluationContextFactory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UVariable;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;
import java.util.stream.Collectors;

public class POJO2JSONParser {

    private final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();

    private final List<String> iterableTypes = List.of(
            "java.lang.Iterable",
            "java.util.Collection",
            "java.util.List",
            "java.util.Set");

    private final ExpressionParser expressionParser = new SpelExpressionParser();
    private final ParserContext templateParserContext = new TemplateParserContext();

    public POJO2JSONParser() {
    }


    public String uElementToJSONString(@NotNull final UElement uElement,
                                       // {"Character":"#{#fieldName}_#{#uuid}_test"}
                                       @NotNull final Map<String, String> psiTypeExpression) {

        Object result = null;

        if (uElement instanceof UVariable) {
            UVariable variable = (UVariable) uElement;
            result = parseFieldValue((PsiVariable) variable.getJavaPsi(), null, 0, List.of(), getPsiClassGenerics(variable.getType()), psiTypeExpression);
        } else if (uElement instanceof UClass) {
            // UClass.getJavaPsi() IDEA 21* and last version recommend
            result = parseClass(((UClass) uElement).getJavaPsi(), 0, List.of(), Map.of(), psiTypeExpression);
        }

        return gsonBuilder.create().toJson(result);
    }

    private Map<String, Object> parseClass(PsiClass psiClass, int level, List<String> ignoreProperties, Map<String, PsiType> psiClassGenerics, Map<String, String> psiTypeExpression) {
        PsiAnnotation annotation = psiClass.getAnnotation(com.fasterxml.jackson.annotation.JsonIgnoreType.class.getName());
        if (annotation != null) {
            return null;
        }
        return Arrays.stream(psiClass.getAllFields())
                .map(field -> parseField(field, level, ignoreProperties, psiClassGenerics, psiTypeExpression))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (ov, nv) -> ov, LinkedHashMap::new));
    }


    private Map.Entry<String, Object> parseField(PsiField field, int level, List<String> ignoreProperties, Map<String, PsiType> psiClassGenerics, Map<String, String> psiTypeExpression) {
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
        Object fieldValue = parseFieldValue(field, null, level, ignoreProperties, psiClassGenerics, psiTypeExpression);
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
                                   Map<String, PsiType> psiClassGenerics,
                                   Map<String, String> psiTypeExpression) {

        level = ++level;

        PsiType type = deepType != null ? deepType : variable.getType();

        if (type instanceof PsiPrimitiveType) {       //primitive Type

            return getPrimitiveTypeValue(variable, type, psiTypeExpression);

        } else if (type instanceof PsiArrayType) {   //array type

            PsiType typeToDeepType = type.getDeepComponentType();
            Object obj = parseFieldValue(variable, typeToDeepType, level, ignoreProperties, getPsiClassGenerics(typeToDeepType), psiTypeExpression);
            return obj != null ? List.of(obj) : List.of();

        } else {    //reference Type

            PsiClass psiClass = PsiUtil.resolveClassInClassTypeOnly(type);

            if (psiClass == null) {
                return new LinkedHashMap<>();
            }

            if (psiClass.isEnum()) { // enum

                return Arrays.stream(psiClass.getAllFields())
                        .filter(psiField -> psiField instanceof PsiEnumConstant)
                        .findFirst()
                        .map(PsiField::getName)
                        .orElse("");

            } else {

                List<String> fieldTypeNames = new ArrayList<>();
                fieldTypeNames.add(psiClass.getQualifiedName());
                fieldTypeNames.addAll(Arrays.stream(psiClass.getSupers())
                        .map(PsiClass::getQualifiedName).toList());
                fieldTypeNames = fieldTypeNames.stream().filter(Objects::nonNull).toList();

                List<String> retain = new ArrayList<>(fieldTypeNames);
                retain.retainAll(psiTypeExpression.keySet());
                if (!retain.isEmpty()) {  // Object Test<String,String>
                    try {
                        Expression expression = expressionParser.parseExpression(psiTypeExpression.get(retain.get(0)), templateParserContext);
                        return expression.getValue(EvaluationContextFactory.newEvaluationContext(variable));
                    } catch (Exception e) {
                        throw new KnownException(e);
                    }
                } else {

                    boolean iterable = fieldTypeNames.stream().anyMatch(iterableTypes::contains);

                    if (iterable) {// Iterable List<Test<String>>

                        PsiType typeToDeepType = PsiUtil.extractIterableTypeParameter(type, false);
                        if (typeToDeepType == null) {
                            return List.of();
                        }
                        Object obj = parseFieldValue(variable, typeToDeepType, level, ignoreProperties, getPsiClassGenerics(typeToDeepType), psiTypeExpression);
                        return obj != null ? List.of(obj) : List.of();

                    } else {

                        if (level > 200) {
                            throw new KnownException("This class reference level exceeds maximum limit or has nested references!");
                        }

                        PsiType typeToDeepType = psiClassGenerics.get(psiClass.getName());
                        if (typeToDeepType != null) {
                            return parseFieldValue(variable, typeToDeepType, level, ignoreProperties, getPsiClassGenerics(typeToDeepType), psiTypeExpression);
                        }

                        return parseClass(psiClass, level, ignoreProperties, getPsiClassGenerics(type), psiTypeExpression);
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

    private Object getPrimitiveTypeValue(PsiVariable variable, PsiType type, Map<String, String> psiTypeExpression) {

        Expression expression;
        switch (type.getCanonicalText()) {
            case "boolean":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Boolean"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(variable));
            case "byte":
            case "short":
            case "int":
            case "long":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Number"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(variable));
            case "float":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Float"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(variable));
            case "double":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Double"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(variable));
            case "char":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Character"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(variable));
            default:
                return null;
        }
    }

}
