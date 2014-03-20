package model;

import database.Database;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 03/11/13
 * Time: 11:49
 */
public abstract class Element implements Serializable, FieldRetrievable {

    public static final int PATTERN = 0;
    public static final int QUESTION = 1;
    public static final int VOCABULARY = 2;
    public static final int GRAMMAR = 3;
    public static final int DIALOG = 4;
    public static final int ABOUT = 5;
    public static final String TEXT_DIALOG = "dialog";
    public static final String TEXT_GRAMMAR = "grammar";
    public static final String TEXT_PATTERN = "pattern";
    public static final String TEXT_QUESTION = "question";
    public static final String TEXT_VOCABULARY = "vocabulary";
    public static final String TEXT_ABOUT = "about";
    public static final int[] ELEMENT_TYPES = new int[]{PATTERN, QUESTION, VOCABULARY, GRAMMAR, DIALOG};
    public static final String[] ELEMENT_TEXTS = new String[]{TEXT_PATTERN, TEXT_QUESTION, TEXT_VOCABULARY, TEXT_GRAMMAR, TEXT_DIALOG};
    public static final Class[] ELEMENT_CLASSES = new Class[]{Pattern.class, Question.class, Vocabulary.class, Grammar.class, Dialogue.class};
    public static final Element[] ELEMENT_INSTANCES = new Element[]{new Pattern(-1), new Question(-1), new Vocabulary(-1), new Grammar(-1), new Dialogue(-1)};

    protected Integer id = null;

    public Element() {
        id = Database.next();
    }

    public Element(int id) {
        this.id = id;
    }

    public boolean isRaw() {
        String[] fieldNames = getFieldNames();
        Element newInstance = makeInstance();
        for (int f = 0; f < fieldNames.length; f++)
            if (!getField(fieldNames[f]).equals(newInstance.getField(fieldNames[f]))) return false;
        return true;
    }

    public boolean setField(String key, String value) {
        boolean match = false;
        for (int f = 0; f < getFieldNames().length; f++) {
            if (getFieldNames()[f].equals(key)) match = true;
        }
        if (!match) return false;
        try {
            Field field = this.getClass().getDeclaredField(key);
            if (int.class.isAssignableFrom(field.getType())) field.setInt(this, Integer.parseInt(value));
            else field.set(this, value);
            return true;
        } catch (NumberFormatException e) {
            setField(key, "0");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getField(String key) {
        boolean match = false;
        for (int f = 0; f < getFieldNames().length; f++) {
            if (getFieldNames()[f].equals(key)) match = true;
        }
        if (!match) return null;
        try {
            Field field = this.getClass().getDeclaredField(key);
            field.setAccessible(true);
            return field.get(this) == null ? "" : field.get(this).toString();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract int getType();

    public abstract String getName();

    public abstract Element makeInstance();

    public abstract String toString();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        if (id != null ? !id.equals(element.id) : element.id != null) return false;

        return true;
    }

    @Override
    public abstract int hashCode();
}
