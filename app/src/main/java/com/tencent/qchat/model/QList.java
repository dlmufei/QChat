
package com.tencent.qchat.model;

public class QList {

    private Integer code;
    private String message;
    private Data data;

    /**
     * @return The code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code The code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The data
     */
    public Data getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "QList{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
