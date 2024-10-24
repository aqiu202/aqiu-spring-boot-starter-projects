package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.meta.IndexedMeta;
import com.github.aqiu202.excel.meta.MapPropertyMeta;
import com.github.aqiu202.excel.meta.DataMeta;
import com.github.aqiu202.excel.read.cell.HeadMeta;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleIndexedMetaResolver implements IndexedMetaResolver {
    @Override
    public List<IndexedMeta> resolve(Class<?> type, List<? extends DataMeta> metas, HeadMeta[] headMetas) {
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

    private IndexedMeta buildMapIndexedTableMeta(HeadMeta headMeta) {
        return new IndexedMeta(headMeta.getColIndex(), new MapPropertyMeta(headMeta.asText()));
    }

    private IndexedMeta resolveIndexedTableMeta(DataMeta meta, HeadMeta[] headMetas) {
        HeadMeta headMeta = Stream.of(headMetas)
                .filter(hm -> hm.isMatched(meta))
                .findFirst()
                .orElse(null);
        return headMeta == null ? null : new IndexedMeta(headMeta.getColIndex(), meta);
    }
}
