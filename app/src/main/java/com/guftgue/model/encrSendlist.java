package com.guftgue.model;

public class encrSendlist {
    public String name;
    public String mobile;
    public String encription_type;
    public String encription_name;
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEncription_name() {
        return encription_name;
    }

    public void setEncription_name(String encription_name) {
        this.encription_name = encription_name;
    }

    public String created_date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEncription_type() {
        return encription_type;
    }

    public void setEncription_type(String encription_type) {
        this.encription_type = encription_type;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
