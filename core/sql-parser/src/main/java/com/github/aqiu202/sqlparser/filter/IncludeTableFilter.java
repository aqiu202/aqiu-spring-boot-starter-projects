package com.github.aqiu202.sqlparser.filter;

import com.github.aqiu202.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class IncludeTableFilter implements TableFilter {

    private final Set<String> whitelist = new HashSet<>();

    private final Map<String, List<String>> whitelistFields = new HashMap<>();

    public IncludeTableFilter(String... whitelist) {
        this(Arrays.asList(whitelist));
    }

    public IncludeTableFilter(Collection<String> whitelist) {
        whitelist.stream()
                .map(this::processName)
                .forEach(this.whitelist::add);
    }

    public IncludeTableFilter(Map<String, List<String>> whitelist) {
        whitelist.forEach((k, v) ->
        {
            String key = this.processName(k);
            this.whitelist.add(key);
            this.whitelistFields.put(key, v.stream().map(this::processName).collect(Collectors.toList()));
        });
    }

    public void addTable(String tableName) {
        whitelist.add(this.processName(tableName));
    }

    public void addTableFields(String tableName, Collection<String> fields) {
        this.addTable(tableName);
        this.whitelistFields.compute(this.processName(tableName), (key, value) -> {
            if (value == null) {
                value = new ArrayList<>();
            }
            value.addAll(fields.stream().map(this::processName).collect(Collectors.toList()));
            return value;
        });
    }


    @Override
    public boolean support(String tableName) {
        String processedTableName = this.processName(tableName);
        return whitelist.contains(processedTableName);
    }

    @Override
    public boolean support(String tableName, String tableFieldName) {
        String key = this.processName(tableName);
        List<String> fields = this.whitelistFields.get(key);
        // 未找到字段配置，则取表的配置
        if (CollectionUtils.isEmpty(fields)) {
            return this.support(tableName);
        }
        return fields.contains(this.processName(tableFieldName));
    }
}
