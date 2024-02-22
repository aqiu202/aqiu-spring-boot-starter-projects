package com.github.aqiu202.excel.write;

public interface TypedExcelWriter extends CombineExcelWriter<TypedExcelWriter> {

    <S> ItemExcelWriter<S> append(Class<S> type);

}
