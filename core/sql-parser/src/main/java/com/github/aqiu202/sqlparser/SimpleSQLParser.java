package com.github.aqiu202.sqlparser;

import com.github.aqiu202.util.StringUtils;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.update.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SimpleSQLParser implements SQLParser {

    private static final Logger log = LoggerFactory.getLogger(SimpleSQLParser.class);

    public static final SimpleSQLParser INSTANCE = new SimpleSQLParser();

    protected String prepareSQL(String sql) {
        if (StringUtils.isBlank(sql)) {
            throw new RuntimeException("SQL不能为空");
        }
        return sql.trim().replaceAll("\\s+", " ");
    }

    @Override
    public SQL parse(String sql) {
        String preparedSQL = this.prepareSQL(sql);
        // 使用JSqlParser解析SQL
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(preparedSQL);
        } catch (JSQLParserException e) {
            throw new RuntimeException("SQL解析异常", e);
        }
        List<SelectSQLFragment> selectFragments = new ArrayList<>();
        if (statement instanceof Select) {
            Select select = (Select) statement;
            if (select instanceof PlainSelect) {
                PlainSelect plainSelect = (PlainSelect) select;
                selectFragments.add(new SelectSQLFragment(plainSelect));
            } else if (select instanceof SetOperationList) {
                SetOperationList setOperationList = (SetOperationList) select;
                List<Select> selectBodies = setOperationList.getSelects();
                for (Select selectBodyItem : selectBodies) {
                    if (selectBodyItem instanceof PlainSelect) {
                        PlainSelect plainSelect = (PlainSelect) selectBodyItem;
                        selectFragments.add(new SelectSQLFragment(plainSelect));
                    }
                }
            }
            return new SelectSQL(select, selectFragments);
        } else if (statement instanceof Update) {
            return new UpdateSQL((Update) statement);
        } else if (statement instanceof Delete) {
            return new DeleteSQL((Delete) statement);
        } else if (statement instanceof Insert) {
            return new InserSQL((Insert) statement);
        }
        throw new UnsupportedOperationException("不支持的SQL类型");
    }

}
