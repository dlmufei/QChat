package com.tencent.qchat.model;

/**
 * 问题的回答者，即员工
 *
 * Created by cliffyan on 2016/8/30.
 */
public class StaffRow {
    private int id;//用户id
    private String avatar;//用户头像
    private String nickname;//用户昵称
    private String title;//用户头衔
    private String skilled_field;//擅长回答的领域
    private String profile;//我的简介

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSkilled_field() {
        return skilled_field;
    }

    public void setSkilled_field(String skilled_field) {
        this.skilled_field = skilled_field;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "StaffRow{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", title='" + title + '\'' +
                ", skilled_field='" + skilled_field + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }
}
