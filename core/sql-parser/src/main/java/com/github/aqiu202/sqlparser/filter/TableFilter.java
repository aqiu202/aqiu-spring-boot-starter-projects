package com.github.aqiu202.sqlparser.filter;

import java.util.Locale;

public interface TableFilter {

    TableFilter ALL = new TableFilter() {
        @Override
        public boolean support(String tableName) {
            return true;
        }

        @Override
        public boolean support(String tableName, String tableFieldName) {
            return true;
        }
    };

    TableFilter NONE = new TableFilter() {
        @Override
        public boolean support(String tableName) {
            return false;
        }

        @Override
        public boolean support(String tableName, String tableFieldName) {
            return false;
        }
    };

    boolean support(String tableName);

    boolean support(String tableName, String tableFieldName);

    default String processName(String tableName) {
        return tableName.toUpperCase(Locale.ROOT);
    }
}
