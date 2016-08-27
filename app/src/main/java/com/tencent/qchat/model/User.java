package com.tencent.qchat.model;

/**
 * Created by hiwang on 16/8/27.
 */
public class User {

    private String nickname;       // 昵称
    private String avatar;       // 头像
    private String open_type;       // qq wechat
    private String open_id;       // 唯一标识
    private boolean is_staff;       // 是否是员工(嘉宾)

    public User(String nickname, String avatar, String open_type, String open_id, boolean is_staff) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.open_type = open_type;
        this.open_id = open_id;
        this.is_staff = is_staff;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOpen_type() {
        return open_type;
    }

    public void setOpen_type(String open_type) {
        this.open_type = open_type;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public boolean is_staff() {
        return is_staff;
    }

    public void setIs_staff(boolean is_staff) {
        this.is_staff = is_staff;
    }

}
