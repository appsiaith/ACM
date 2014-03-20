package util;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 28/02/2014
 * Time: 09:36
 */
public class Menu {

    public static String MAIN_1 = ("Current Unit");
    public static String MAIN_SUB_1 = ("You are currently studying Unit U1");
    public static String MAIN_2 = ("Revision");
    public static String MAIN_SUB_2 = ("Practice what you have learned so far");
    public static String MAIN_3 = ("Dictionary");
    public static String MAIN_SUB_3 = ("All words used in the course");
    public static String MAIN_4 = ("Grammar");
    public static String MAIN_SUB_4 = ("Info about the Welsh language");
    public static String MAIN_5 = ("About");
    public static String MAIN_SUB_5 = ("This coure and learning Welsh");

    public static String UNIT_1 = ("Patrymau/Patterns");
    public static String UNIT_SUB_1 = ("Learn the patterns");
    public static String UNIT_2 = ("Geirfa/Vocabulary");
    public static String UNIT_SUB_2 = ("Practise the vocabulary");
    public static String UNIT_3 = ("Ymarferion/Exercises");
    public static String UNIT_SUB_3 = ("Try the exercises");
    public static String UNIT_4 = ("Gramadeg/Grammar");
    public static String UNIT_SUB_4 = ("Review the grammar");
    public static String UNIT_5 = ("Deialog/Dialogs");
    public static String UNIT_SUB_5 = ("Practice with longer pieces");

    public static boolean setField(String key, String value) {
        try {
            Field field = Menu.class.getDeclaredField(key);
            field.set(new Menu(), value);
            return true;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getField(String key) {
        try {
            Field field = Menu.class.getDeclaredField(key);
            field.setAccessible(true);
            return field.get(new Menu()) == null ? "" : field.get(new Menu()).toString();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
