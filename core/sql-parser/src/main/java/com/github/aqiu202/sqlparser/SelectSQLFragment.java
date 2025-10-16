package com.github.aqiu202.sqlparser;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>查询片段</b>
 * <p>包含表别名和Select</p>
 */
public class SelectSQLFragment extends AbstractSQLFragment<PlainSelect> {

    public SelectSQLFragment(PlainSelect select) {
        super(select.getFromItem(), select.getJoins(), select.getWhere());
        this.select = select;
    }

    private static final Logger log = LoggerFactory.getLogger(SelectSQLFragment.class);

    /**
     * 查询条件
     */
    private final PlainSelect select;

    /**
     * 拼接查询条件
     *
     * @param extExpression 附加查询条件
     */
    @Override
    public void appendExpression(Expression extExpression) {
        if (extExpression == null) {
            return;
        }
        PlainSelect select = this.getStatement();
        Expression where = select.getWhere();
        if (where == null) {
            where = extExpression;
        } else {
            where = new AndExpression(where, extExpression);
        }
        select.setWhere(where);
    }

    @Override
    protected PlainSelect getStatement() {
        return this.select;
    }

}
