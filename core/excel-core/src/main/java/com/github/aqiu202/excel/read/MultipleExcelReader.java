package com.github.aqiu202.excel.read;


import com.github.aqiu202.excel.mapping.AnnotationValueMapping;
import com.github.aqiu202.excel.model.SheetReadConfiguration;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description: 可以读取多个sheet
 *
 * @author haoyong
 * @createDate: 2023/12/8 16:13
 * @lastModifyBy haoyong
 */
public class MultipleExcelReader {




    private Workbook workbook;

    private SheetReadConfiguration sheetReadConfiguration = new SheetReadConfiguration();

    public MultipleExcelReader(File file) {
        workbook = this.getWorkbookFromFile(file);
    }

    public MultipleExcelReader(InputStream inputStream) {

        workbook = this.getWorkBook(inputStream);

    }

//    sheetReadConfiguration.setPropertyAccessor(PropertyAccessor.METHOD);
    public void setSheetReadConfiguration(SheetReadConfiguration configuration){
        this.sheetReadConfiguration = configuration;
    }

    public SheetReadConfiguration getSheetReadConfiguration() {
        return sheetReadConfiguration;
    }

    private Workbook getWorkbookFromFile(File file){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            return getWorkBook(fileInputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("未找到文件，请检查上传的excel", e);
        }

    }

    private Workbook getWorkBook(InputStream inputStream) {
        try {
            return WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("创建excel workbook失败，请检查上传的excel", e);
        }

    }

    public <T> List<T> annotationRead(String sheetName, Class<T> clazz) {
        SimpleCommonExcelReader simpleExcelReader = new SimpleCommonExcelReader<T>(clazz);
        int sheetIndex = this.workbook.getSheetIndex(sheetName);
        if (sheetIndex<0)return Collections.emptyList();
        return simpleExcelReader.valueMapping(new AnnotationValueMapping()).configuration(sheetReadConfiguration).read(this.workbook, sheetIndex);
    }

    public boolean hasSheet(String sheetName){
        int sheetIndex = this.workbook.getSheetIndex(sheetName);
        return sheetIndex>-1;
    }


    public Map<Class, List> annotationRead(List<SheetMap> sheetMapList) {
        return sheetMapList.stream().collect(Collectors.toMap(t -> t.getSheetClass(), t -> this.annotationRead(t.getSheetName(), t.getSheetClass())));

    }


    public static class SheetMap {
        /**
         * sheet页名称
         */
        private String sheetName;
        /**
         * sheet页对应的类
         */
        private Class sheetClass;

        public SheetMap(String sheetName, Class sheetClass) {
            this.sheetName = sheetName;
            this.sheetClass = sheetClass;
        }

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public Class getSheetClass() {
            return sheetClass;
        }

        public void setSheetClass(Class sheetClass) {
            this.sheetClass = sheetClass;
        }
    }


}
