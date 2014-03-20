package util;

import java.awt.*;
import java.util.Hashtable;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 29/01/2014
 * Time: 13:44
 */
public class Appearance {

    public static TreeMap<String, Appearance> appearances = new TreeMap<String, Appearance>();

    public String tableName;
    public String title;
    public String header;

    public String twoTexts;
    public String backImage;
    public String headerImage;

    public int headerHeight;
    public int cellHeight;
    public int mainFontSize;
    public int subFontSize;
    public int headerFontSize;

    public Color backColor;
    public Color cellColor;
    public Color mainFontColor;
    public Color subFontColor;
    public Color headerFontColor;
    public Color headerCellColor;

    public Appearance(String tableName) {
        this.tableName = tableName;
    }

    public static Appearance makeDefaultAppearance(String tableName, String title, String header) {
        Appearance appearance = new Appearance(tableName);
        appearance.title = title;
        appearance.header = header;

        appearance.twoTexts = "YES";
        appearance.backImage = "";
        appearance.headerImage = "";

        appearance.headerHeight = 120;
        appearance.cellHeight = 60;
        appearance.mainFontSize = 24;
        appearance.subFontSize = 12;
        appearance.headerFontSize = 24;

        appearance.backColor = Color.white;
        appearance.cellColor = Color.lightGray;
        appearance.mainFontColor = Color.black;
        appearance.subFontColor = Color.darkGray;
        appearance.headerFontColor = Color.black;
        appearance.headerCellColor = Color.lightGray;
        return appearance;
    }

    public int getSize(String key) {
        if (key.equals("headerHeight")) return headerHeight;
        else if (key.equals("cell")) return cellHeight;
        else if (key.equals("mainFont")) return mainFontSize;
        else if (key.equals("subFont")) return subFontSize;
        else if (key.equals("headerFont")) return headerFontSize;
        return -1;
    }

    public void setSize(String key, int size) {
        if (key.equals("headerHeight")) headerHeight = size;
        else if (key.equals("cell")) cellHeight = size;
        else if (key.equals("mainFont")) mainFontSize = size;
        else if (key.equals("subFont")) subFontSize = size;
        else if (key.equals("headerFont")) headerFontSize = size;
    }

    public String getString(String key) {
        if (key.equals("title")) return title;
        else if (key.equals("header")) return header;
        else if (key.equals("twoTexts")) return twoTexts;
        return null;
    }

    public void setString(String key, String string) {
        if (key.equals("title")) title = string;
        else if (key.equals("header")) header = string;
        else if (key.equals("twoTexts")) twoTexts = string;
    }

    public String getImageFile(String key) {
        if (key.equals("back")) return backImage;
        else if (key.equals("header")) return headerImage;
        return null;
    }

    public void setImageFile(String key, String imageFile) {
        if (key.equals("back")) backImage = imageFile;
        else if (key.equals("header")) headerImage = imageFile;
    }

    public Color getColor(String key) {
        if (key.equals("back")) return backColor;
        else if (key.equals("cell")) return cellColor;
        else if (key.equals("mainFont")) return mainFontColor;
        else if (key.equals("subFont")) return subFontColor;
        else if (key.equals("headerFont")) return headerFontColor;
        else if (key.equals("headerCell")) return headerCellColor;
        return null;
    }

    public void setColor(String key, Color color) {
        if (key.equals("back")) backColor = color;
        else if (key.equals("cell")) cellColor = color;
        else if (key.equals("mainFont")) mainFontColor = color;
        else if (key.equals("subFont")) subFontColor = color;
        else if (key.equals("headerFont")) headerFontColor = color;
        else if (key.equals("headerCell")) headerCellColor = color;
    }

    public Appearance(String tableName, String title, String header, String twoTexts, String backImage, int backColorRed, int backColorGreen, int backColorBlue, int backColorAlpha, int headerHeight, String headerImage, int cellHeight, int cellColorRed, int cellColorGreen, int cellColorBlue, int cellColorAlpha, int mainFontSize, int mainFontColorRed, int mainFontColorGreen, int mainFontColorBlue, int mainFontColorAlpha, int subFontSize, int subFontColorRed, int subFontColorGreen, int subFontColorBlue, int subFontColorAlpha, int headerFontSize, int headerFontColorRed, int headerFontColorGreen, int headerFontColorBlue, int headerFontColorAlpha, int headerCellColorRed, int headerCellColorGreen, int headerCellColorBlue, int headerCellColorAlpha) {
        this.tableName = tableName;
        this.title = title;
        this.header = header;
        this.twoTexts = twoTexts;
        this.backImage = backImage;
        this.backColor = new Color(backColorRed, backColorGreen, backColorBlue, backColorAlpha);
        this.headerHeight = headerHeight;
        this.headerImage = headerImage;
        this.cellHeight = cellHeight;
        this.cellColor = new Color(cellColorRed, cellColorGreen, cellColorBlue, cellColorAlpha);
        this.mainFontSize = mainFontSize;
        this.mainFontColor = new Color(mainFontColorRed, mainFontColorGreen, mainFontColorBlue, mainFontColorAlpha);
        this.subFontSize = subFontSize;
        this.subFontColor = new Color(subFontColorRed, subFontColorGreen, subFontColorBlue, subFontColorAlpha);
        this.headerFontSize = headerFontSize;
        this.headerFontColor = new Color(headerFontColorRed, headerFontColorGreen, headerFontColorBlue, headerFontColorAlpha);
        this.headerCellColor = new Color(headerCellColorRed, headerCellColorGreen, headerCellColorBlue, headerCellColorAlpha);
    }

    public void copyStyleFrom(Appearance sourceAppearance) {
        twoTexts = sourceAppearance.twoTexts;
        backImage = sourceAppearance.backImage;
        headerImage = sourceAppearance.headerImage;
        headerHeight = sourceAppearance.headerHeight;
        cellHeight = sourceAppearance.cellHeight;
        mainFontSize = sourceAppearance.mainFontSize;
        subFontSize = sourceAppearance.subFontSize;
        headerFontSize = sourceAppearance.headerFontSize;
        backColor = sourceAppearance.backColor;
        cellColor = sourceAppearance.cellColor;
        mainFontColor = sourceAppearance.mainFontColor;
        subFontColor = sourceAppearance.subFontColor;
        headerFontColor = sourceAppearance.headerFontColor;
        headerCellColor = sourceAppearance.headerCellColor;
    }
}
