package model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 01/11/2013
 * Time: 13:54
 */
public class Dialogue extends Element implements Serializable {

    protected String south = "";
    protected String north = "";
    protected String speaker = "";
    protected String audioSouth = "";
    protected String audioNorth = "";

    public Dialogue() {
        super();
    }

    public Dialogue(int id) {
        super(id);
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

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
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

    @Override
    public int getType() {
        return Element.DIALOG;
    }

    @Override
    public String getName() {
        return Element.TEXT_DIALOG;
    }

    @Override
    public Element makeInstance() {
        return new Dialogue(-1);
    }

    @Override
    public String toString() {
        return south;
    }

    @Override
    public String[] getFieldNames() {
        return new String[]{"south", "north", "speaker", "audioSouth", "audioNorth"};
    }

    @Override
    public String[] getFieldNamesShort() {
        return new String[]{"South", "North", "Speaker", "AudioS", "AudioN"};
    }

    @Override
    public boolean isRaw() {
        if (super.isRaw()) return true;
        if ((south.equals("<p></p>") || south.equals("<p>\n      \n    </p>"))
                && ((north.equals("<p></p>")) || north.equals("<p>\n      \n    </p>"))
                && audioSouth.equals("") && audioNorth.equals("")) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result = south.hashCode();
        result = 31 * result + north.hashCode();
        result = 31 * result + speaker.hashCode();
        result = 31 * result + audioSouth.hashCode();
        result = 31 * result + audioNorth.hashCode();
        return result;
    }
}
