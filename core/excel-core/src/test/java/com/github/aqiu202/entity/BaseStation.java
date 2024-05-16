package com.github.aqiu202.entity;


import com.github.aqiu202.excel.anno.ExcelColumn;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 台位
 */
public class BaseStation {

    /**
     * 台位编号
     */
    @ExcelColumn(value = {"台位信息", "台位编号"})
    private String code;
    /**
     * 台位名称
     */
    @ExcelColumn(value = "台位名称")
    private String name;
    @ExcelColumn(value = "总数")
    private BigDecimal total;
    @ExcelColumn(value = "系数")
    private BigDecimal coefficient;
    /**
     * 台位描述
     */
    @ExcelColumn(value = "有效数值", formula = "${total}*${coefficient}")
    private BigDecimal validTotal;

    @ExcelColumn(value = "预览图", image = true)
    private String url;
    /**
     * 所属工作中心
     */
    @ExcelColumn(value = "工作中心编号", field = "workCenter.code")
    @ExcelColumn(value = "工作中心名称", field = "workCenter.name")
    private BaseWorkCenter workCenter;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新操作时间
     */
    private LocalDateTime updateTime;

    /**
     * 记录是否有效（1:有效，0无效）
     */
    @ExcelColumn(value = "是否有效", converter = "convertValid")
    private Short valid;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public BigDecimal getValidTotal() {
        return validTotal;
    }

    public void setValidTotal(BigDecimal validTotal) {
        this.validTotal = validTotal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BaseWorkCenter getWorkCenter() {
        return workCenter;
    }

    public void setWorkCenter(BaseWorkCenter workCenter) {
        this.workCenter = workCenter;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Short getValid() {
        return valid;
    }

    public void setValid(Short valid) {
        this.valid = valid;
    }
}
