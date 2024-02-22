package com.github.aqiu202.excel.convert.cell;

import com.github.aqiu202.excel.format.FormatterFacade;
import com.github.aqiu202.excel.format.FormatterProvider;
import com.github.aqiu202.excel.format.FormatterProviderWrapper;
import com.github.aqiu202.excel.format.SimpleFormatterFacade;
import com.github.aqiu202.excel.format.wrap.FormatResultWrapper;
import com.github.aqiu202.excel.format.wrap.ResultWrapper;
import com.github.aqiu202.excel.meta.MappingMeta;
import com.github.aqiu202.excel.prop.MappedProperty;
import com.github.aqiu202.excel.read.cell.*;
import com.github.aqiu202.util.ClassUtils;
import com.github.aqiu202.util.ReflectionUtils;
import com.github.aqiu202.util.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;

public class SimpleMappedCellValueConverter implements MappedCellValueConverter {

    private final FormatterFacade formatterFacade = new SimpleFormatterFacade();

    @Override
    public void convert(MappedCellValue mappedCellValue, FormatterProvider formatterProvider) {
        CellValue<?> cellValue = mappedCellValue.getCellValue();
        Cell cell = cellValue.getCell();
        MappedProperty mappedProperty = mappedCellValue.getMappedProperty();
        if (mappedProperty.nonProperty()) {
            return;
        }
        Class<?> type = mappedProperty.getBeanProperty().getPropertyType();
        if (type == null) {
            return;
        }
        MappingMeta mappingMeta = mappedProperty.getMappingMeta();
        if (mappingMeta instanceof FormatterProviderWrapper) {
            FormatterProvider fp = ((FormatterProviderWrapper) mappingMeta).getFormatterProvider();
            if (fp != null) {
                formatterProvider = fp;
            }
        }
        CellValue<?> newCellValue = null;
        // 实体中是字符类型，但是Excel中实际为其他类型
        if (CharSequence.class.isAssignableFrom(type) && !(cellValue instanceof StringCellValue)) {
            // 如果是（数字或者日期）
            if (cellValue instanceof NumberCellValue || cellValue instanceof DateCellValue) {
                ResultWrapper<?> resultWrapper = this.formatterFacade.format(cellValue.getValue(), formatterProvider);
                if (resultWrapper instanceof FormatResultWrapper) {
                    newCellValue = new StringCellValue(cell, ((FormatResultWrapper) resultWrapper).getResult());
                } else {
                    newCellValue = new StringCellValue(cell, String.valueOf(resultWrapper.getResult()));
                }
            } else {
                newCellValue = new StringCellValue(cell, cellValue.getCell().getStringCellValue());
            }
            // 如果实体中是数字类型，而Excel中不匹配
        } else if (ClassUtils.isNumber(type) && !(cellValue instanceof NumberCellValue)) {
            Object result = null;
            try {
                // 尝试进行解析
                result = this.formatterFacade.parse(String.valueOf(cellValue), type, formatterProvider);
            } catch (NumberFormatException e) {
                // 对于格式异常，比如日期格式用数字类型接收，跳过设置默认值0
            }
            if (result == null) {
                newCellValue = new SimpleCellValue<>(cell, null);
            }else {
                newCellValue = new NumberCellValue(cell, new BigDecimal(String.valueOf(result)));
            }
        } else if (ClassUtils.isDate(type) && !(cellValue instanceof DateCellValue)) {
            Object parse = null;
            try {
                parse = this.formatterFacade.parse(cellValue.toString(), type, formatterProvider);
            } catch (RuntimeException e) {
                // 对于其他非法格式的字符类型，强制转换为日期时会报错，跳过，设置默认值null即可
                e.printStackTrace();
            }
            newCellValue = DateCellValue.of(cell, parse);
        } else if ((type.equals(Boolean.class) || type.equals(Boolean.TYPE)) && !(cellValue instanceof BooleanCellValue)) {
            newCellValue = new BooleanCellValue(cell, Boolean.valueOf(cellValue.toString()));
        }
        if (newCellValue != null) {
            mappedCellValue.setCellValue(new ConvertedCellValue(cellValue, newCellValue));
        }
    }
}
