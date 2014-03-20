package model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 01/11/2013
 * Time: 13:57
 */
public class Vocabulary extends Element implements Serializable {

    protected String south = "";
    protected String north = "";
    protected String english = "";
    protected String audioSouth = "";
    protected String audioNorth = "";
    protected String gender = "";
    protected String wordClass = "";

    public Vocabulary() {
        super();
    }

    public Vocabulary(int id) {
        super(id);
    }

    @Override
    public int getType() {
        return Element.VOCABULARY;
    }

    @Override
    public String getName() {
        return Element.TEXT_VOCABULARY;
    }

    @Override
    public Element makeInstance() {
        return new Vocabulary(-1);
    }

    public String getSouth() {
        return south;
    }

    public void setSouth(String south) {
        this.south = south;
    }

    public String getNorth() {
        return north;
    }

    public void setNorth(String north) {
        this.north = north;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getAudioSouth() {
        return audioSouth;
    }

    public void setAudioSouth(String audioSouth) {
        this.audioSouth = audioSouth;
    }

    public String getAudioNorth() {
        return audioNorth;
    }

    public void setAudioNorth(String audioNorth) {
        this.audioNorth = audioNorth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWordClass() {
        return wordClass;
    }

    public void setWordClass(String wordClass) {
        this.wordClass = wordClass;
    }

    @Override
    public String toString() {
        return english;
    }

    @Override
    public String[] getFieldNames() {
        return new String[]{"english", "south", "north", "audioSouth", "audioNorth", "gender", "wordClass"};
    }

    @Override
    public String[] getFieldNamesShort() {
        return new String[]{"English", "South", "North", "AudioS", "AudioN", "Gender", "WordClass"};
    }

    @Override
    public int hashCode() {
        int result = south.hashCode();
        result = 31 * result + north.hashCode();
        result = 31 * result + english.hashCode();
        result = 31 * result + audioSouth.hashCode();
        result = 31 * result + audioNorth.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + wordClass.hashCode();
        return result;
    }
}
