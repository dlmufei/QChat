package com.tencent.qchat.model;

/**
 * Created by cliffyan on 2016/8/30.
 */
public class MsgList {

    private Integer code;
    private String message;
    private MsgData data;

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

    public MsgData getData() {
        return data;
    }

    public void setData(MsgData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MsgList{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
