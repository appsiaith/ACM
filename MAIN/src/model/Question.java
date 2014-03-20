package model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 01/11/2013
 * Time: 14:06
 */
public class Question extends Element implements Serializable {

    protected String title = "";
    protected String picture = "";
    protected String questionSouth = "";
    protected String questionNorth = "";
    protected String questionAudioSouth = "";
    protected String questionAudioNorth = "";
    protected String answerSouth = "";
    protected String answerNorth = "";
    protected String answerAudioSouth = "";
    protected String answerAudioNorth = "";

    public Question() {
        super();
    }

    public Question(int id) {
        super(id);
    }

    @Override
    public int getType() {
        return Element.QUESTION;
    }

    @Override
    public String getName() {
        return Element.TEXT_QUESTION;
    }

    @Override
    public Element makeInstance() {
        return new Question(-1);
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public String[] getFieldNames() {
        return new String[]{
                "title",
                "picture",
                "questionSouth",
                "questionNorth",
                "questionAudioSouth",
                "questionAudioNorth",
                "answerSouth",
                "answerNorth",
                "answerAudioSouth",
                "answerAudioNorth"
        };
    }

    @Override
    public String[] getFieldNamesShort() {
        return new String[]{
                "Title",
                "Picture",
                "QSouth",
                "QNorth",
                "QAudioS",
                "QAudioN",
                "ASouth",
                "ANorth",
                "AAudioS",
                "AAudioN"
        };
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getQuestionSouth() {
        return questionSouth;
    }

    public void setQuestionSouth(String questionSouth) {
        this.questionSouth = questionSouth;
    }

    public String getQuestionNorth() {
        return questionNorth;
    }

    public void setQuestionNorth(String questionNorth) {
        this.questionNorth = questionNorth;
    }

    public String getQuestionAudioSouth() {
        return questionAudioSouth;
    }

    public void setQuestionAudioSouth(String questionAudioSouth) {
        this.questionAudioSouth = questionAudioSouth;
    }

    public String getQuestionAudioNorth() {
        return questionAudioNorth;
    }

    public void setQuestionAudioNorth(String questionAudioNorth) {
        this.questionAudioNorth = questionAudioNorth;
    }

    public String getAnswerSouth() {
        return answerSouth;
    }

    public void setAnswerSouth(String answerSouth) {
        this.answerSouth = answerSouth;
    }

    public String getAnswerNorth() {
        return answerNorth;
    }

    public void setAnswerNorth(String answerNorth) {
        this.answerNorth = answerNorth;
    }

    public String getAnswerAudioSouth() {
        return answerAudioSouth;
    }

    public void setAnswerAudioSouth(String answerAudioSouth) {
        this.answerAudioSouth = answerAudioSouth;
    }

    public String getAnswerAudioNorth() {
        return answerAudioNorth;
    }

    public void setAnswerAudioNorth(String answerAudioNorth) {
        this.answerAudioNorth = answerAudioNorth;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + picture.hashCode();
        result = 31 * result + questionSouth.hashCode();
        result = 31 * result + questionNorth.hashCode();
        result = 31 * result + questionAudioSouth.hashCode();
        result = 31 * result + questionAudioNorth.hashCode();
        result = 31 * result + answerSouth.hashCode();
        result = 31 * result + answerNorth.hashCode();
        result = 31 * result + answerAudioSouth.hashCode();
        result = 31 * result + answerAudioNorth.hashCode();
        return result;
    }
}
