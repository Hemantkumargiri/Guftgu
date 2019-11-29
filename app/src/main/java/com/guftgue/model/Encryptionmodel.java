package com.guftgue.model;

import java.util.ArrayList;

public class Encryptionmodel {
    public String success;
    public String code;
    public String message;
    public ArrayList<encrSendlist> listencrSendlist;

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

    public ArrayList<encrSendlist> getListencrSendlist() {
        return listencrSendlist;
    }

    public void setListencrSendlist(ArrayList<encrSendlist> listencrSendlist) {
        this.listencrSendlist = listencrSendlist;
    }
}
