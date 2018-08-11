package com.xyh.spring.el;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;


/**
 * spring中的el表達式.
 * @author hcxyh  2018年8月11日
 *
 */
public class SpringElNote {
	private static ExpressionParser parser = new SpelExpressionParser();

    @Test
    public void testHelloWorld() {
        Expression expression = parser.parseExpression("'你好世界！'");
        String result = (String) expression.getValue();
        System.out.println(result);
    }
    
    @Test
    public void testStringOperation() {
        Expression expression = parser.parseExpression("'你好'.concat('世界!')");
        String result = (String) expression.getValue();
        System.out.println(result);
        expression = parser.parseExpression("'Hello world!'.toUpperCase()");
        result = expression.getValue(String.class);
        System.out.println(result);
        
        
        //在表达式上下文中，我们可以设置新变量。然后在表达式中使用#变量名访问变量。
        //Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
        Object tesla = new Object();
        StandardEvaluationContext context = new StandardEvaluationContext(tesla);
        context.setVariable("newName", "Mike Tesla");
        parser.parseExpression("Name = #newName").getValue(context);
        //System.out.println(tesla.getName()); // "Mike Tesla"
    }
}
