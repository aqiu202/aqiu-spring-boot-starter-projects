package com.github.aqiu202.starters.jpa.util;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public final class JPAExpressionSingleton {

    private static class SingletonHolder {

        private static ExpressionParser PARSER = new SpelExpressionParser();
        private static ParserContext TEMPLATE = new TemplateParserContext(":#{", "}");
    }

    private JPAExpressionSingleton() {
    }

    public static ExpressionParser getParser() {
        return SingletonHolder.PARSER;
    }

    public static ParserContext getTemplate() {
        return SingletonHolder.TEMPLATE;
    }
}
