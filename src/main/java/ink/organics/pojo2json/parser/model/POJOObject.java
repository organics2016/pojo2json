package ink.organics.pojo2json.parser.model;

import com.intellij.psi.PsiType;
import ink.organics.pojo2json.parser.el.EvaluationContextFactory;

import java.util.List;
import java.util.Map;

public abstract class POJOObject {

    /**
     * PSI类型表达式
     * eg: {"java.util.UUID":"#{#uuid.getValue()}"}
     * init: {@link EvaluationContextFactory#initExpressionMap()}
     */
    protected Map<String, String> psiTypeExpression;

    /**
     * 递归深度
     * ref: {@link ink.organics.pojo2json.parser.POJO2JSONParser#parseFieldValue(POJOVariable)}
     */
    protected int recursionLevel;

    /**
     * 需要过滤的属性，随Fields上定义的注释而变化
     * ref: {@link ink.organics.pojo2json.parser.POJO2JSONParser#parseField}
     */
    protected List<String> ignoreProperties;

    /**
     * 当前PsiType的Class所拥有的泛型Map，Map中包含当前PsiClass所定义的 泛型 和 泛型对应的用户指定类型 (E=CustomObject)
     * 并在解析当前PsiClass所包含的Field时，尝试获取这个Field所定义的泛型Map，然后传入下一层
     */
    protected Map<String, PsiType> psiClassGenerics;

    public Map<String, String> getPsiTypeExpression() {
        return psiTypeExpression;
    }

    public int getRecursionLevel() {
        return recursionLevel;
    }

    public List<String> getIgnoreProperties() {
        return ignoreProperties;
    }

    public Map<String, PsiType> getPsiClassGenerics() {
        return psiClassGenerics;
    }
}
