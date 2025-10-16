package com.github.aqiu202.sqlparser;

import com.github.aqiu202.sqlparser.expression.AppendableExpressionFactory;
import com.github.aqiu202.sqlparser.expression.AppendableExpressionItem;
import com.github.aqiu202.sqlparser.fragment.FromFragment;
import com.github.aqiu202.sqlparser.fragment.JoinFragment;
import com.github.aqiu202.sqlparser.fragment.SelectSQLFragments;
import com.github.aqiu202.sqlparser.fragment.TableFragment;
import com.github.aqiu202.sqlparser.fragment.WhereFragment;
import com.github.aqiu202.util.CollectionUtils;
import com.github.aqiu202.util.StringUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;

import java.util.*;

public abstract class AbstractSQLFragment<T extends Statement> implements SQLFragment {

    private final FromFragment fromFragment;

    private final List<JoinFragment> joinFragments;

    private final WhereFragment whereFragment;

    private Map<String, String> tableAliasMap;

    public AbstractSQLFragment(FromItem fromItem, List<Join> joins, Expression where) {
        this.fromFragment = new FromFragment(fromItem);
        List<JoinFragment> joinFragments = new ArrayList<>();
        if (joins != null) {
            for (Join join : joins) {
                joinFragments.add(new JoinFragment(join));
            }
        }
        this.joinFragments = joinFragments;
        this.whereFragment = new WhereFragment(where);
    }

    @Override
    public FromFragment getFromFragment() {
        return fromFragment;
    }

    @Override
    public List<JoinFragment> getJoinFragments() {
        return joinFragments;
    }

    @Override
    public WhereFragment getWhereFragment() {
        return whereFragment;
    }

    /**
     * 表名称的统一处理（变为大写）
     *
     * @param tableName 表名称
     * @return 处理后的表名称
     */
    protected String processTableName(String tableName) {
        if (StringUtils.isNotBlank(tableName)) {
            return tableName.toUpperCase();
        }
        return tableName;
    }

    @Override
    public Map<String, String> getTableAliasMap() {
        if (this.tableAliasMap == null) {
            this.tableAliasMap = new HashMap<>();
            FromFragment ff = this.getFromFragment();
            TableFragment tf = ff.getTableFragment();
            if (tf != null) {
                this.tableAliasMap.put(this.processTableName(tf.getTableName()), tf.getTableAlias());
            }
            List<JoinFragment> jfs = this.getJoinFragments();
            if (CollectionUtils.isNotEmpty(jfs)) {
                for (JoinFragment jf : jfs) {
                    TableFragment tf1 = jf.getTableFragment();
                    if (tf1 != null) {
                        this.tableAliasMap.put(this.processTableName(tf1.getTableName()), tf1.getTableAlias());
                    }
                }
            }
        }
        return this.tableAliasMap;
    }

    @Override
    public Set<String> findAllTableNames() {
        Set<String> results = new HashSet<>(this.getTableAliasMap().keySet());
        FromFragment ff = this.getFromFragment();
        SelectSQLFragments fss = ff.getSqlFragments();
        if (fss != null) {
            results.addAll(fss.findAllTableNames());
        }
        List<JoinFragment> jfs = this.getJoinFragments();
        if (CollectionUtils.isNotEmpty(jfs)) {
            for (JoinFragment jf : jfs) {
                results.addAll(jf.findAllTableNames());
            }
        }
        WhereFragment wf = this.getWhereFragment();
        if (wf != null) {
            List<SelectSQLFragment> sss = wf.getSelects();
            if (CollectionUtils.isNotEmpty(sss)) {
                for (SelectSQLFragment ss : sss) {
                    results.addAll(ss.findAllTableNames());
                }
            }
        }
        return results;
    }


    protected abstract T getStatement();

    @Override
    public String asText() {
        return this.getStatement().toString();
    }

    @Override
    public void accept(AppendableExpressionFactory<?> factory) {
        FromFragment ff = this.getFromFragment();
        TableFragment tf = ff.getTableFragment();
        if (tf != null) {
            List<AppendableExpressionItem> expressionItems = factory.createExpressionItems(tf);
            if (CollectionUtils.isNotEmpty(expressionItems)) {
                for (AppendableExpressionItem expressionItem : expressionItems) {
                    this.appendExpression(this.createExpression(expressionItem.toExpression()));
                }
            }
        }
        SelectSQLFragments fsql = ff.getSqlFragments();
        if (fsql != null) {
            fsql.accept(factory);
        }
        List<JoinFragment> jfs = this.getJoinFragments();
        if (CollectionUtils.isNotEmpty(jfs)) {
            for (JoinFragment jf : jfs) {
                TableFragment jtf = jf.getTableFragment();
                if (jtf != null) {
                    List<AppendableExpressionItem> expressionItems = factory.createExpressionItems(jtf);
                    if (CollectionUtils.isNotEmpty(expressionItems)) {
                        for (AppendableExpressionItem expressionItem : expressionItems) {
                            jf.appendExpression(this.createExpression(expressionItem.toExpression()));
                        }
                    }
                }
                SelectSQLFragments sql = jf.getSqlFragments();
                if (sql != null) {
                    sql.accept(factory);
                }
            }
        }
        WhereFragment wf = this.getWhereFragment();
        if (wf != null) {
            List<SelectSQLFragment> sss = wf.getSelects();
            if (CollectionUtils.isNotEmpty(sss)) {
                for (SelectSQLFragment ss : sss) {
                    ss.accept(factory);
                }
            }
        }
    }
}
