package ink.organics.pojo2json.parser.el;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiEnumConstant;
import com.intellij.psi.PsiField;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumType implements RandomTypeValue {

    private PsiClass psiClass;

    public EnumType() {
    }

    public EnumType(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    @Override
    public Object getRandomValue() {
        List<String> psiFieldNames = Arrays.stream(psiClass.getAllFields())
                .filter(psiField -> psiField instanceof PsiEnumConstant)
                .map(PsiField::getName)
                .collect(Collectors.toList());

        if (psiFieldNames.isEmpty()) {
            return "";
        }

        return psiFieldNames.get(random.nextInt(0, psiFieldNames.size()));
    }

    @Override
    public Object getValue() {
        return Arrays.stream(psiClass.getAllFields())
                .filter(psiField -> psiField instanceof PsiEnumConstant)
                .findFirst()
                .map(PsiField::getName)
                .orElse("");
    }
}
