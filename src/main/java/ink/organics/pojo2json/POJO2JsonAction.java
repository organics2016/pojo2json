package ink.organics.pojo2json;

import com.google.gson.GsonBuilder;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class POJO2JsonAction extends AnAction {


    private static final NotificationGroup notificationGroup =
            new NotificationGroup("pojo2json.NotificationGroup", NotificationDisplayType.BALLOON, true);

    @NonNls
    private static final Map<String, Object> normalTypes = new HashMap<>();

    private static final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();

    static {

        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        normalTypes.put("Boolean", false);
        normalTypes.put("Number", 0);
        normalTypes.put("CharSequence", "");
        normalTypes.put("Date", dateTime);
        normalTypes.put("Temporal", dateTime);
        normalTypes.put("LocalDateTime", dateTime);
        normalTypes.put("LocalDate", now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        normalTypes.put("LocalTime", now.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
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
            String json = gsonBuilder.create().toJson(kv);
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

    private static Map<String, Object> getFields(PsiClass psiClass) {
        Map<String, Object> map = new LinkedHashMap<>();

        if (psiClass == null) {
            return map;
        }

        for (PsiField field : psiClass.getAllFields()) {
            map.put(field.getName(), typeResolve(field.getType()));
        }

        return map;
    }


    private static Object typeResolve(PsiType type) {


        if (type instanceof PsiPrimitiveType) {       //primitive Type

            return PsiTypesUtil.getDefaultValue(type);

        } else if (type instanceof PsiArrayType) {   //array type

            List<Object> list = new ArrayList<>();
            PsiType deepType = type.getDeepComponentType();
            list.add(typeResolve(deepType));

            return list;

        } else {    //reference Type
            /*
             * Object
             * Iterable
             * enum
             * */
            Map<String, Object> map = new LinkedHashMap<>();

            PsiType[] types = type.getSuperTypes();
            if (types.length == 0) {
                return map;
            } else {

                List<String> fieldTypeNames = new ArrayList<>();

                fieldTypeNames.add(type.getPresentableText());
                fieldTypeNames.addAll(Arrays.stream(types).map(PsiType::getPresentableText).collect(Collectors.toList()));

                if (fieldTypeNames.stream().anyMatch(s -> s.startsWith("Collection") || s.startsWith("Iterable"))) {

                    List<Object> list = new ArrayList<>();
                    PsiType iterableType = PsiUtil.extractIterableTypeParameter(type, false);
                    list.add(typeResolve(iterableType));

                    return list;
                } else if (fieldTypeNames.stream().anyMatch(s -> s.startsWith("Enum"))) {
                    return "";
                } else {
                    List<String> retain = new ArrayList<>(fieldTypeNames);
                    retain.retainAll(normalTypes.keySet());
                    if (!retain.isEmpty()) {
                        return normalTypes.get(retain.get(0));
                    } else {
                        PsiClass psiClass = PsiUtil.resolveClassInClassTypeOnly(type);
                        if (psiClass != null) {
                            for (PsiField field : psiClass.getAllFields()) {
                                map.put(field.getName(), typeResolve(field.getType()));
                            }
                        }
                        return map;
                    }
                }
            }
        }
    }
}


