package com.organics.javabean2json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NonNls;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.*;
import java.util.List;

public class JavaBean2JsonAction extends AnAction {


    private static NotificationGroup notificationGroup;

    @NonNls
    private static final Map<String, Object> normalTypes = new HashMap<>();

    static {
        notificationGroup = new NotificationGroup("javabean2json.NotificationGroup", NotificationDisplayType.BALLOON, true);

        normalTypes.put("Boolean", false);
        normalTypes.put("Number", 0);
        normalTypes.put("String", "");
        normalTypes.put("Date", "");
        normalTypes.put("LocalDateTime", "");
        normalTypes.put("LocalDate", "");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {

        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        Project project = e.getProject();
        PsiElement elementAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass selectedClass = PsiTreeUtil.getContextOfType(elementAt, PsiClass.class);
        try {
            Map<String, Object> kv = getFields(selectedClass);
            String json = JSON.toJSONString(kv, SerializerFeature.PrettyFormat);
            StringSelection selection = new StringSelection(json);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            String message = "Convert " + selectedClass.getName() + " to JSON success, copied to clipboard.";
            Notification success = notificationGroup.createNotification(message, NotificationType.INFORMATION);
            Notifications.Bus.notify(success, project);
        } catch (Exception ex) {
            Notification error = notificationGroup.createNotification("Convert to JSON failed.", NotificationType.ERROR);
            Notifications.Bus.notify(error, project);
        }
    }


    public static PsiType getSuperTypes(PsiType type) {

        PsiType[] types = type.getSuperTypes();
        if (types.length == 0) {
            return type;
        } else {
            PsiType superTypes = types[0];
            if (superTypes.getPresentableText().startsWith("Object")) {
                return type;
            } else {
                return getSuperTypes(superTypes);
            }
        }
    }

    public static Map<String, Object> getFields(PsiClass psiClass) {
        Map<String, Object> map = new LinkedHashMap<>();

        if (psiClass == null) {
            return map;
        }

        for (PsiField field : psiClass.getAllFields()) {
            PsiType type = field.getType();
            String name = field.getName();

            if (type instanceof PsiPrimitiveType) {       //primitive Type
                map.put(name, PsiTypesUtil.getDefaultValue(type));
            } else if (type instanceof PsiArrayType) {   //array type
                List<Object> list = new ArrayList<>();
                PsiType deepType = type.getDeepComponentType();
                String deepTypeName = deepType.getPresentableText();

                if (deepType instanceof PsiPrimitiveType) {
                    list.add(PsiTypesUtil.getDefaultValue(deepType));
                } else if (normalTypes.containsKey(deepTypeName)) {
                    list.add(normalTypes.get(deepTypeName));
                } else {
                    list.add(getFields(PsiUtil.resolveClassInType(deepType)));
                }

                map.put(name, list);

            } else {    //reference Type
                PsiType superTypes = getSuperTypes(type);
                String fieldTypeName = superTypes.getPresentableText();

                if (normalTypes.containsKey(fieldTypeName)) {    //normal Type
                    map.put(name, normalTypes.get(fieldTypeName));

                } else if (fieldTypeName.startsWith("Iterable")) {   //list type

                    PsiType iterableType = PsiUtil.extractIterableTypeParameter(type, false);
                    PsiClass iterableClass = PsiUtil.resolveClassInClassTypeOnly(iterableType);
                    List<Object> list = new ArrayList<>();
                    String classTypeName = iterableClass.getName();
                    if (normalTypes.containsKey(classTypeName)) {
                        list.add(normalTypes.get(classTypeName));
                    } else {
                        list.add(getFields(iterableClass));
                    }
                    map.put(name, list);

                } else {    //class type
                    map.put(name, getFields(PsiUtil.resolveClassInType(type)));
                }
            }
        }

        return map;
    }
}
