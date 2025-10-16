package com.github.aqiu202.sqlparser.fragment;

import com.github.aqiu202.sqlparser.SelectSQLFragment;
import com.github.aqiu202.util.CollectionUtils;
import com.github.aqiu202.util.StringUtils;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FromFragment {

    private final FromItem fromItem;
    private final TableFragment tableFragment;
    private final SelectSQLFragments sqlFragments;

    public FromFragment(FromItem fromItem) {
        this.fromItem = fromItem;
        TableFragment tableFragment = null;
        SelectSQLFragments ssf = null;
        if (fromItem instanceof Table) {
            tableFragment = new TableFragment((Table) fromItem);
        } else if (fromItem instanceof ParenthesedSelect) {
            Select select = ((ParenthesedSelect) fromItem).getSelect();
            if (select instanceof PlainSelect) {
                ssf = new SelectSQLFragments(new SelectSQLFragment(((PlainSelect) select)));
            } else if (select instanceof SetOperationList) {
                List<Select> selects = ((SetOperationList) select).getSelects();
                List<SelectSQLFragment> selectFragments = new ArrayList<>();
                for (Select selectItem : selects) {
                    if (selectItem instanceof PlainSelect) {
                        selectFragments.add(new SelectSQLFragment((PlainSelect) selectItem));
                    }
                }
                if (CollectionUtils.isNotEmpty(selectFragments)) {
                    ssf = new SelectSQLFragments(selectFragments);
                }
            }
        }
        this.tableFragment = tableFragment;
        this.sqlFragments = ssf;
    }

    public FromItem getFromItem() {
        return fromItem;
    }

    public TableFragment getTableFragment() {
        return this.tableFragment;
    }

    public SelectSQLFragments getSqlFragments() {
        return this.sqlFragments;
    }

    public Set<String> findAllTableNames() {
        Set<String> tableNames = new HashSet<>();
        if (this.tableFragment != null) {
            String tableName = this.tableFragment.getTableName();
            if (StringUtils.isNotBlank(tableName)) {
                tableNames.add(tableName);
            }
        } else if (this.sqlFragments != null) {
            tableNames.addAll(this.sqlFragments.findAllTableNames());
        }
        return tableNames;
    }

}
