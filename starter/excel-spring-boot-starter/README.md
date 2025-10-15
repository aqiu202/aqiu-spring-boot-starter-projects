# excel-spring-boot-starter
### 描述
> 基于POI封装的通用Excel导入/导出组件
> - 支持多种方式导出（反射/注解/动态构造/自定义）并支持多sheet页导出

### 使用
##### 1. 引入依赖
```xml
<dependency>
    <groupId>com.github.aqiu202</groupId>
    <artifactId>excel-spring-boot-starter</artifactId>
    <version>${revision}</version>
</dependency>
```
##### 2. 配置
```yaml
excel:
  write: # 导出配置
    head-style: # 表头样式配置
      font: # 字体配置
        bold: true # 是否加粗
      background: # 背景色配置
        name: grey_25_percent
    content-style: # 内容样式配置
      font: # 字体配置
        bold: false # 是否加粗
      border: # 边框配置
        top: none
  read: # 导入配置
    date-format: 'yyyy-MM-dd' # 日期格式
    number-format: '#.##' # 数字格式
    property-accessor: method # 属性访问方式
```

##### 3. 示例
实体数据代码：
```java
/**
 * 台位信息
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
```
导入导出代码：
```java
public class AppTest {
    public static void main(String[] args) {
        ExcelFactory excelFactory = new SimpleExcelFactory();
        // Excel阅读器
        ExcelReader excelReader = excelFactory.forReader().build();
        // 根据字段映射读取
        List<BaseStation> readData = excelReader.index(BaseStation.class, new IndexValueMapping(
                        "code", "name", "", "createTime",
                        "updateTime", "workCenter.code", "workCenter.name", "dictStationType.dictName",
                        "dictStationProduceAttr.dictName", "dictStationSpecialAttr.dictName",
                        "createUser.userName", "updateUser.userName"
                ))
                .read(new File("D:/station.xlsx"));
//        List<HashMap> readData = excelReader.map(HashMap.class)
//                .read(new File("D:/station.xlsx"));
        // Excel写入器
        ExcelWriter excelWriter = excelFactory.forWriter().build();
        // 根据注解导出
        excelWriter.annotation(BaseStation.class).writeData(readData)
                .append(BaseStation.class).write("台位信息", readData)
                .writeTo("D:/station-copy.xlsx");
        System.out.println(readData);
    }
}
```