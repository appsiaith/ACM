package util;

import model.*;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 18/01/14
 * Time: 14:54
 */
public class Config {
    public static String PROJECT_NAME = "New Project";
    public static String IMAGE_FOLDER = "image";
    public static String AUDIO_FOLDER = "audio";
    public static String HTML_FOLDER = "html";
    public static String DATABASE_FILE = "database";
    public static String ABOUT_NAME = "About";
    public static String LESSON_NAME = "Unit";
    public static String DIALOG_NAME = "Dialogue";
    public static String GRAMMAR_NAME = "Grammar";
    public static String QUESTION_NAME = "Question";
    public static String PATTERN_NAME = "Pattern";
    public static String VOCABULARY_NAME = "Vocabulary";
    public static String SOUTH_NAME = "South Wales";
    public static String NORTH_NAME = "North Wales";
    public static String ENGLISH_NAME = "English";
    public static String AUTO_APPEND = "NO";
    public static String CLEAN_BEFORE_SAVE = "YES";

    public static String getProjectFolder() {
        return new File(DATABASE_FILE).getParent() + File.separator;
    }

    public static String getAudioFolder() {
        return new File(DATABASE_FILE).getParent() + File.separator + AUDIO_FOLDER + File.separator;
    }

    public static String getHTMLFolder() {
        return new File(DATABASE_FILE).getParent() + File.separator + HTML_FOLDER + File.separator;
    }

    public static String getImageFolder() {
        return new File(DATABASE_FILE).getParent() + File.separator + IMAGE_FOLDER + File.separator;
    }

    public static String decorate(String text) {
        if (text.equals("text")) return null;
        if (text.equals("textAlt")) return null;
        if (text.equals("translation")) return null;
        if (text.equals("south")) return SOUTH_NAME;
        if (text.equals("north")) return NORTH_NAME;
        if (text.equals("english")) return ENGLISH_NAME;

        StringBuffer buffer = new StringBuffer();
        for (char c : text.toCharArray()) {
            if (Character.isUpperCase(c)) buffer.append(" " + c);
            else buffer.append(c);
        }
        text = buffer.toString();
        text = text.trim();
        text = text.replaceAll("  ", " ");
        return text.substring(0, 1).toUpperCase() + text.substring(1, text.length());
    }

    public static String[] getElementNames() {
        return new String[]{PATTERN_NAME, QUESTION_NAME, VOCABULARY_NAME, GRAMMAR_NAME, DIALOG_NAME};
    }

    public static String[][] getElementFieldNames() {
        String[][] fieldNames = new String[5][];
        fieldNames[0] = new Pattern(-1).getFieldNames();
        fieldNames[1] = new Question(-1).getFieldNames();
        fieldNames[2] = new Vocabulary(-1).getFieldNames();
        fieldNames[3] = new Grammar(-1).getFieldNames();
        fieldNames[4] = new Dialogue(-1).getFieldNames();
        return fieldNames;
    }

    public static boolean isAutoAppend() {
        return AUTO_APPEND.equals("YES");
    }

    public static boolean isAutoClean() {
        return CLEAN_BEFORE_SAVE.equals("YES");
    }
}
