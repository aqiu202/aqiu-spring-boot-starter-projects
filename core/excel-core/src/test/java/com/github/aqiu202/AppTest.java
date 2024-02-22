package com.github.aqiu202;

import com.github.aqiu202.entity.BaseStation;
import com.github.aqiu202.excel.ExcelFactory;
import com.github.aqiu202.excel.SimpleExcelFactory;
import com.github.aqiu202.excel.mapping.IndexValueMapping;
import com.github.aqiu202.excel.read.ExcelReader;
import com.github.aqiu202.excel.write.ExcelWriter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {
    public static void main(String[] args) {
        ExcelFactory excelFactory = new SimpleExcelFactory();
        ExcelReader excelReader = excelFactory.forReader().build();
        List<BaseStation> readData = excelReader.index(BaseStation.class, new IndexValueMapping(
                        "code", "name", "", "createTime",
                        "updateTime", "workCenter.code", "workCenter.name", "dictStationType.dictName",
                        "dictStationProduceAttr.dictName", "dictStationSpecialAttr.dictName",
                        "createUser.userName", "updateUser.userName"
                ))
                .read(new File("D:/station.xlsx"));
//        List<HashMap> readData = excelReader.map(HashMap.class)
//                .read(new File("D:/station.xlsx"));
        ExcelWriter excelWriter = excelFactory.forWriter().build();
        excelWriter.annotation(BaseStation.class).writeData(readData)
                .append(BaseStation.class).write("台位信息", readData)
                .writeTo("D:/station-copy.xlsx");
//        long t3 = System.currentTimeMillis();
        System.out.println(readData);
    }
}
