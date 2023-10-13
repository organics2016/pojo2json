package ink.organics.pojo2json.parser.model;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;

import java.util.List;
import java.util.Map;

public class POJOVariable extends POJOObject {

    protected final PsiVariable psiVariable;

    protected PsiType psiType;

    protected PsiClass psiClass;

    protected String name;


    protected POJOVariable(PsiVariable psiVariable) {
        this.psiVariable = psiVariable;
        this.psiType = psiVariable.getType();
        this.name = psiVariable.getName();
    }

    public static POJOVariable init(PsiVariable psiVariable,
                                    Map<String, String> psiTypeExpression,
                                    Map<String, PsiType> psiClassGenerics) {
        var pojo = new POJOVariable(psiVariable);
        pojo.psiTypeExpression = psiTypeExpression;
        pojo.recursionLevel = 0;
        pojo.ignoreProperties = List.of();
        pojo.psiClassGenerics = psiClassGenerics;
        return pojo;
    }


    public POJOVariable deepVariable(PsiType deepType, Map<String, PsiType> psiClassGenerics) {
        this.recursionLevel++;
        this.psiType = deepType;
        this.psiClassGenerics = psiClassGenerics;
        return this;
    }

    public POJOClass deepClass(PsiClass psiClass, Map<String, PsiType> psiClassGenerics) {
        this.recursionLevel++;
        this.psiClass = psiClass;
        this.psiClassGenerics = psiClassGenerics;
        return POJOClass.init(this);
    }

    public PsiVariable getPsiVariable() {
        return psiVariable;
    }

    public PsiType getPsiType() {
        return psiType;
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }

    public String getName() {
        return name;
    }
}
