package com.github.aqiu202.excel.extract;

import com.github.aqiu202.excel.format.FormatterFacade;
import com.github.aqiu202.excel.format.SimpleFormatterFacade;
import com.github.aqiu202.excel.meta.MappingMeta;

/**
 * <b>抽象的数据提取器</b>
 *
 * @author xuqiu 2023/3/22 14:08
 **/
public abstract class AbstractDataExtractor<T extends MappingMeta> implements DataExtractor<T> {

    protected FormatterFacade formatterFacade = new SimpleFormatterFacade();

    @Override
    public FormatterFacade getFormatterFacade() {
        return formatterFacade;
    }

    public AbstractDataExtractor<T> setFormatterFacade(FormatterFacade formatterFacade) {
        this.formatterFacade = formatterFacade;
        return this;
    }

}
