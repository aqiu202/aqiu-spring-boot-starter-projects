package com.github.aqiu202.sqlparser.fragment;

import com.github.aqiu202.sqlparser.SelectSQLFragment;
import com.github.aqiu202.util.CollectionUtils;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;

import java.util.ArrayList;
import java.util.List;

public class WhereFragment implements ExpressionAppendable {

    private final Expression where;
    private final List<SelectSQLFragment> selects;

    public WhereFragment(Expression where) {
        this.where = where;
        this.selects = this.findSelectFragments(where);
    }

    public List<SelectSQLFragment> findSelectFragments(Expression expression) {
        List<SelectSQLFragment> ssf = new ArrayList<>();
        if (expression instanceof BinaryExpression) {
            BinaryExpression binaryExpression = (BinaryExpression) expression;
            ssf.addAll(this.findSelectFragments(binaryExpression.getLeftExpression()));
            ssf.addAll(this.findSelectFragments(binaryExpression.getRightExpression()));
        } else if (expression instanceof ExistsExpression) {
            ssf.addAll(this.findSelectFragments(((ExistsExpression) expression).getRightExpression()));
        } else if (expression instanceof InExpression) {
            ssf.addAll(this.findSelectFragments(((InExpression) expression).getLeftExpression()));
            ssf.addAll(this.findSelectFragments(((InExpression) expression).getRightExpression()));
        } else if (expression instanceof Between) {
            ssf.addAll(this.findSelectFragments(((Between) expression).getBetweenExpressionStart()));
            ssf.addAll(this.findSelectFragments(((Between) expression).getBetweenExpressionEnd()));
        } else if (expression instanceof NotExpression) {
            ssf.addAll(this.findSelectFragments(((NotExpression) expression).getExpression()));
        } else if (expression instanceof ParenthesedSelect) {
            ssf.add(new SelectSQLFragment(((ParenthesedSelect) expression).getPlainSelect()));
        }
        return ssf;
    }

    public List<SelectSQLFragment> getSelects() {
        return this.selects;
    }

    public Expression getWhere() {
        return where;
    }

    @Override
    public void appendExpression(Expression expression) {
        if (expression == null) {
            return;
        }
        List<SelectSQLFragment> sqls = this.getSelects();
        if (CollectionUtils.isNotEmpty(sqls)) {
            for (SelectSQLFragment sql : sqls) {
                sql.appendExpression(expression);
            }
        }
    }

}
