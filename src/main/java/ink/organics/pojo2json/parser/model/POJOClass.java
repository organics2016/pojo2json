package ink.organics.pojo2json.parser.model;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.List;
import java.util.Map;

public class POJOClass extends POJOObject {

    protected PsiClass psiClass;

    public static POJOClass init(PsiClass psiClass,
                                 Map<String, String> psiTypeExpression) {
        var pojo = new POJOClass();
        pojo.psiClass = psiClass;
        pojo.psiTypeExpression = psiTypeExpression;
        pojo.recursionLevel = 0;
        pojo.ignoreProperties = List.of();
        pojo.psiClassGenerics = Map.of();
        return pojo;
    }

    public POJOField toField(PsiField psiField) {
        return POJOField.init(psiField, this);
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }
}
