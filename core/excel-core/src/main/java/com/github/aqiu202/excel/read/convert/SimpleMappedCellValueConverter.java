package com.github.aqiu202.excel.read.convert;

import com.github.aqiu202.excel.format.FormatterFacade;
import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.format.FormatterProviderWrapper;
import com.github.aqiu202.excel.format.SimpleFormatterFacade;
import com.github.aqiu202.excel.format.wrap.FormatedValueWrapper;
import com.github.aqiu202.excel.format.wrap.ValueWrapper;
import com.github.aqiu202.excel.meta.DataMeta;
import com.github.aqiu202.excel.meta.ValueDescriptor;
import com.github.aqiu202.excel.read.cell.*;
import com.github.aqiu202.util.ClassUtils;
import org.apache.poi.ss.usermodel.Cell;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public class SimpleMappedCellValueConverter implements MappedCellValueConverter {

    private final FormatterFacade formatterFacade = new SimpleFormatterFacade();

    @Override
    public void convert(@Nonnull MappedCellValue mappedCellValue, FormatterProvider formatterProvider) {
        CellVal<?> cellVal = mappedCellValue.getCellValue();
        // 公式实际值为内部的计算值
        if (cellVal instanceof FormulaCellVal) {
            cellVal = ((FormulaCellVal) cellVal).getValue();
            mappedCellValue.setCellValue(cellVal);
        }
        Cell cell = cellVal.getCell();
        DataMeta dataMeta = mappedCellValue.getTableMeta();
        if (dataMeta instanceof FormatterProviderWrapper) {
            FormatterProvider fp = ((FormatterProviderWrapper) dataMeta).getProvider();
            if (fp != null) {
                formatterProvider = fp;
            }
        }
        CellVal<?> newCellVal = null;
        ValueDescriptor vd = dataMeta.getValueDescriptor();
        Class<?> valueType = vd.getValueType();
        if (valueType == null) {
            return;
        }
        // 实体中是字符类型，但是Excel中实际为其他类型
        if (String.class.isAssignableFrom(valueType) && !(cellVal instanceof StringCellVal)) {
            // 如果是（数字或者日期）
            if (cellVal instanceof NumberCellVal || cellVal instanceof DateCellVal) {
                ValueWrapper<?> valueWrapper = this.formatterFacade.format(cellVal.getValue(), formatterProvider);
                if (valueWrapper instanceof FormatedValueWrapper) {
                    newCellVal = new StringCellVal(cell, ((FormatedValueWrapper) valueWrapper).getValue());
                } else {
                    newCellVal = new StringCellVal(cell, String.valueOf(valueWrapper.getValue()));
                }
            } else {
                newCellVal = new StringCellVal(cell, String.valueOf(cellVal.getValue()));
            }
            // 如果实体中是数字类型，而Excel中不匹配
        } else if (ClassUtils.isNumber(valueType) && !(cellVal instanceof NumberCellVal)) {
            Object result = null;
            try {
                // 尝试进行解析
                result = this.formatterFacade.parse(String.valueOf(cellVal), valueType, formatterProvider);
            } catch (RuntimeException e) {
                // 对于格式异常，比如日期格式用数字类型接收，跳过设置默认值0
            }
            if (result == null) {
                newCellVal = new SimpleCellVal<>(cell, null);
            } else {
                newCellVal = new NumberCellVal(cell, new BigDecimal(String.valueOf(result)));
            }
        } else if (ClassUtils.isDate(valueType) && !(cellVal instanceof DateCellVal)) {
            Object parse = null;
            try {
                parse = this.formatterFacade.parse(cellVal.toString(), valueType, formatterProvider);
            } catch (RuntimeException e) {
                // 日期格式化异常时，忽略
            }
            newCellVal = DateCellVal.of(cell, parse);
        } else if ((valueType.equals(Boolean.class) || valueType.equals(Boolean.TYPE)) && !(cellVal instanceof BooleanCellVal)) {
            newCellVal = new BooleanCellVal(cell, Boolean.valueOf(cellVal.toString()));
        }
        if (newCellVal != null) {
            mappedCellValue.setCellValue(new ConvertedCellVal(cellVal, newCellVal));
        }
    }
}
