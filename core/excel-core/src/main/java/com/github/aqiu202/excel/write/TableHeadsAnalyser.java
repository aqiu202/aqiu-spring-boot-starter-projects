package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.meta.TableMeta;

import java.util.List;

/**
 * 解析表元数据为表格的表头（用于渲染表格）
 */
public interface TableHeadsAnalyser {

    Heads analyse(List<? extends TableMeta> metas);

}
