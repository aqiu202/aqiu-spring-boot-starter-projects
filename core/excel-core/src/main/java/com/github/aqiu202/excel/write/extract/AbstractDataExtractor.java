package com.github.aqiu202.excel.write.extract;

import com.github.aqiu202.excel.format.FormatterFacade;
import com.github.aqiu202.excel.format.SimpleFormatterFacade;
import com.github.aqiu202.excel.meta.TableMeta;
import com.github.aqiu202.excel.write.Heads;
import com.github.aqiu202.excel.write.SimpleTableHeadsAnalyser;
import com.github.aqiu202.excel.write.TableHeadsAnalyser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>抽象的数据提取器</b>
 *
 * @author xuqiu 2023/3/22 14:08
 **/
public abstract class AbstractDataExtractor<T extends TableMeta> implements DataExtractor<T> {

    private final Map<Class<?>, List<T>> metaMap = new HashMap<>();
    private TableHeadsAnalyser tableHeadsAnalyser = new SimpleTableHeadsAnalyser();
    protected FormatterFacade formatterFacade = new SimpleFormatterFacade();

    @Override
    public List<T> extractMetas(Class<?> dataType) {
        return this.metaMap.compute(dataType, (k, v) -> {
            if (v == null) {
                v = this.getMetaAnalyzer().analyseMetas(k);
            }
            return v;
        });
    }

    @Override
    public Heads extractHeadsFromMetas(List<T> metas) {
        return this.getTableHeadsAnalyser().analyse(metas);
    }

    @Override
    public FormatterFacade getFormatterFacade() {
        return formatterFacade;
    }

    public AbstractDataExtractor<T> setFormatterFacade(FormatterFacade formatterFacade) {
        this.formatterFacade = formatterFacade;
        return this;
    }

    public TableHeadsAnalyser getTableHeadsAnalyser() {
        return this.tableHeadsAnalyser;
    }

    public void setTableHeadsAnalyser(TableHeadsAnalyser tableHeadsAnalyser) {
        this.tableHeadsAnalyser = tableHeadsAnalyser;
    }
}
