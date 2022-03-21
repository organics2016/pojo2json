package ink.organics.pojo2json.generator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiEnumConstant;
import com.intellij.psi.PsiField;
import com.intellij.psi.util.PsiUtil;

public class EnumGenerator extends JSONValueGenerator {


    private final PsiField field;

    public EnumGenerator(PsiField field) {
        this.field = field;
    }


    @Override
    public Object generator() {

        PsiClass psiClass = PsiUtil.resolveClassInClassTypeOnly(field.getType());
        if (psiClass.isEnum()) { // enum

            for (PsiField field : psiClass.getFields()) {
                if (field instanceof PsiEnumConstant) {
                    return field.getName();
                }
            }
            return "";

        }

        return null;
    }
}
