package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.meta.IndexedTableMeta;
import com.github.aqiu202.excel.meta.TableMeta;
import com.github.aqiu202.excel.read.cell.HeadMeta;

import java.util.List;

/**
 * 根据表头信息给元数据添加索引
 */
public interface IndexedMetaResolver {

    List<IndexedTableMeta> resolve(Class<?> type, List<? extends TableMeta> metas, HeadMeta[] headMetas);
}
