package com.github.aqiu202.sqlparser.filter;

import com.github.aqiu202.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ExcludeTableFilter implements TableFilter {

    private final Set<String> blacklist = new HashSet<>();

    private final Map<String, List<String>> blacklistFields = new HashMap<>();

    public ExcludeTableFilter(String... blacklist) {
        this(Arrays.asList(blacklist));
    }

    public ExcludeTableFilter(Collection<String> blacklist) {
        blacklist.stream()
                .map(this::processName)
                .forEach(this.blacklist::add);
    }

    public ExcludeTableFilter(Map<String, List<String>> whitelist) {
        whitelist.forEach((k, v) ->
        {
            String key = this.processName(k);
            this.blacklist.add(key);
            this.blacklistFields.put(key, v.stream().map(this::processName).collect(Collectors.toList()));
        });
    }

    public void addTable(String tableName) {
        blacklist.add(this.processName(tableName));
    }

    public void addTableFields(String tableName, Collection<String> fields) {
        this.addTable(tableName);
        this.blacklistFields.compute(this.processName(tableName), (key, value) -> {
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
        return !blacklist.contains(processedTableName);
    }

    @Override
    public boolean support(String tableName, String tableFieldName) {
        String key = this.processName(tableName);
        List<String> fields = this.blacklistFields.get(key);
        // 未找到字段配置，则取表的配置
        if (CollectionUtils.isEmpty(fields)) {
            return this.support(tableName);
        }
        return !fields.contains(this.processName(tableFieldName));
    }

}
