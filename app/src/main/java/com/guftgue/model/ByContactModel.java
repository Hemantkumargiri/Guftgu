package com.guftgue.model;

import java.util.ArrayList;

public class ByContactModel {

    public String success;
    public String code;
    public String message;
    public ArrayList<enBylist> enBylist;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<com.guftgue.model.enBylist> getEnBylist() {
        return enBylist;
    }

    public void setEnBylist(ArrayList<com.guftgue.model.enBylist> enBylist) {
        this.enBylist = enBylist;
    }
}
