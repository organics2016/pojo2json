package ink.organics.pojo2json.parser.model;

import com.intellij.psi.PsiField;

import java.util.List;

public class POJOField extends POJOVariable {

    protected PsiField psiField;


    public static POJOField init(PsiField psiField,
                                 POJOClass pojoClass) {

        var pojo = new POJOField();
        pojo.psiField = psiField;
        pojo.psiTypeExpression = pojoClass.psiTypeExpression;
        pojo.recursionLevel = pojoClass.recursionLevel;
        pojo.ignoreProperties = pojoClass.ignoreProperties;
        pojo.psiClassGenerics = pojoClass.psiClassGenerics;
        return pojo;
    }

    public POJOVariable toVariable() {
        this.psiVariable = this.psiField;
        this.psiType = this.psiField.getType();
        return this;
    }

    public void setIgnoreProperties(List<String> ignoreProperties) {
        this.ignoreProperties = ignoreProperties;
    }

    public PsiField getPsiField() {
        return psiField;
    }
}
