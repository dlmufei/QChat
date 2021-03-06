
package com.tencent.qchat.model;

public class Row {

    private Integer question_id;
    private String question_content;
    private Boolean question_is_hot;
    private Long question_time;
    private String question_url;
    private Integer answer_count;
    private AnswerLead answer_lead;
    private Boolean is_fresh;


    public Boolean getIs_fresh() {
        return is_fresh;
    }

    public void setIs_fresh(Boolean is_fresh) {
        this.is_fresh = is_fresh;
    }

    /**
     * @return The questionId
     */
    public Integer getQuestionId() {
        return question_id;
    }

    /**
     * @param questionId The question_id
     */
    public void setQuestionId(Integer questionId) {
        this.question_id = questionId;
    }

    /**
     * @return The questionContent
     */
    public String getQuestionContent() {
        return question_content;
    }

    /**
     * @param questionContent The question_content
     */
    public void setQuestionContent(String questionContent) {
        this.question_content = questionContent;
    }

    /**
     * @return The questionIsHot
     */
    public Boolean getQuestionIsHot() {
        return question_is_hot;
    }

    /**
     * @param questionIsHot The question_is_hot
     */
    public void setQuestionIsHot(Boolean questionIsHot) {
        this.question_is_hot = questionIsHot;
    }

    public Long getQuestionTime() {
        return question_time;
    }

    public void setQuestionTime(Long question_time) {
        this.question_time = question_time;
    }

    /**
     * @return The questionUrl
     */
    public String getQuestionUrl() {
        return question_url;
    }

    /**
     * @param questionUrl The question_url
     */
    public void setQuestionUrl(String questionUrl) {
        this.question_url = questionUrl;
    }

    /**
     * @return The answerCount
     */
    public Integer getAnswerCount() {
        return answer_count;
    }

    /**
     * @param answerCount The answer_count
     */
    public void setAnswerCount(Integer answerCount) {
        this.answer_count = answerCount;
    }

    /**
     * @return The answerLead
     */
    public AnswerLead getAnswerLead() {
        return answer_lead;
    }

    /**
     * @param answerLead The answer_lead
     */
    public void setAnswerLead(AnswerLead answerLead) {
        this.answer_lead = answerLead;
    }

    @Override
    public String toString() {
        return "Row{" +
                "question_id=" + question_id +
                ", question_content='" + question_content + '\'' +
                ", question_is_hot=" + question_is_hot +
                ", question_time=" + question_time +
                ", question_url='" + question_url + '\'' +
                ", answer_count=" + answer_count +
                ", answer_lead=" + answer_lead +
                '}';
    }
}
