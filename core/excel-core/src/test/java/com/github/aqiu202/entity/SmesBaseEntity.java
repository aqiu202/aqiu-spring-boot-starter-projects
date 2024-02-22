package com.github.aqiu202.entity;

import com.github.aqiu202.excel.anno.ExcelColumn;

import java.time.LocalDateTime;

/**
 * smes基础实体类
 * 如果不需要扩展字段可继承此类
 * 如果需要扩展字段需要继承 @com.noah.smes.customizefield.base.SmesBaseEntityWithJsonField
 *
 * @param <T>
 */
public abstract class SmesBaseEntity<T extends SmesBaseEntity> {

    public SmesBaseEntity() {

    }

    /**
     * 创建人
     */
    @ExcelColumn(value = "创建人", field = "createUser.userName")
    private BaseAccountView createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新操作人
     */
    @ExcelColumn(value = "更新人", field = "updateUser.userName")
    private BaseAccountView updateUser;

    /**
     * 更新操作时间
     */
    private LocalDateTime updateTime;

    /**
     * 记录是否有效（1:有效，0无效）
     */
    private Short valid;


//    public Long getCreateId() {
//        return createId;
//    }
//
//    public void setCreateId(Long createId) {
//        this.createId = createId;
//    }

    public BaseAccountView getCreateUser() {
        return createUser;
    }

    public void setCreateUser(BaseAccountView createAccount) {
        this.createUser = createAccount;
    }

    public BaseAccountView getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(BaseAccountView updateAccount) {
        this.updateUser = updateAccount;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

//    public Long getUpdateId() {
//        return updateId;
//    }
//
//    public void setUpdateId(Long updateId) {
//        this.updateId = updateId;
//    }

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

    public enum BaseEnum {
        //定义一个枚举
        INSERT, UPDATE, DELETE
    }
}
