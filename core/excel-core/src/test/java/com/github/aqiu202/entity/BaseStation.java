package com.github.aqiu202.entity;


import com.github.aqiu202.excel.anno.ExcelColumn;

/**
 * 台位
 */
public class BaseStation extends SmesBaseEntity<BaseStation> {

    /**
     * 台位编号
     */
    private String code;
    /**
     * 台位名称
     */
    private String name;
    /**
     * 台位描述
     */
    private String description;
    /**
     * 台位类型
     */
    @ExcelColumn(value = "台位类型", field = "dictStationType.dictName")
    private DictStationType dictStationType;
    /**
     * 台位生产属性
     */
    @ExcelColumn(value = "台位生产属性", field = "dictStationProduceAttr.dictName")
    private DictStationProduceAttr dictStationProduceAttr;

    /**
     * 台位特殊属性
     */
    @ExcelColumn(value = "台位特殊属性", field = "dictStationSpecialAttr.dictName")
    private DictStationSpecialAttr dictStationSpecialAttr;
    /**
     * 所属工作中心
     */
    @ExcelColumn(value = "工作中心编号", field = "workCenter.code")
    @ExcelColumn(value = "工作中心名称", field = "workCenter.name")
    private BaseWorkCenter workCenter;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BaseWorkCenter getWorkCenter() {
        return workCenter;
    }

    public void setWorkCenter(BaseWorkCenter workCenter) {
        this.workCenter = workCenter;
    }

    public DictStationSpecialAttr getDictStationSpecialAttr() {
        return dictStationSpecialAttr;
    }

    public void setDictStationSpecialAttr(DictStationSpecialAttr dictStationSpecialAttr) {
        this.dictStationSpecialAttr = dictStationSpecialAttr;
    }

    public DictStationType getDictStationType() {
        return dictStationType;
    }

    public void setDictStationType(DictStationType dictStationType) {
        this.dictStationType = dictStationType;
    }

    public DictStationProduceAttr getDictStationProduceAttr() {
        return dictStationProduceAttr;
    }

    public void setDictStationProduceAttr(DictStationProduceAttr dictStationProduceAttr) {
        this.dictStationProduceAttr = dictStationProduceAttr;
    }
}
