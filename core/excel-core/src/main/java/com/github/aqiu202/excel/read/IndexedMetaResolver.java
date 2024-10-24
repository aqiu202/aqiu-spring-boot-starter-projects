package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.meta.IndexedMeta;
import com.github.aqiu202.excel.meta.DataMeta;
import com.github.aqiu202.excel.read.cell.HeadMeta;

import java.util.List;

/**
 * 根据表头信息给元数据添加索引
 */
public interface IndexedMetaResolver {

    List<IndexedMeta> resolve(Class<?> type, List<? extends DataMeta> metas, HeadMeta[] headMetas);
}
