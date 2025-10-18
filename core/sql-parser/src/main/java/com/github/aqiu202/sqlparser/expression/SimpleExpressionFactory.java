package com.github.aqiu202.sqlparser.expression;

import com.github.aqiu202.sqlparser.filter.TableFilter;
import com.github.aqiu202.sqlparser.fragment.TableFragment;
import com.github.aqiu202.util.CollectionUtils;
import com.github.aqiu202.util.StringUtils;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SimpleExpressionFactory implements AppendableExpressionFactory<SimpleExpressions> {

    private final SimpleExpressions expressions;
    private final TableFilter tableFilter;

    public SimpleExpressionFactory(List<AppendableExpressionItem> items, TableFilter tableFilter) {
        this.expressions = new SimpleExpressions(items);
        this.tableFilter = tableFilter;
    }

    @Override
    public AppendableExpressionMetaStore<SimpleExpressions> getMetaStore() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SimpleExpressions findMetaByTableName(String tableName) {
        return this.expressions;
    }

    public SimpleExpressions getExpressions() {
        return this.expressions;
    }

    public boolean hasExpressions() {
        return CollectionUtils.isNotEmpty(this.getExpressions().getItems());
    }

    @Override
    public List<AppendableExpressionItem> buildExpressionItems(TableFragment fragment, SimpleExpressions meta) {
        return meta.getItems()
                .stream()
                .filter(item -> {
                    String tableName = this.processName(fragment.getTableName());
                    String fieldName = this.processName(item.getExpression().getTableFieldName());
                    return this.tableFilter.support(tableName, fieldName);
                })
                .map(item -> this.buildAppendableExpression(fragment, item)
                ).collect(Collectors.toList());
    }

    protected AppendableExpressionItem buildAppendableExpression(TableFragment fragment, AppendableExpressionItem item) {
        ExpressionItem expression = item.getExpression();
        String tableAlias = fragment.getTableAlias();
        String tableFieldName = expression.getTableFieldName();
        String left = StringUtils.isNotBlank(tableAlias) ? tableAlias + "." + tableFieldName : tableFieldName;
        return new SimpleAppendableExpressionItem(this.rebuildExpression(new EqualsExpressionItem(left, expression.getVariableName())), item.getExpressionValueProvider());
    }

    protected ExpressionItem rebuildExpression(ExpressionItem item) {
        return new StringEqualsExpressionItem(item.getTableFieldName());
    }

    protected String processName(String name) {
        return name.toUpperCase(Locale.ROOT);
    }
}
