package com.github.aqiu202.sqlparser;

import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SetOperationList;

import java.util.ArrayList;
import java.util.List;

public class InserSQL extends AbstractSQL {

    public InserSQL(Insert statement) {
        super(statement, findFragments(statement));
    }

    static List<? extends SQLFragment> findFragments(Insert statement) {
        List<SQLFragment> fragments = new ArrayList<>();
        fragments.add(new InsertSQLFragment(statement));
        Select select = statement.getSelect();
        if (select != null) {
            if (select instanceof PlainSelect) {
                fragments.add(new SelectSQLFragment((PlainSelect) select));
            } else if (select instanceof SetOperationList) {
                SetOperationList setOperationList = (SetOperationList) select;
                List<Select> selectBodies = setOperationList.getSelects();
                for (Select selectBodyItem : selectBodies) {
                    if (selectBodyItem instanceof PlainSelect) {
                        fragments.add(new SelectSQLFragment((PlainSelect) selectBodyItem));
                    }
                }
            }
        }
        return fragments;
    }

}
