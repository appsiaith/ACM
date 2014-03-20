package model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 01/11/2013
 * Time: 13:55
 */
public class Lesson extends Element implements Serializable {

    protected String south = "";
    protected String north = "";
    protected String english = "";

    protected ElementGroups<Dialogue> dialogues = new ElementGroups<Dialogue>(Dialogue.class);
    protected ElementGroups<Question> questions = new ElementGroups<Question>(Question.class);
    protected ElementGroups<Grammar> grammars = new ElementGroups<Grammar>(Grammar.class);
    protected ElementGroups<Pattern> patterns = new ElementGroups<Pattern>(Pattern.class);
    protected ElementGroups<Vocabulary> vocabularies = new ElementGroups<Vocabulary>(Vocabulary.class);

    public Lesson() {
        super();
    }

    public Lesson(int id) {
        super(id);
    }

    @Override
    public boolean isRaw() {
        return super.isRaw() & dialogues.isEmpty() & questions.isEmpty() & grammars.isEmpty() & patterns.isEmpty() & vocabularies.isEmpty();
    }

    @Override
    public int getType() {
        return -1;
    }

    @Override
    public String getName() {
        return "Lesson";
    }

    @Override
    public Element makeInstance() {
        return new Lesson(-1);
    }

    @Override
    public String toString() {
        return english.isEmpty() ? "No title" : english;
    }

    @Override
    public String[] getFieldNames() {
        return new String[]{"english", "south", "north"};
    }

    @Override
    public String[] getFieldNamesShort() {
        return new String[]{"English", "South", "North"};
    }

    public Lesson fill() {
        dialogues.fill();
        questions.fill();
        grammars.fill();
        patterns.fill();
        vocabularies.fill();
        return this;
    }

    public ElementGroups getElementGroups(int elementType) {
        switch (elementType) {
            case Element.DIALOG:
                return dialogues;
            case Element.GRAMMAR:
                return grammars;
            case Element.PATTERN:
                return patterns;
            case Element.QUESTION:
                return questions;
            case Element.VOCABULARY:
                return vocabularies;
            default:
                return null;
        }
    }

    public ElementGroups<Dialogue> getDialogues() {
        return dialogues;
    }

    public ElementGroups<Question> getQuestions() {
        return questions;
    }

    public ElementGroups<Grammar> getGrammars() {
        return grammars;
    }

    public ElementGroups<Pattern> getPatterns() {
        return patterns;
    }

    public ElementGroups<Vocabulary> getVocabularies() {
        return vocabularies;
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

    @Override
    public int hashCode() {
        int result = south.hashCode();
        result = 31 * result + north.hashCode();
        result = 31 * result + english.hashCode();
        result = 31 * result + dialogues.hashCode();
        result = 31 * result + questions.hashCode();
        result = 31 * result + grammars.hashCode();
        result = 31 * result + patterns.hashCode();
        result = 31 * result + vocabularies.hashCode();
        return result;
    }
}
