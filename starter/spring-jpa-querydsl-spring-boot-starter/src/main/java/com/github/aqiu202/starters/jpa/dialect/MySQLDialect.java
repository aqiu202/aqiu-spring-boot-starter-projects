package com.github.aqiu202.starters.jpa.dialect;

import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class MySQLDialect extends MySQL57Dialect {

    public MySQLDialect() {
        super();
        this.registerFunction("group_concat",
                new SQLFunctionTemplate(StandardBasicTypes.STRING, "GROUP_CONCAT(?1)"));
    }
}
