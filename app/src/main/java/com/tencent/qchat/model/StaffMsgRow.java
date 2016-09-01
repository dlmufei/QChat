package com.tencent.qchat.model;

/**
 *
 * Created by cliffyan on 2016/9/1.
 */
public class StaffMsgRow {
    private String notification_type;
    private Integer question_id;
    private String question_content;
    private Boolean question_is_hot;
    private Integer question_time;
    private String question_url;
    private Integer answer_count;

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public Boolean getQuestion_is_hot() {
        return question_is_hot;
    }

    public void setQuestion_is_hot(Boolean question_is_hot) {
        this.question_is_hot = question_is_hot;
    }

    public Integer getQuestion_time() {
        return question_time;
    }

    public void setQuestion_time(Integer question_time) {
        this.question_time = question_time;
    }

    public String getQuestion_url() {
        return question_url;
    }

    public void setQuestion_url(String question_url) {
        this.question_url = question_url;
    }

    public Integer getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(Integer answer_count) {
        this.answer_count = answer_count;
    }

    @Override
    public String toString() {
        return "StaffMsgRow{" +
                "notification_type='" + notification_type + '\'' +
                ", question_id=" + question_id +
                ", question_content='" + question_content + '\'' +
                ", question_is_hot=" + question_is_hot +
                ", question_time=" + question_time +
                ", question_url='" + question_url + '\'' +
                ", answer_count=" + answer_count +
                '}';
    }
}
