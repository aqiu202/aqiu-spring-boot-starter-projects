package com.github.aqiu202.sqlparser;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.statement.delete.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>DELETE片段</b>
 * <p>包含表别名和Select</p>
 */
public class DeleteSQLFragment extends AbstractSQLFragment<Delete> {

    public DeleteSQLFragment(Delete delete) {
        super(delete.getTable(), delete.getJoins(), delete.getWhere());
        this.delete = delete;
    }

    private static final Logger log = LoggerFactory.getLogger(DeleteSQLFragment.class);

    /**
     * 查询条件
     */
    private final Delete delete;

    @Override
    public Delete getStatement() {
        return delete;
    }

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
        Delete select = this.getStatement();
        Expression where = select.getWhere();
        if (where == null) {
            where = extExpression;
        } else {
            where = new AndExpression(where, extExpression);
        }
        select.setWhere(where);
    }

}
