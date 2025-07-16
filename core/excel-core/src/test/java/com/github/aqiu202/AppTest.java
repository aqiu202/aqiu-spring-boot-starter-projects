package com.github.aqiu202;

import com.github.aqiu202.entity.BaseStation;
import com.github.aqiu202.excel.ExcelFactory;
import com.github.aqiu202.excel.SimpleExcelFactory;
import com.github.aqiu202.excel.convert.SimpleConverterFactory;
import com.github.aqiu202.excel.convert.SimpleMapConverter;
import com.github.aqiu202.excel.meta.MapPropertyMeta;
import com.github.aqiu202.excel.read.ExcelReader;
import com.github.aqiu202.excel.write.ExcelWriter;
import com.github.aqiu202.excel.write.extract.PropertyDataExtractor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Unit test for simple App.
 */
public class AppTest {


    private Map<Short, String> getValidMap() {
        return new HashMap<Short, String>(){{
            put((short) 1, "正常");
            put((short) 2, "故障");
            put(null, "未知");
        }};
    }

    @Test
    public void testExcelExport() {
        SimpleConverterFactory converterFactory = new SimpleConverterFactory();
        converterFactory.addConverter("convertValid", new SimpleMapConverter<>(this.getValidMap()));
        ExcelFactory excelFactory = ExcelFactory.builder()
            .converterFactory(converterFactory).build();
        String url = "https://img0.baidu.com/it/u=759006001,1162006204&fm=253&fmt=auto&app=138&f=JPEG?w=888&h=500";
        String url2 = "https://img1.baidu.com/it/u=307475733,3112699775&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=281";
        List<BaseStation> baseStations = new ArrayList<>();
        BaseStation bs1 = new BaseStation();
        bs1.setCode("TW_001");
        bs1.setName("台位001");
        bs1.setTotal(new BigDecimal(2));
        bs1.setCoefficient(new BigDecimal("10.5"));
        bs1.setUrl(url);
        bs1.setCreateTime(LocalDateTime.now());
        BaseStation bs2 = new BaseStation();
        bs2.setCode("TW_002");
        bs2.setName("台位002");
        bs2.setTotal(new BigDecimal(5));
        bs2.setCoefficient(new BigDecimal("8.5"));
        bs2.setUrl(url2);
        bs2.setCreateTime(LocalDateTime.now());
        bs2.setValid(((short) 2));
        baseStations.add(bs1);
        baseStations.add(bs2);
        SimpleConverterFactory cf = new SimpleConverterFactory();
        ExcelWriter excelWriter = excelFactory.buildWriter()
                .configuration(config -> {
                    config.setDefaultDateFormat("yyyy-MM-dd HH:mm:ss");
                })
                .converterFactory(cf)
                .build();
        excelWriter
                .annotation(BaseStation.class)
                .sheetName("台位信息")
                .then()
                .write(baseStations)
                .write(Collections.singleton(bs1))
                .next()
                .type(BaseStation.class)
                .protectSheet("123456")
                .sheetName("简单信息")
                .then()
                .write(baseStations)
                .exportTo("/Users/xuqiu/tmp/test1.xlsx");
        List<MapPropertyMeta> metas = Arrays.asList(
                new MapPropertyMeta("field1", "模板说明：\n" +
                        "1、模版keyType字段仅支持web（前端）与backend（后端）类型，字段不能为空\n" +
                        "2、如需添加web类型文案，languageKey格式为intl_{8}-{4}-{4}-{4}-{12}（intl_UUID32位）\n" +
                        "3、如需添加backend类型文案，languageKey格式为intl_backend_{8}-{4}-{4}-{4}-{12}（intl_backend_UUID32位）\n" +
                        "4、如需添加公共类型文案，模版keyType字段仅支持web（前端）\n" +
                        "5、模板中内容请勿修改，以防止导入出现问题", "field1"),
                new MapPropertyMeta("","English"),
                new MapPropertyMeta("","简体中文"),
                new MapPropertyMeta("","繁体中文"),
                new MapPropertyMeta("","日本语")
        );
        excelWriter.custom(new PropertyDataExtractor(metas), Map.class)
                        .then().write(new ArrayList<>())
                        .exportTo("/Users/xuqiu/tmp/export.xlsx");
        System.out.println(baseStations);
    }

    @Test
    public void testReader() {
        SimpleConverterFactory converterFactory = new SimpleConverterFactory();
        converterFactory.addConverter("convertValid", new SimpleMapConverter<>(this.getValidMap()));
        ExcelFactory excelFactory = ExcelFactory.builder()
                .converterFactory(converterFactory).build();
        ExcelReader excelReader = excelFactory.buildReader()
                .configuration(configuration -> {
                    configuration.setReadFormula(true);
                    configuration.setReadEmptyText(false);
                })
                .build();
        List<BaseStation> readData = excelReader.annotation(BaseStation.class)
                .read("C:/tmp/test.xlsx", 0, 2);
        System.out.println(readData);
    }
}
