package com.github.aqiu202.entity;


public class BaseAccountView {

    /**
     * 人员表主键id
     */
    private Long id;

    /**
     * 账号表account
     */
    private String loginName;

    /**
     * 人员表姓名
     */
    private String userName;


    public Long getId() {
        return id;
    }

    public void setId(Long userId) {
        this.id = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
