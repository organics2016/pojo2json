package ink.organics.pojo2json.test;


import com.google.common.base.CaseFormat;
import ink.organics.pojo2json.parser.el.EvaluationContextFactory;
import ink.organics.pojo2json.parser.el.TemporalTypeValue;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class TempTestCase {

    @Test
    public void test1() {
//        SpelParserConfiguration configuration = new SpelParserConfiguration(true, true);

        EvaluationContext evaluationContext = new StandardEvaluationContext("TTTTTT");
        evaluationContext.setVariable("xxhh", LocalDate.now());
        evaluationContext.setVariable("boolean", false);

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
        Expression expression = parser.parseExpression("#{#array.getValue}", template);
        System.out.println(expression.getValue(evaluationContext));
    }

    @Test
    public void test4() {
        System.out.println(Arrays.asList("fsdfsef\n\nsdfeeef\n\nsdfefesf\n\n".split("\n")));
    }

    @Test
    public void test5() {
        // #{#field.getName()}
        String sss = "classNameSpELMap";
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, sss));
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, sss));
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sss));
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, sss));
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, sss));
    }

    @Test
    public void test6() {
        TemporalTypeValue temporalTypeValue = new TemporalTypeValue();
        String f = LocalDateTime.ofInstant(Instant.ofEpochMilli((long) temporalTypeValue.getRandomValue()), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(f);
    }

    @Test
    public void test7() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String f = ZonedDateTime
                .ofInstant(Instant.now(), ZoneId.systemDefault())
                .format(formatter);
        System.out.println(f);
    }
}
