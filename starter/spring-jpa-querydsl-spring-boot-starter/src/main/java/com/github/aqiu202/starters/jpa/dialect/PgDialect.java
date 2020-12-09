package com.github.aqiu202.starters.jpa.dialect;

import java.sql.Types;
import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

public class PgDialect extends PostgreSQL94Dialect {

    public PgDialect() {
        super();
        this.registerHibernateType(Types.OTHER, "string");
        this.registerFunction("array_agg",
                new SQLFunctionTemplate(StandardBasicTypes.CHARACTER_ARRAY, "ARRAY_AGG(?1)"));
        this.registerFunction("array_to_string",
                new SQLFunctionTemplate(StandardBasicTypes.STRING, "ARRAY_TO_STRING(?1, ?2)"));
        this.registerFunction("string_agg",
                new SQLFunctionTemplate(StandardBasicTypes.STRING, "STRING_AGG(?1, ?2)"));
    }

    @Override
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
        switch (sqlTypeDescriptor.getSqlType()) {
            case Types.CLOB:
            case Types.BLOB:
            case Types.OTHER://其他类型默认是pgsql的json
                return VarcharTypeDescriptor.INSTANCE;
        }
        return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
    }

}
