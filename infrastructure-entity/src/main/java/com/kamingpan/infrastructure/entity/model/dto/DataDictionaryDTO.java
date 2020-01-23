package com.kamingpan.infrastructure.entity.model.dto;

import com.kamingpan.infrastructure.entity.group.DataDictionaryGroup;
import com.kamingpan.infrastructure.entity.model.entity.DataDictionary;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 数据字典 dto
 *
 * @author kamingpan
 * @since 2019-02-15
 */
@Data
public class DataDictionaryDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 标签
     */
    @NotEmpty(message = "标签不能为空", groups = {DataDictionaryGroup.Insert.class, DataDictionaryGroup.Update.class})
    private String label;

    /**
     * 值
     */
    @NotEmpty(message = "值不能为空", groups = {DataDictionaryGroup.Insert.class, DataDictionaryGroup.Update.class})
    private String value;

    /**
     * 类型
     */
    @NotEmpty(message = "类型不能为空", groups = {DataDictionaryGroup.Insert.class, DataDictionaryGroup.Update.class})
    private String type;

    /**
     * 类/表
     */
    @NotEmpty(message = "类不能为空", groups = DataDictionaryGroup.Insert.class)
    private String clazz;

    /**
     * 变量/字段
     */
    @NotEmpty(message = "变量不能为空", groups = DataDictionaryGroup.Insert.class)
    private String variable;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {DataDictionaryGroup.Insert.class, DataDictionaryGroup.Update.class})
    @Digits(integer = 4, fraction = 0, message = "排序只能为整数",
            groups = {DataDictionaryGroup.Insert.class, DataDictionaryGroup.Update.class})
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    public DataDictionary toDataDictionary() {
        DataDictionary dataDictionary = new DataDictionary();

        // 赋值
        dataDictionary.setId(this.getId());
        dataDictionary.setLabel(this.getLabel());
        dataDictionary.setValue(this.getValue());
        dataDictionary.setType(this.getType());
        dataDictionary.setClazz(this.getClazz());
        dataDictionary.setVariable(this.getVariable());
        dataDictionary.setSort(this.getSort());
        dataDictionary.setRemark(this.getRemark());

        return dataDictionary;
    }

}
