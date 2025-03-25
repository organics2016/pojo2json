package ink.organics.pojo2json.parser.model;

import com.google.common.base.CaseFormat;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.List;

public class POJOField extends POJOVariable {

    protected final PsiField psiField;

    protected final PsiClass psiFieldClass;

    protected POJOField(PsiField psiField, PsiClass psiFieldClass) {
        super(psiField);
        this.psiField = psiField;
        this.psiFieldClass = psiFieldClass;
    }

    public static POJOField init(PsiField psiField, POJOClass pojoClass) {

        var pojo = new POJOField(psiField, pojoClass.psiClass);
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

    public PsiClass getPsiFieldClass() {
        return psiFieldClass;
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

    public String getLowerCaseName() {
        return this.getCamelCaseName().toLowerCase();
    }

    public String getLowerDotCaseName() {
        return this.getKebabCaseName().replace("-", ".");
    }
}
