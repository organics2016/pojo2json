package ink.organics.pojo2json.parser.model;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.List;
import java.util.Map;

public class POJOClass extends POJOObject {

    protected final PsiClass psiClass;

    protected POJOClass(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    public static POJOClass init(PsiClass psiClass,
                                 Map<String, String> psiTypeExpression) {
        var pojo = new POJOClass(psiClass);
        pojo.psiTypeExpression = psiTypeExpression;
        pojo.recursionLevel = 0;
        pojo.ignoreProperties = List.of();
        pojo.psiClassGenerics = Map.of();
        return pojo;
    }

    public static POJOClass init(POJOVariable pojoVariable) {
        var pojo = new POJOClass(pojoVariable.psiClass);
        pojo.psiTypeExpression = pojoVariable.psiTypeExpression;
        pojo.recursionLevel = pojoVariable.recursionLevel;
        pojo.ignoreProperties = pojoVariable.ignoreProperties;
        pojo.psiClassGenerics = pojoVariable.psiClassGenerics;
        return pojo;
    }

    public POJOField toField(PsiField psiField) {
        return POJOField.init(psiField, this);
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }
}
