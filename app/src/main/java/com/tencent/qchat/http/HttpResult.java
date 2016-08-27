package com.tencent.qchat.http;

/**
 * Created by hiwang on 16/8/27.
 */
public class HttpResult<T> {
    public int code;
    public String message;
    public T data;
}
