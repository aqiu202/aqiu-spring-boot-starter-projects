package com.github.aqiu202.excel.meta;

/**
 * 表格的元数据（表头和表内容的描述）
 */
public interface DataMeta {

    /**
     * 表格字段的key，配合{@link #getFormula()}描述公式
     */
    String getKey();

    /**
     * 获取表达式的字符串表示，需要配合{@link #getKey()}使用
     */
    default String getFormula() {
        return null;
    }

    /**
     * 是否是图片
     */
    default boolean isImage() {
        return false;
    }

    /**
     * 数据的读写描述
     */
    ValueDescriptor getValueDescriptor();

    /**
     * 表头描述
     */
    HeadDescriptor getHeadDescriptor();

    /**
     * 顺序
     */
    default int getOrder() {
        return 0;
    }

}
