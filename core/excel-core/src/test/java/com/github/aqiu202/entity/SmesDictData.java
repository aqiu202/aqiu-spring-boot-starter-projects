package com.github.aqiu202.entity;

public abstract class SmesDictData<T extends SmesDictData> {
    /**
     * 可选项编号
     */
    private Short dictCode;

    /**
     * 可选项名称
     */
    private String dictName;

    /**
     * 可选项排序
     */
    private Short dictSort;

    /**
     * 样式
     */
    private String cssClass;

    /**
     * 是否默认
     */
    private Short isDefault;

    /**
     * 可选项状态
     */
    private Short dictStatus;

    /**
     * 备注
     */
    private String dictRemark;

    /**
     * 可选项描述
     */
    private String dictDesc;


    public SmesDictData() {
    }

    public SmesDictData(Long id, Short dictCode, String dictName, Short dictSort) {
        this.dictCode = dictCode;
        this.dictName = dictName;
        this.dictSort = dictSort;
        this.dictStatus = (short) 1;

    }

    public Short getDictCode() {
        return dictCode;
    }

    public void setDictCode(Short dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public Short getDictSort() {
        return dictSort;
    }

    public void setDictSort(Short dictSort) {
        this.dictSort = dictSort;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public Short getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Short isDefault) {
        this.isDefault = isDefault;
    }

    public Short getDictStatus() {
        return dictStatus;
    }

    public void setDictStatus(Short dictStatus) {
        this.dictStatus = dictStatus;
    }

    public String getDictRemark() {
        return dictRemark;
    }

    public void setDictRemark(String dictRemark) {
        this.dictRemark = dictRemark;
    }

    public String getDictDesc() {
        return dictDesc;
    }

    public void setDictDesc(String dictDesc) {
        this.dictDesc = dictDesc;
    }

}
