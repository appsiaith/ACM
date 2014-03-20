package util;

import view.Main;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 02/11/13
 * Time: 10:16
 */
public class ProgramSettings {
    public static final int MAX_HEIGHT = 600;
    public static String IMG_ROOT = "/img/";
    public static final String AUDIO_EXT = ".mp3";
    public static final int IMG_WIDTH = 110;
    public static final int IMG_HEIGHT = 125;
    public static final int MARGIN = 2;
    public static String last_AUDIO_ROOT = null;
    public static String last_FILE_ROOT = null;
    public static String last_IMG_ROOT = null;
    public static int LIST_SCROLL_WIDTH = 45;
    public static int BUTTON_HEIGHT = 24;
    public static int DETAIL_PANEL_WIDTH = 600;
    public static int DETAIL_PANEL_HEIGHT = 400;
    public static int LESSON_LENGTH = 30;

    public static String NO_IMAGE = IMG_ROOT + "no_image.png";
    public static String NO_AUDIO = IMG_ROOT + "beep.mp3";

    public static Color backColor = new Color(180, 180, 180);
    //    public static Color midColor = new Color(200, 200, 200);
    public static Color frontColor = new Color(225, 225, 225);

    public static String RECENT1;
    public static String RECENT2;
    public static String RECENT3;

    public static String getLastFileDirectory() {
        if (last_FILE_ROOT == null || last_FILE_ROOT.isEmpty() || !new File(last_FILE_ROOT).exists())
            return Config.getProjectFolder();
        return last_FILE_ROOT;
    }

    public static String getLastAudioDirectory() {
        if (last_AUDIO_ROOT == null || last_AUDIO_ROOT.isEmpty() || !new File(last_AUDIO_ROOT).exists())
            return Config.getAudioFolder();
        return last_AUDIO_ROOT;
    }

    public static String getLastImageDirectory() {
        if (last_IMG_ROOT == null || last_IMG_ROOT.isEmpty() || !new File(last_IMG_ROOT).exists())
            return Config.getImageFolder();
        return last_IMG_ROOT;
    }

    public static void setLastFileDirectory(File path) {
        last_FILE_ROOT = path.getParent();
    }

    public static String convertKeyword(String input) {
        if (input.equals("english")) return "english";
        if (input.equals("instruction")) return "english";
        if (input.equals("south")) return "south";
        if (input.equals("title")) return "title";
        if (input.equals("cymraeg")) return "south";
        if (input.equals("sContent")) return "south";
        if (input.equals("north")) return "north";
        if (input.equals("nContent")) return "north";
        if (input.equals("audio")) return "audioSouth";
        if (input.equals("gender")) return "gender";
        if (input.equals("speechType")) return "wordClass";
        if (input.equals("speaker")) return "speaker";
        if (input.equals("html")) return "html";
        if (input.equals("lessonId")) return Config.LESSON_NAME.toLowerCase() + "Id";
        if (input.equals("lessonNum")) return Config.LESSON_NAME.toLowerCase() + "Id";
        if (input.equals("patternId")) return "grouping";
        if (input.equals("exerciseId")) return "grouping";
        if (input.equals("exerciseNum")) return "ordering";
        if (input.equals("qText")) return "questionSouth";
        if (input.equals("NqText")) return "questionNorth";
        if (input.equals("qAudio")) return "questionAudioSouth";
        if (input.equals("ansText")) return "answerSouth";
        if (input.equals("NansText")) return "answerNorth";
        if (input.equals("ansAudio")) return "answerAudioSouth";
        if (input.equals("exerciseId")) return "exerciseId";
        if (input.equals("text")) return "south";
        if (input.equals("textAlt")) return "north";
        if (input.equals("translation")) return "english";
        return "";
    }

    public static void loadParameters() {
        Properties properties = new Properties();
        InputStream is;

        // First try loading from the current directory
        try {
            File f = new File("settings.properties");
            is = new FileInputStream(f);
            properties.load(is);
        } catch (Exception e) {
            e.printStackTrace();
//            is = null;
        }

//        try {
//            if (is == null) {
                // Try loading from classpath
//                is = ProgramSettings.class.getResourceAsStream("settings.properties");
//            }

            // Try loading properties from the file (if found)

//        } catch (Exception e) {
//        }

        RECENT1 = properties.getProperty("RECENT1", "");
        RECENT2 = properties.getProperty("RECENT2", "");
        RECENT3 = properties.getProperty("RECENT3", "");
        last_AUDIO_ROOT = properties.getProperty("last_AUDIO_ROOT", "");
        last_FILE_ROOT = properties.getProperty("last_FILE_ROOT", "");
        last_IMG_ROOT = properties.getProperty("last_IMG_ROOT", "");
    }

    public static void saveParameters() {
        try {
            Properties properties = new Properties();
            properties.setProperty("RECENT1", RECENT1);
            properties.setProperty("RECENT2", RECENT2);
            properties.setProperty("RECENT3", RECENT3);
            properties.setProperty("last_AUDIO_ROOT", last_AUDIO_ROOT);
            properties.setProperty("last_FILE_ROOT", last_FILE_ROOT);
            properties.setProperty("last_IMG_ROOT", last_IMG_ROOT);
            File f = new File("settings.properties");
            OutputStream out = new FileOutputStream(f);
            properties.store(out, "...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateRecent(String recent) {
        ArrayList<String> recents = new ArrayList<String>();
        recents.add(recent);
        if (!recents.contains(ProgramSettings.RECENT1)) recents.add(ProgramSettings.RECENT1);
        if (!recents.contains(ProgramSettings.RECENT2)) recents.add(ProgramSettings.RECENT2);
        if (!recents.contains(ProgramSettings.RECENT3)) recents.add(ProgramSettings.RECENT3);
        ProgramSettings.RECENT1 = recents.size() > 0 ? recents.get(0) : "";
        ProgramSettings.RECENT2 = recents.size() > 1 ? recents.get(1) : "";
        ProgramSettings.RECENT3 = recents.size() > 2 ? recents.get(2) : "";
        Main.main.menuRecent1.setText(ProgramSettings.RECENT1.isEmpty() ? "..." : ProgramSettings.RECENT1);
        Main.main.menuRecent1.setEnabled(!ProgramSettings.RECENT1.isEmpty());
        Main.main.menuRecent2.setText(ProgramSettings.RECENT2.isEmpty() ? "..." : ProgramSettings.RECENT2);
        Main.main.menuRecent2.setEnabled(!ProgramSettings.RECENT2.isEmpty());
        Main.main.menuRecent3.setText(ProgramSettings.RECENT3.isEmpty() ? "..." : ProgramSettings.RECENT3);
        Main.main.menuRecent3.setEnabled(!ProgramSettings.RECENT3.isEmpty());
    }

    public static void validateRecent() {
        ArrayList<String> recents = new ArrayList<String>();
        File file = new File(RECENT1);
        if (!file.exists()) RECENT1 = "";
        file = new File(RECENT2);
        if (!file.exists()) RECENT2 = "";
        file = new File(RECENT3);
        if (!file.exists()) RECENT3 = "";
        if (!recents.contains(ProgramSettings.RECENT1)) recents.add(ProgramSettings.RECENT1);
        if (!recents.contains(ProgramSettings.RECENT2)) recents.add(ProgramSettings.RECENT2);
        if (!recents.contains(ProgramSettings.RECENT3)) recents.add(ProgramSettings.RECENT3);
        ProgramSettings.RECENT1 = recents.size() > 0 ? recents.get(0) : "";
        ProgramSettings.RECENT2 = recents.size() > 1 ? recents.get(1) : "";
        ProgramSettings.RECENT3 = recents.size() > 2 ? recents.get(2) : "";
        Main.main.menuRecent1.setText(ProgramSettings.RECENT1.isEmpty() ? "..." : ProgramSettings.RECENT1);
        Main.main.menuRecent1.setEnabled(!ProgramSettings.RECENT1.isEmpty());
        Main.main.menuRecent2.setText(ProgramSettings.RECENT2.isEmpty() ? "..." : ProgramSettings.RECENT2);
        Main.main.menuRecent2.setEnabled(!ProgramSettings.RECENT2.isEmpty());
        Main.main.menuRecent3.setText(ProgramSettings.RECENT3.isEmpty() ? "..." : ProgramSettings.RECENT3);
        Main.main.menuRecent3.setEnabled(!ProgramSettings.RECENT3.isEmpty());
    }
}
