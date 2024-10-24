package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.meta.IndexedMeta;
import com.github.aqiu202.excel.meta.DataMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SimpleFormulaResolver implements FormulaResolver {

    private String variablePrefix = "${";
    private String variableSuffix = "}";

    private String variableRegex;

    public SimpleFormulaResolver() {
    }

    public SimpleFormulaResolver(String variablePrefix, String variableSuffix) {
        this.variablePrefix = variablePrefix;
        this.variableSuffix = variableSuffix;
    }

    @Override
    public String getVariablePrefix() {
        return variablePrefix;
    }

    public void setVariablePrefix(String variablePrefix) {
        this.variablePrefix = variablePrefix;
    }

    @Override
    public String getVariableSuffix() {
        return variableSuffix;
    }

    public void setVariableSuffix(String variableSuffix) {
        this.variableSuffix = variableSuffix;
    }

    @Override
    public String resolve(String formula, int rowIndex, List<? extends DataMeta> metaList) {
        List<IndexedMeta> indexedMetas = this.resolveIndexedMeta(metaList);
        Map<String, Integer> metaKeyIndexMap = indexedMetas.stream()
                .collect(Collectors.toMap(IndexedMeta::getKey, IndexedMeta::getIndex));
        return this.resolveFormula(formula, metaKeyIndexMap, rowIndex);
    }

    private List<IndexedMeta> resolveIndexedMeta(List<? extends DataMeta> metaList) {
        List<IndexedMeta> results = new ArrayList<>();
        for (int i = 0; i < metaList.size(); i++) {
            DataMeta dataMeta = metaList.get(i);
            results.add(new IndexedMeta(i, dataMeta));
        }
        return results;
    }

    private String resoleColumnName(int rowIndex, int colIndex) {
        int row = rowIndex + 1;
        int column = colIndex + 1;
        if (row < 1 || column < 1) {
            throw new IllegalArgumentException("Row and column numbers must be greater than 0.");
        }
        StringBuilder sb = new StringBuilder();
        // 处理列号（转换为A-Z, AA-ZZ, ...）
        while (column > 0) {
            column--; // Excel的列号是从1开始的，但计算时要从0开始
            int remainder = column % 26;
            sb.insert(0, (char) (remainder + 'A')); // 将余数转换为字母并插入到字符串的前面
            column /= 26; // 更新列号，继续处理下一个26进制位
        }
        // 添加行号
        sb.append(row);
        return sb.toString();
    }

    /**
     * 解析文本（将表名替换为别名）
     *
     * @param text 待解析文本
     * @return 解析后的文本
     */
    public String resolveFormula(String text, Map<String, Integer> metaKeyIndexMap, int rowIndex) {
        // 正则表达式匹配变量模式
        if (this.variableRegex == null) {
            String vp = this.getVariablePrefix();
            String vs = this.getVariableSuffix();
            String qvs = Pattern.quote(vs);
            this.variableRegex = Pattern.quote(vp) + "([^" + qvs + "]+)" + qvs;
        }
        Pattern pattern = Pattern.compile(this.variableRegex);
        Matcher matcher = pattern.matcher(text);
        // 替换变量
        StringBuffer output = new StringBuffer();
        while (matcher.find()) {
            String varName = matcher.group(1); // 获取变量名
            // 有表别名的替换为表别名，否则去掉字段的表名前缀
            Integer colIndex = metaKeyIndexMap.get(varName);
            if (colIndex != null) {
                matcher.appendReplacement(output, this.resoleColumnName(rowIndex, colIndex));
            }
        }
        matcher.appendTail(output);
        return output.toString();
    }


}
