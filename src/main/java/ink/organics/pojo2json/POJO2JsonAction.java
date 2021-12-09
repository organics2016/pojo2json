package ink.organics.pojo2json;

import com.google.gson.GsonBuilder;
import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import ink.organics.pojo2json.fake.*;
import kotlin.sequences.Sequence;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.uast.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public abstract class POJO2JsonAction extends AnAction {


    private final NotificationGroup notificationGroup =
            NotificationGroupManager.getInstance().getNotificationGroup("pojo2json.NotificationGroup");

    @NonNls
    private final Map<String, JsonFakeValuesService> normalTypes = new HashMap<>();

    private final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();

    public POJO2JsonAction() {

        FakeDecimal fakeDecimal = new FakeDecimal();
        FakeDateTime fakeDateTime = new FakeDateTime();

        normalTypes.put("Boolean", new FakeBoolean());
        normalTypes.put("Float", fakeDecimal);
        normalTypes.put("Double", fakeDecimal);
        normalTypes.put("BigDecimal", fakeDecimal);
        normalTypes.put("Number", new FakeInteger());
        normalTypes.put("Character", new FakeChar());
        normalTypes.put("CharSequence", new FakeString());
        normalTypes.put("Date", fakeDateTime);
        normalTypes.put("Temporal", new FakeTemporal());
        normalTypes.put("LocalDateTime", fakeDateTime);
        normalTypes.put("LocalDate", new FakeDate());
        normalTypes.put("LocalTime", new FakeTime());
        normalTypes.put("ZonedDateTime", new FakeZonedDateTime());
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        Project project = e.getProject();
        PsiElement elementAt = psiFile.findElementAt(editor.getCaretModel().getOffset());

        // ADAPTS to all JVM platform languages
        UElement uElement = UastContextKt.toUElement(elementAt, UElement.class);
        try {

            if (uElement == null) {
                throw new KnownException("Can't find class scope, move the cursor over the class name.");
            }
            UClass uClass =  UastUtils.getContainingUClass(uElement);

            if (uClass == null) {
                throw new KnownException("Can't find class scope, move the cursor within the class scope.");
            }

            Map<String, Object> kv = parseClass(uClass.getJavaPsi(), 0, List.of());
            String json = gsonBuilder.create().toJson(kv);
            StringSelection selection = new StringSelection(json);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            String message = "Convert " + uClass.getName() + " to JSON success, copied to clipboard.";
            Notification success = notificationGroup.createNotification(message, NotificationType.INFORMATION);
            Notifications.Bus.notify(success, project);


        } catch (KnownException ex) {
            Notification warn = notificationGroup.createNotification(ex.getMessage(), NotificationType.WARNING);
            Notifications.Bus.notify(warn, project);
        } catch (Exception ex) {
            Notification error = notificationGroup.createNotification("Convert to JSON failed. " + ex.getMessage(), NotificationType.ERROR);
            Notifications.Bus.notify(error, project);
        }
    }


    protected abstract Object getFakeValue(JsonFakeValuesService jsonFakeValuesService);


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
        if (ignoreProperties.contains(field.getName())) {
            return null;
        }

        PsiAnnotation annotation = field.getAnnotation(com.fasterxml.jackson.annotation.JsonIgnore.class.getName());
        if (annotation != null) {
            return null;
        }

        PsiDocComment docComment = field.getDocComment();
        if (docComment != null) {
            ignoreProperties = POJO2JsonPsiUtils.docTextToList("@JsonIgnoreProperties", docComment.getText());
        } else {
            annotation = field.getAnnotation(com.fasterxml.jackson.annotation.JsonIgnoreProperties.class.getName());
            if (annotation != null) {
                ignoreProperties = POJO2JsonPsiUtils.arrayTextToList(annotation.findAttributeValue("value").getText());
            }
        }

        String fieldKey = parseFieldKey(field);
        Object fieldValue = parseFieldValue(field, level, ignoreProperties);
        if (fieldValue == null) {
            return null;
        }
        return Map.entry(fieldKey, fieldValue);
    }

    private String parseFieldKey(PsiField field) {
        PsiAnnotation annotation = field.getAnnotation(com.fasterxml.jackson.annotation.JsonProperty.class.getName());
        if (annotation != null) {
            String fieldName = POJO2JsonPsiUtils.psiTextToString(annotation.findAttributeValue("value").getText());
            if (StringUtils.isNotBlank(fieldName)) {
                return fieldName;
            }
        }

        annotation = field.getAnnotation("com.alibaba.fastjson.annotation.JSONField");
        if (annotation != null) {
            String fieldName = POJO2JsonPsiUtils.psiTextToString(annotation.findAttributeValue("name").getText());
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

                for (PsiField field : psiClass.getFields()) {
                    if (field instanceof PsiEnumConstant) {
                        return field.getName();
                    }
                }
                return "";

            } else {

                List<String> fieldTypeNames = new ArrayList<>();

                PsiType[] types = type.getSuperTypes();

                fieldTypeNames.add(type.getPresentableText());
                fieldTypeNames.addAll(Arrays.stream(types).map(PsiType::getPresentableText).collect(Collectors.toList()));

                if (fieldTypeNames.stream().anyMatch(s -> s.startsWith("Collection") || s.startsWith("Iterable"))) {// Iterable

                    PsiType deepType = PsiUtil.extractIterableTypeParameter(type, false);
                    Object obj = parseFieldValueType(deepType, level, ignoreProperties);
                    return obj != null ? List.of(obj) : List.of();

                } else { // Object

                    List<String> retain = new ArrayList<>(fieldTypeNames);
                    retain.retainAll(normalTypes.keySet());
                    if (!retain.isEmpty()) {
                        return this.getFakeValue(normalTypes.get(retain.get(0)));
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


    public Object getPrimitiveTypeValue(PsiType type) {
        switch (type.getCanonicalText()) {
            case "boolean":
                return this.getFakeValue(normalTypes.get("Boolean"));
            case "byte":
            case "short":
            case "int":
            case "long":
                return this.getFakeValue(normalTypes.get("Number"));
            case "float":
            case "double":
                return this.getFakeValue(normalTypes.get("BigDecimal"));
            case "char":
                return this.getFakeValue(normalTypes.get("Character"));
            default:
                return null;
        }
    }

}


