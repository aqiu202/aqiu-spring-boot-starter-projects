package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.meta.IndexedTableMeta;
import com.github.aqiu202.excel.meta.PropertyTableMeta;
import com.github.aqiu202.excel.meta.TableMeta;
import com.github.aqiu202.excel.read.cell.HeadMeta;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleIndexedMetaResolver implements IndexedMetaResolver {
    @Override
    public List<IndexedTableMeta> resolve(Class<?> type, List<? extends TableMeta> metas, HeadMeta[] headMetas) {
        if (Map.class.isAssignableFrom(type)) {
            return Stream.of(headMetas)
                    .map(this::buildMapIndexedTableMeta)
                    .collect(Collectors.toList());
        }
        return metas.stream()
                .map(meta -> this.resolveIndexedTableMeta(meta, headMetas))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private IndexedTableMeta buildMapIndexedTableMeta(HeadMeta headMeta) {
        return new IndexedTableMeta(headMeta.getColIndex(), new PropertyTableMeta(headMeta.asText()));
    }

    private IndexedTableMeta resolveIndexedTableMeta(TableMeta meta, HeadMeta[] headMetas) {
        HeadMeta headMeta = Stream.of(headMetas)
                .filter(hm -> hm.isMatched(meta))
                .findFirst()
                .orElse(null);
        return headMeta == null ? null : new IndexedTableMeta(headMeta.getColIndex(), meta);
    }
}
