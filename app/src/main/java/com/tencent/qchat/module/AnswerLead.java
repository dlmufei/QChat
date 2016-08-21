
package com.tencent.qchat.module;


public class AnswerLead {


    private Integer answer_id;
    private String user_avatar;
    private String user_nickname;
    private String user_title;
    private Integer answer_time;
    private String answer_type;
    private String answer_content;

    /**
     * @return The answerId
     */
    public Integer getAnswerId() {
        return answer_id;
    }

    /**
     * @param answerId The answer_id
     */
    public void setAnswerId(Integer answerId) {
        this.answer_id = answerId;
    }

    /**
     * @return The userAvatar
     */
    public String getUserAvatar() {
        return user_avatar;
    }

    /**
     * @param userAvatar The user_avatar
     */
    public void setUserAvatar(String userAvatar) {
        this.user_avatar = userAvatar;
    }

    /**
     * @return The userNickname
     */
    public String getUserNickname() {
        return user_nickname;
    }

    /**
     * @param userNickname The user_nickname
     */
    public void setUserNickname(String userNickname) {
        this.user_nickname = userNickname;
    }

    /**
     * @return The userTitle
     */
    public String getUserTitle() {
        return user_title;
    }

    /**
     * @param userTitle The user_title
     */
    public void setUserTitle(String userTitle) {
        this.user_title = userTitle;
    }

    /**
     * @return The answerTime
     */
    public Integer getAnswerTime() {
        return answer_time;
    }

    /**
     * @param answerTime The answer_time
     */
    public void setAnswerTime(Integer answerTime) {
        this.answer_time = answerTime;
    }

    /**
     * @return The answerType
     */
    public String getAnswerType() {
        return answer_type;
    }

    /**
     * @param answerType The answer_type
     */
    public void setAnswerType(String answerType) {
        this.answer_type = answerType;
    }

    /**
     * @return The answerContent
     */
    public String getAnswerContent() {
        return answer_content;
    }

    /**
     * @param answerContent The answer_content
     */
    public void setAnswerContent(String answerContent) {
        this.answer_content = answerContent;
    }

    @Override
    public String toString() {
        return "AnswerLead{" +
                "answer_id=" + answer_id +
                ", user_avatar='" + user_avatar + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                ", user_title='" + user_title + '\'' +
                ", answer_time=" + answer_time +
                ", answer_type='" + answer_type + '\'' +
                ", answer_content='" + answer_content + '\'' +
                '}';
    }
}
