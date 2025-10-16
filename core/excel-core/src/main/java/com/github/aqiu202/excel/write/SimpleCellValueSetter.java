package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.style.StyleProcessor;
import com.github.aqiu202.excel.format.wrap.*;
import com.github.aqiu202.util.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SimpleCellValueSetter implements CellValueSetter {

    @Override
    public void setCellValue(Cell cell, ValueWrapper<?> result, StyleProcessor styleProcessor, SheetWriteConfiguration configuration) {
        if (result instanceof NumberValueWrapper) {
            cell.setCellValue(((NumberValueWrapper) result).getValue().doubleValue());
        } else if (result instanceof StringValueWrapper) {
            cell.setCellValue(((StringValueWrapper) result).getValue());
        } else if (result instanceof FormatedValueWrapper) {
            FormatedValueWrapper frw = (FormatedValueWrapper) result;
            ValueWrapper<?> original = frw.getOriginal();
            if (original instanceof NumberValueWrapper) {
                cell.setCellValue(((NumberValueWrapper) original).getValue().doubleValue());
            } else {
                cell.setCellValue(((FormatedValueWrapper) result).getValue());
            }
        } else if (result instanceof DateValueWrapper) {
            DataFormat dataFormat = styleProcessor.getCreationHelper().createDataFormat();
            styleProcessor.modifyCellStyle(cell,
                    p -> {
                        p.setDataFormat(dataFormat.getFormat(configuration.getDefaultDateFormat()));
                        return p;
                    });
            cell.setCellValue(((DateValueWrapper) result).getValue());
        } else if (result instanceof ImageValueWrapper) {
            this.setImageValue(cell, (ImageValueWrapper) result);
        }
    }

    /**
     * 设置图片
     * @param cell 单元格
     * @param result 图片url
     */
    protected void setImageValue(Cell cell, ImageValueWrapper result) {
        String url = result.getValue();
        if (StringUtils.isBlank(url)) {
            return;
        }
        Sheet sheet = cell.getRow().getSheet();
        Drawing<?> drawingPatriarch = sheet.createDrawingPatriarch();
        int colIndex = cell.getColumnIndex();
        int rowIndex = cell.getRowIndex();
        ClientAnchor anchor = drawingPatriarch.createAnchor(0, 0, 0, 0, colIndex, rowIndex, colIndex + 1, rowIndex + 1);
        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
        byte[] imageData;
        try {
            imageData = this.readImageUrl(url);
        } catch (IOException ignored) {
            // TODO 读取网络图片失败，暂时忽略不处理
            return;
        }
        Workbook workbook = sheet.getWorkbook();
        int picId = workbook.addPicture(imageData, Workbook.PICTURE_TYPE_PNG);
//        Hyperlink hyperlink = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
//        hyperlink.setAddress(url);
//        cell.setHyperlink(hyperlink);
        drawingPatriarch.createPicture(anchor, picId);
    }

    protected byte[] readImageUrl(String url) throws IOException {
        URL httpUrl = new URL(url);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (InputStream inputStream = httpUrl.openStream()) {
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int n;
            while ((n = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
        }
        return out.toByteArray();
    }
}
