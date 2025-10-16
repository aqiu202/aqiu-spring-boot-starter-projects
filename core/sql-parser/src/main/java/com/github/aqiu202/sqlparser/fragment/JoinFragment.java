package com.github.aqiu202.sqlparser.fragment;

import com.github.aqiu202.sqlparser.util.SQLUtils;
import com.github.aqiu202.util.CollectionUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.Join;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class JoinFragment extends FromFragment implements ExpressionAppendable {

    private final Join join;

    public JoinFragment(Join join) {
        super(join.getFromItem());
        this.join = join;
    }

    public Join getJoin() {
        return join;
    }

    @Override
    public void appendExpression(Expression expression) {
        if (expression == null) {
            return;
        }
        Collection<Expression> onExpressions = this.getJoin().getOnExpressions();
        List<Expression> newOnExpressions = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(onExpressions)) {
            for (Expression onExpression : onExpressions) {
                Expression andExpressions = SQLUtils.buildAndExpressions(Arrays.asList(onExpression, expression));
                newOnExpressions.add(andExpressions);
            }
            this.getJoin().setOnExpressions(newOnExpressions);
        }
    }

}
