package ink.organics.pojo2json.parser;

import com.google.gson.GsonBuilder;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiUtil;
import ink.organics.pojo2json.parser.el.EvaluationContextFactory;
import ink.organics.pojo2json.parser.model.POJOClass;
import ink.organics.pojo2json.parser.model.POJOField;
import ink.organics.pojo2json.parser.model.POJOVariable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
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
            "java.util.AbstractCollection",
            "java.util.List",
            "java.util.AbstractList",
            "java.util.Set",
            "java.util.AbstractSet");

    private final ExpressionParser expressionParser = new SpelExpressionParser();

    private final ParserContext templateParserContext = new TemplateParserContext();

    private final SettingsState settingsState = SettingsState.getInstance();

    public POJO2JSONParser() {
    }


    public String uElementToJSONString(@NotNull final UElement uElement) {

        Object result = null;

        if (uElement instanceof UVariable variable) {
            result = parseFieldValue(POJOVariable.init((PsiVariable) variable.getJavaPsi(), getPsiClassGenerics(variable.getType())));
        } else if (uElement instanceof UClass) {
            // UClass.getJavaPsi() IDEA 21* and last version recommend
            result = parseClass(POJOClass.init(((UClass) uElement).getJavaPsi()));
        }

        return gsonBuilder.create().toJson(result);
    }

    private Map<String, Object> parseClass(POJOClass pojoClass) {
        PsiClass psiClass = pojoClass.getPsiClass();

        PsiAnnotation annotation = psiClass.getAnnotation(com.fasterxml.jackson.annotation.JsonIgnoreType.class.getName());
        if (annotation != null) {
            return null;
        }
        return Arrays.stream(psiClass.getAllFields())
                .map(field -> parseField(pojoClass.toField(field)))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (ov, nv) -> ov, LinkedHashMap::new));
    }


    private Map.Entry<String, Object> parseField(POJOField pojoField) {
        PsiField field = pojoField.getPsiField();
        // 移除所有 static 属性，这其中包括 kotlin 中的 companion object 和 INSTANCE
        if (field.hasModifierProperty(PsiModifier.STATIC)) {
            return null;
        }

        if (pojoField.getIgnoreProperties().contains(field.getName())) {
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

            pojoField.setIgnoreProperties(POJO2JSONParserUtils.docTextToList("@JsonIgnoreProperties", docComment.getText()));
        } else {
            annotation = field.getAnnotation(com.fasterxml.jackson.annotation.JsonIgnoreProperties.class.getName());
            if (annotation != null) {
                pojoField.setIgnoreProperties(POJO2JSONParserUtils.arrayTextToList(annotation.findAttributeValue("value").getText()));
            }
        }

        String fieldKey = parseFieldKey(pojoField);
        if (fieldKey == null) {
            return null;
        }
        pojoField.setName(fieldKey);

        Object fieldValue = parseFieldValue(pojoField);
        if (fieldValue == null) {
            return null;
        }
        return Map.entry(fieldKey, fieldValue);
    }

    private String parseFieldKey(POJOField pojoField) {
        PsiField field = pojoField.getPsiField();

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

        Expression expression = expressionParser.parseExpression(settingsState.fieldNameSpEL, templateParserContext);
        return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoField), String.class);
    }

    private Object parseFieldValue(POJOVariable pojoVariable) {

        PsiType type = pojoVariable.getPsiType();
        Map<String, String> psiTypeExpression = settingsState.classNameSpELMap;

        if (type instanceof PsiPrimitiveType) {       //primitive Type

            return getPrimitiveTypeValue(pojoVariable, type, psiTypeExpression);

        } else if (type instanceof PsiArrayType) {   //array type

            PsiType typeToDeepType = type.getDeepComponentType();
            Object obj = parseFieldValue(pojoVariable.deepVariable(typeToDeepType, getPsiClassGenerics(typeToDeepType)));
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
                        return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoVariable));
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
                        Object obj = parseFieldValue(pojoVariable.deepVariable(typeToDeepType, getPsiClassGenerics(typeToDeepType)));
                        return obj != null ? List.of(obj) : List.of();

                    } else {

                        if (pojoVariable.getRecursionLevel() > 200) {
                            throw new KnownException("This class reference level exceeds maximum limit or has nested references!");
                        }

                        PsiType typeToDeepType = pojoVariable.getPsiClassGenerics().get(psiClass.getName());
                        if (typeToDeepType != null) {
                            return parseFieldValue(pojoVariable.deepVariable(typeToDeepType, getPsiClassGenerics(typeToDeepType)));
                        }

                        return parseClass(pojoVariable.deepClass(psiClass, getPsiClassGenerics(type)));
                    }
                }

            }
        }
    }

    private Map<String, PsiType> getPsiClassGenerics(PsiType type) {
        PsiClass psiClass = PsiUtil.resolveClassInClassTypeOnly(type);
        if (psiClass != null) {
            return Arrays.stream(psiClass.getTypeParameters())
                    .map(p -> Pair.of(p, PsiUtil.substituteTypeParameter(type, psiClass, p.getIndex(), false)))
                    .filter(p -> p.getValue() != null)
                    .collect(Collectors.toMap(p -> p.getKey().getName(), Pair::getValue));
        }
        return Map.of();
    }

    private Object getPrimitiveTypeValue(POJOVariable pojoVariable, PsiType type, Map<String, String> psiTypeExpression) {

        Expression expression;
        switch (type.getCanonicalText()) {
            case "boolean":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Boolean"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoVariable));
            case "byte":
            case "short":
            case "int":
            case "long":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Number"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoVariable));
            case "float":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Float"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoVariable));
            case "double":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Double"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoVariable));
            case "char":
                expression = expressionParser.parseExpression(psiTypeExpression.get("java.lang.Character"), templateParserContext);
                return expression.getValue(EvaluationContextFactory.newEvaluationContext(pojoVariable));
            default:
                return null;
        }
    }

}
