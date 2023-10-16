package ink.organics.pojo2json.parser.model;

import com.google.common.base.CaseFormat;
import com.intellij.psi.PsiField;

import java.util.List;

public class POJOField extends POJOVariable {

    protected final PsiField psiField;

    protected POJOField(PsiField psiField) {
        super(psiField);
        this.psiField = psiField;
    }

    public static POJOField init(PsiField psiField,
                                 POJOClass pojoClass) {

        var pojo = new POJOField(psiField);
        pojo.recursionLevel = pojoClass.recursionLevel;
        pojo.ignoreProperties = pojoClass.ignoreProperties;
        pojo.psiClassGenerics = pojoClass.psiClassGenerics;
        return pojo;
    }

    public void setIgnoreProperties(List<String> ignoreProperties) {
        this.ignoreProperties = ignoreProperties;
    }

    public PsiField getPsiField() {
        return psiField;
    }

    public String getCamelCaseName() {
        return this.getName();
    }

    public String getSnakeCaseName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.psiField.getName());
    }

    public String getKebabCaseName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, this.psiField.getName());
    }

    public String getPascalCaseName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, this.psiField.getName());
    }

    public String getSnakeCaseUpperName() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this.psiField.getName());
    }
}
