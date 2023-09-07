package ink.organics.pojo2json.test;


import ink.organics.pojo2json.parser.el.EvaluationContextFactory;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.time.LocalDate;

public class SpELTestCase {

    @Test
    public void test1() {
//        SpelParserConfiguration configuration = new SpelParserConfiguration(true, true);

        EvaluationContext evaluationContext = new StandardEvaluationContext("TTTTTT");
        evaluationContext.setVariable("xxhh", LocalDate.now());
        evaluationContext.setVariable("boolean",false);

        ExpressionParser parser = new SpelExpressionParser();
        ParserContext template = new TemplateParserContext();
//        Expression expression = parser.parseExpression("ABC#{('Hello' + ' World').concat('!')}#{#root.toLowerCase()+#xxhh}", template);
        Expression expression = parser.parseExpression("#{#boolean}", template);

        System.out.println(expression.getValue(evaluationContext).getClass().getTypeName());
    }


    @Test
    public void test3() {
        EvaluationContext evaluationContext = EvaluationContextFactory.newEvaluationContext(null);

        ExpressionParser parser = new SpelExpressionParser();
        ParserContext template = new TemplateParserContext();
        Expression expression = parser.parseExpression("#{#arr.getValue}", template);
        System.out.println(expression.getValue(evaluationContext));
    }
}
