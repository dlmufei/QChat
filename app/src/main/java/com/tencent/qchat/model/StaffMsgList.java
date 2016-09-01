package com.tencent.qchat.model;

/**
 * Created by cliffyan on 2016/9/1.
 */
public class StaffMsgList {
    private Integer code;
    private String message;
    private StaffMsgData data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StaffMsgData getData() {
        return data;
    }

    public void setData(StaffMsgData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StaffMsgList{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
