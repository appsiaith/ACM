package database;

import model.*;
import util.*;
import view.Main;
import view.components.SwingDialog;

import javax.swing.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 01/11/2013
 * Time: 15:23
 */
public class Database {
    private Connection connection;
    //    private static Connection connection2;
    private HashMap<String, Vector<Integer>> deletions = new HashMap<String, Vector<Integer>>();
    private volatile static int id = -1;

    public synchronized static int next() {
        return --id;
    }

    public Database(String databasePath) throws SQLException {
        Config.DATABASE_FILE = databasePath;
        ProgramSettings.updateRecent(databasePath);
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (connection != null && !connection.isClosed()) connection.close();
        connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
//        connection2 = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
    }

    public void save(final Main main) throws SQLException {
        try {
            final Vector<String> missingFiles = SwingUtils.optimiseFileNames();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (!missingFiles.isEmpty()) SwingDialog.missingFileDialog(missingFiles);
                }
            });
            if (Config.isAutoClean()) clean(main.lessons);
            delete();
            saveConfig();
            saveMenu();
            saveAppearance();
            PreparedStatement insertGroup = connection.prepareStatement("insert into group_header (lessonId,south,north,english," +
                    "ordering,groupType,memberType) values (?,?,?,?,?,'about',?)");
            PreparedStatement updateGroup = connection.prepareStatement(
                    "update group_header set lessonId = ?, south = ?, north = ?, english = ?, ordering = ?," +
                            "memberType = ? where id = ?");
            PreparedStatement insertElement = prepareElementInsert("about", main.abouts.getInstance().getFieldNames());
            PreparedStatement updateElement = prepareElementUpdate("about", main.abouts.getInstance().getFieldNames());
            saveGroups(-1, main.abouts, insertGroup, updateGroup, insertElement, updateElement);

            save(main.lessons);
            insertGroup.close();
            updateGroup.close();
            insertElement.close();
            updateElement.close();
            ProgramSettings.updateRecent(Config.DATABASE_FILE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void createTables() throws SQLException, IllegalAccessException {
        PreparedStatement create;
        create = connection.prepareStatement("create table config (key TEXT PRIMARY KEY, defaultValue TEXT, customValue TEXT)");
        create.execute();
        createConfig();
        create = connection.prepareStatement("create table appearance (" +
                "id INTEGER PRIMARY KEY," +
                "tableName TEXT UNIQUE ON CONFLICT IGNORE," +
                "title TEXT," +
                "header TEXT," +
                "twoTexts TEXT," +
                "backImage TEXT," +
                "backColorRed INTEGER," +
                "backColorGreen INTEGER," +
                "backColorBlue INTEGER," +
                "backColorAlpha INTEGER," +
                "headerHeight INTEGER," +
                "headerImage TEXT," +
                "cellHeight INTEGER," +
                "cellColorRed INTEGER," +
                "cellColorGreen INTEGER," +
                "cellColorBlue INTEGER," +
                "cellColorAlpha INTEGER," +
                "mainFontSize INTEGER," +
                "mainFontColorRed INTEGER," +
                "mainFontColorGreen INTEGER," +
                "mainFontColorBlue INTEGER," +
                "mainFontColorAlpha INTEGER," +
                "subFontSize INTEGER," +
                "subFontColorRed INTEGER," +
                "subFontColorGreen INTEGER," +
                "subFontColorBlue INTEGER," +
                "subFontColorAlpha INTEGER," +
                "headerFontSize INTEGER," +
                "headerFontColorRed INTEGER," +
                "headerFontColorGreen INTEGER," +
                "headerFontColorBlue INTEGER," +
                "headerFontColorAlpha INTEGER," +
                "headerCellColorRed INTEGER," +
                "headerCellColorGreen INTEGER," +
                "headerCellColorBlue INTEGER," +
                "headerCellColorAlpha INTEGER)");
        create.execute();
        createAppearance();
        create = connection.prepareStatement("create table menu (key TEXT PRIMARY KEY, text TEXT, subText TEXT)");
        create.execute();
        createMenu();
        create = connection.prepareStatement("create table lesson (id INTEGER PRIMARY KEY, south TEXT, north TEXT, english TEXT, ordering INTEGER)");
        create.execute();
        create = connection.prepareStatement("create table pattern (id INTEGER PRIMARY KEY, groupId INTEGER, south TEXT, north TEXT, english TEXT, audioSouth TEXT, audioNorth TEXT, ordering INTEGER)");
        create.execute();
        create = connection.prepareStatement("create table vocabulary (id INTEGER PRIMARY KEY, groupId INTEGER, south TEXT, north TEXT, english TEXT, audioSouth TEXT, audioNorth TEXT, gender TEXT, wordClass TEXT, ordering INTEGER)");
        create.execute();
        create = connection.prepareStatement("create table dialog (id INTEGER PRIMARY KEY, groupId INTEGER, south TEXT, north TEXT, english TEXT, audioSouth TEXT, audioNorth TEXT, speaker TEXT, ordering INTEGER)");
        create.execute();
        create = connection.prepareStatement("create table grammar (id INTEGER PRIMARY KEY, groupId INTEGER, title TEXT, html TEXT, ordering INTEGER)");
        create.execute();
        create = connection.prepareStatement("create table about (id INTEGER PRIMARY KEY, groupId INTEGER, title TEXT, info TEXT,  url TEXT, linkType TEXT, ordering INTEGER)");
        create.execute();
        create = connection.prepareStatement("create table question (id INTEGER PRIMARY KEY, groupId INTEGER, title TEXT, picture TEXT, questionSouth TEXT, questionNorth TEXT, questionAudioSouth TEXT, questionAudioNorth TEXT, answerSouth TEXT, answerNorth TEXT, answerAudioSouth TEXT, answerAudioNorth TEXT, ordering INTEGER)");
        create.execute();
        create = connection.prepareStatement("create table group_header (id INTEGER PRIMARY KEY, lessonId INTEGER, south TEXT, north TEXT, english TEXT, groupType TEXT, memberType INTEGER, ordering INTEGER)");
        create.execute();
        create.close();
    }

    public void createAppearance() throws SQLException {
        newAppearanceInsert(Appearance.makeDefaultAppearance("MainMenu", "", ""));
        newAppearanceInsert(Appearance.makeDefaultAppearance("UnitChoice", "Uned U1", "Ex"));
        newAppearanceInsert(Appearance.makeDefaultAppearance("PatternHeader", "Patrymau", "Patrymau i'w hymarfer\n" + "Patterns to practise"));
        newAppearanceInsert(Appearance.makeDefaultAppearance("UnitVocab", "Geirfa", "Geirfa i'w hymarfer\n" + "Vocabulary to practise"));
        newAppearanceInsert(Appearance.makeDefaultAppearance("QuestionHeader", "Ymarferion", "Ymadroddion i'w hymarfer\n" + "Phrases to practise"));
        newAppearanceInsert(Appearance.makeDefaultAppearance("UnitGrammar", "Gramadeg", "Grammar Topics in Unit U1"));
        newAppearanceInsert(Appearance.makeDefaultAppearance("DialogHeader", "Examples", "Example texts in Unit U1"));
        newAppearanceInsert(Appearance.makeDefaultAppearance("DialogBody", "", ""));
        newAppearanceInsert(Appearance.makeDefaultAppearance("GlobalVocab", "Geirfa", "Searchable list of all vocabulary"));
        newAppearanceInsert(Appearance.makeDefaultAppearance("GlobalGrammar", "Gramadeg", "All Grammar"));
        newAppearanceInsert(Appearance.makeDefaultAppearance("About", "About", "Resources for learning Welsh"));
    }

    public void createConfig() throws SQLException, IllegalAccessException {
        PreparedStatement statement = connection.prepareStatement("insert into config (key, defaultValue) values (?,?)");
        Config config = new Config();
        Field[] fields = Config.class.getFields();
        for (int f = 0; f < fields.length; f++) {
            if (!fields[f].getName().contains("DATABASE"))
                insert(statement, fields[f].getName(), (String) fields[f].get(config));
        }
        statement.close();
    }

    public void createMenu() throws SQLException, IllegalAccessException {
        PreparedStatement statement = connection.prepareStatement("insert into menu (key, text, subText) values (?,?,?)");
        for (int i = 1; i <= 5; i++) {
            statement.setString(1, "MAIN_" + i);
            statement.setString(2, String.valueOf(Menu.getField("MAIN_" + i)));
            statement.setString(3, String.valueOf(Menu.getField("MAIN_SUB_" + i)));
            statement.executeUpdate();
            statement.setString(1, "UNIT_" + i);
            statement.setString(2, String.valueOf(Menu.getField("UNIT_" + i)));
            statement.setString(3, String.valueOf(Menu.getField("UNIT_SUB_" + i)));
            statement.executeUpdate();
        }
        statement.close();
    }

    private void insert(PreparedStatement statement, String key, String value) throws SQLException {
        statement.setString(1, key);
        statement.setString(2, value);
        statement.executeUpdate();
    }

    private void newAppearanceInsert(Appearance appearance) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("insert into appearance (" +
                "title, " +
                "header, " +
                "twoTexts, " +
                "backImage, " +
                "backColorRed, " +
                "backColorGreen, " +
                "backColorBlue, " +
                "backColorAlpha, " +
                "headerHeight, " +
                "headerImage, " +
                "cellHeight, " +
                "cellColorRed, " +
                "cellColorGreen, " +
                "cellColorBlue, " +
                "cellColorAlpha, " +
                "mainFontSize, " +
                "mainFontColorRed, " +
                "mainFontColorGreen, " +
                "mainFontColorBlue, " +
                "mainFontColorAlpha, " +
                "subFontSize, " +
                "subFontColorRed, " +
                "subFontColorGreen, " +
                "subFontColorBlue, " +
                "subFontColorAlpha, " +
                "headerFontSize, " +
                "headerFontColorRed, " +
                "headerFontColorGreen, " +
                "headerFontColorBlue, " +
                "headerFontColorAlpha, " +
                "headerCellColorRed, " +
                "headerCellColorGreen, " +
                "headerCellColorBlue, " +
                "headerCellColorAlpha, " +
                "tableName" +
                ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        statement.setString(1, appearance.title);
        statement.setString(2, appearance.header);
        statement.setString(3, appearance.twoTexts);
        statement.setString(4, appearance.backImage);
        statement.setInt(5, appearance.backColor.getRed());
        statement.setInt(6, appearance.backColor.getGreen());
        statement.setInt(7, appearance.backColor.getBlue());
        statement.setInt(8, appearance.backColor.getAlpha());
        statement.setInt(9, appearance.headerHeight);
        statement.setString(10, appearance.headerImage);
        statement.setInt(11, appearance.cellHeight);
        statement.setInt(12, appearance.cellColor.getRed());
        statement.setInt(13, appearance.cellColor.getGreen());
        statement.setInt(14, appearance.cellColor.getBlue());
        statement.setInt(15, appearance.cellColor.getAlpha());
        statement.setInt(16, appearance.mainFontSize);
        statement.setInt(17, appearance.mainFontColor.getRed());
        statement.setInt(18, appearance.mainFontColor.getGreen());
        statement.setInt(19, appearance.mainFontColor.getBlue());
        statement.setInt(20, appearance.mainFontColor.getAlpha());
        statement.setInt(21, appearance.subFontSize);
        statement.setInt(22, appearance.subFontColor.getRed());
        statement.setInt(23, appearance.subFontColor.getGreen());
        statement.setInt(24, appearance.subFontColor.getBlue());
        statement.setInt(25, appearance.subFontColor.getAlpha());
        statement.setInt(26, appearance.headerFontSize);
        statement.setInt(27, appearance.headerFontColor.getRed());
        statement.setInt(28, appearance.headerFontColor.getGreen());
        statement.setInt(29, appearance.headerFontColor.getBlue());
        statement.setInt(30, appearance.headerFontColor.getAlpha());
        statement.setInt(31, appearance.headerCellColor.getRed());
        statement.setInt(32, appearance.headerCellColor.getGreen());
        statement.setInt(33, appearance.headerCellColor.getBlue());
        statement.setInt(34, appearance.headerCellColor.getAlpha());
        statement.setString(35, appearance.tableName);
        statement.executeUpdate();
    }

    public void loadConfig() throws SQLException {
        PreparedStatement statement;
        statement = connection.prepareStatement("select * from config");
        ResultSet result = statement.executeQuery();
        Config config = new Config();
        while (result.next()) {
            try {
                Field field = Config.class.getDeclaredField(result.getString("key"));
                if (!field.getName().contains("DATABASE")) {
                    if (result.getString("customValue") == null || result.getString("customValue").equals(""))
                        field.set(config, result.getString("defaultValue"));
                    else field.set(config, result.getString("customValue"));
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        result.close();
        statement.close();
    }

    public void loadMenu() throws SQLException {
        PreparedStatement statement;
        statement = connection.prepareStatement("select * from menu");
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            String key = result.getString("key");
            Menu.setField(key, result.getString("text"));
            Menu.setField(key.split("_")[0] + "_" + "SUB" + "_" + key.split("_")[1], result.getString("subText"));
        }
        result.close();
        statement.close();
    }

    public void loadAppearance() throws SQLException {
        PreparedStatement statement;
        statement = connection.prepareStatement("select * from appearance");
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            Appearance.appearances.put(result.getString("tableName"), new Appearance(
                    result.getString("tableName"),
                    result.getString("title"),
                    result.getString("header"),
                    result.getString("twoTexts"),
                    result.getString("backImage"),
                    result.getInt("backColorRed"),
                    result.getInt("backColorGreen"),
                    result.getInt("backColorBlue"),
                    result.getInt("backColorAlpha"),
                    result.getInt("headerHeight"),
                    result.getString("headerImage"),
                    result.getInt("cellHeight"),
                    result.getInt("cellColorRed"),
                    result.getInt("cellColorGreen"),
                    result.getInt("cellColorBlue"),
                    result.getInt("cellColorAlpha"),
                    result.getInt("mainFontSize"),
                    result.getInt("mainFontColorRed"),
                    result.getInt("mainFontColorGreen"),
                    result.getInt("mainFontColorBlue"),
                    result.getInt("mainFontColorAlpha"),
                    result.getInt("subFontSize"),
                    result.getInt("subFontColorRed"),
                    result.getInt("subFontColorGreen"),
                    result.getInt("subFontColorBlue"),
                    result.getInt("subFontColorAlpha"),
                    result.getInt("headerFontSize"),
                    result.getInt("headerFontColorRed"),
                    result.getInt("headerFontColorGreen"),
                    result.getInt("headerFontColorBlue"),
                    result.getInt("headerFontColorAlpha"),
                    result.getInt("headerCellColorRed"),
                    result.getInt("headerCellColorGreen"),
                    result.getInt("headerCellColorBlue"),
                    result.getInt("headerCellColorAlpha")
            ));
        }
        result.close();
        statement.close();
    }

    public void scheduleDeletion(ElementGroup group) {
//        if (group.getId() < 0) {
//            Logging.info("Group Deletion NOT Scheduled, Not a Persisted Object");
//            return true;
//        }
//        boolean success = true;
        for (int j = 0; j < group.size(); j++) scheduleDeletion((Element) group.get(j));
//        try {
//            String type = ((Element) group.getElementClass().newInstance()).getName();
        if (!deletions.containsKey("group_header")) deletions.put("group_header", new Vector<Integer>());
        deletions.get("group_header").add(group.getId());
//            Logging.info("Deletion Scheduled for group id " + group.getId() + " of type " + type);
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }

    public void scheduleDeletion(Element element) {
        /*if (element.getId() < 0) {
            Logging.info("Element " + element.getName() + " Deletion NOT Scheduled, Not a Persisted Object");
        }*/
        if (!deletions.containsKey(element.getName())) deletions.put(element.getName(), new Vector<Integer>());
        deletions.get(element.getName()).add(element.getId());
//        Logging.info("Deletion Scheduled for " + pair.tableName + " id " + pair.id);
//        return false;
    }

    public void scheduleDeletion(Lesson lesson) {
        /*if (lesson.getId() < 0) {
            Logging.info("Element " + lesson.getName() + " Deletion NOT Scheduled, Not a Persisted Object");
            return true;
        }*/
        boolean success = true;
        for (int i = 0; i < lesson.getDialogues().size(); i++)
            scheduleDeletion(lesson.getDialogues().get(i));
        for (int i = 0; i < lesson.getQuestions().size(); i++)
            scheduleDeletion(lesson.getQuestions().get(i));
        for (int i = 0; i < lesson.getVocabularies().size(); i++)
            scheduleDeletion(lesson.getVocabularies().get(i));
        for (int i = 0; i < lesson.getPatterns().size(); i++)
            scheduleDeletion(lesson.getPatterns().get(i));
        for (int i = 0; i < lesson.getGrammars().size(); i++)
            scheduleDeletion(lesson.getGrammars().get(i));
        if (!deletions.containsKey("lesson")) deletions.put("lesson", new Vector<Integer>());
        deletions.get("lesson").add(lesson.getId());
        Logging.info("Deletion Scheduled for lesson id " + lesson.getId());
//        return success;
    }

    public void delete() throws SQLException {
        int count = 0;
        Set<String> keySet = deletions.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            PreparedStatement statement = connection.prepareStatement("delete from " + key + " where id = ?");
            Vector<Integer> ids = deletions.get(key);
            for (int i = 0; i < ids.size(); i++) {
                statement.setInt(1, ids.get(i));
                statement.executeUpdate();
                count++;
            }
            deletions.get(key).clear();
            statement.close();
        }
        /*for (TableIdPair pair : deletions) {
            PreparedStatement statement = connection.prepareStatement("delete from " + pair.tableName + " where id = ?");
            statement.setInt(1, pair.id);
            statement.executeUpdate();
            count++;
            statement.close();
        }
        deletions.clear();*/
        Logging.info("Deletion finished with " + count + " executed queries.");
    }

    public Vector<Lesson> save(Vector<Lesson> lessons) throws SQLException {
        PreparedStatement insert = connection.prepareStatement("insert into lesson (south,north,english,ordering) values(?,?,?,?)");
        PreparedStatement update = connection.prepareStatement("update lesson set south = ?, north = ?, english = ?, ordering = ? where id = ?");
        PreparedStatement updateGroup = connection.prepareStatement(
                "update group_header set lessonId = ?, south = ?, north = ?, english = ?, ordering = ?," +
                        "memberType = ? where id = ?");
        PreparedStatement[] insertGroups = new PreparedStatement[5];
        PreparedStatement[] insertElements = new PreparedStatement[5];
        PreparedStatement[] updateElements = new PreparedStatement[5];
        for (int i = 0; i < Element.ELEMENT_INSTANCES.length; i++) {
            insertGroups[i] = connection.prepareStatement("insert into group_header (lessonId,south,north,english," +
                    "ordering,groupType,memberType) values (?,?,?,?,?,'" + Element.ELEMENT_TEXTS[i] + "',?)");
            insertElements[i] = prepareElementInsert(Element.ELEMENT_TEXTS[i], Element.ELEMENT_INSTANCES[i].getFieldNames());
            updateElements[i] = prepareElementUpdate(Element.ELEMENT_TEXTS[i], Element.ELEMENT_INSTANCES[i].getFieldNames());
        }
        for (int l = 0; l < lessons.size(); l++) {
            Lesson lesson = lessons.get(l);
            if (lesson.getId() < 0) {
                insert.setString(1, lesson.getSouth());
                insert.setString(2, lesson.getNorth());
                insert.setString(3, lesson.getEnglish());
                insert.setInt(4, l);
                insert.executeUpdate();
                ResultSet resultSet = insert.getGeneratedKeys();
                if (resultSet.next()) lesson.setId(resultSet.getInt(1));
                resultSet.close();
            } else {
                update.setString(1, lesson.getSouth());
                update.setString(2, lesson.getNorth());
                update.setString(3, lesson.getEnglish());
                update.setInt(4, l);
                update.setInt(5, lesson.getId());
                update.executeUpdate();
            }
            for (int i = 0; i < Element.ELEMENT_INSTANCES.length; i++)
                saveGroups(lesson.getId(), lesson.getElementGroups(Element.ELEMENT_TYPES[i]), insertGroups[i], updateGroup, insertElements[i], updateElements[i]);
        }
        insert.close();
        update.close();
        updateGroup.close();
        for (int i = 0; i < Element.ELEMENT_INSTANCES.length; i++) {
            insertGroups[i].close();
            insertElements[i].close();
            updateElements[i].close();
        }
        return lessons;
    }

    public void saveAppearance() throws SQLException {
        PreparedStatement update = connection.prepareStatement("update appearance set " +
                "title = ?, " +
                "header = ?, " +
                "twoTexts = ?, " +
                "backImage = ?, " +
                "backColorRed = ?, " +
                "backColorGreen = ?, " +
                "backColorBlue = ?, " +
                "backColorAlpha = ?, " +
                "headerHeight = ?, " +
                "headerImage = ?, " +
                "cellHeight = ?, " +
                "cellColorRed = ?, " +
                "cellColorGreen = ?, " +
                "cellColorBlue = ?, " +
                "cellColorAlpha = ?, " +
                "mainFontSize = ?, " +
                "mainFontColorRed = ?, " +
                "mainFontColorGreen = ?, " +
                "mainFontColorBlue = ?, " +
                "mainFontColorAlpha = ?, " +
                "subFontSize = ?, " +
                "subFontColorRed = ?, " +
                "subFontColorGreen = ?, " +
                "subFontColorBlue = ?, " +
                "subFontColorAlpha = ?, " +
                "headerFontSize = ?, " +
                "headerFontColorRed = ?, " +
                "headerFontColorGreen = ?, " +
                "headerFontColorBlue = ?, " +
                "headerFontColorAlpha = ?, " +
                "headerCellColorRed = ?, " +
                "headerCellColorGreen = ?, " +
                "headerCellColorBlue = ?, " +
                "headerCellColorAlpha = ?" +
                "where tableName = ?");
        Iterator<String> iterator = Appearance.appearances.keySet().iterator();
        while (iterator.hasNext()) {
            String tableName = iterator.next();
            Appearance appearance = Appearance.appearances.get(tableName);
            update.setString(1, appearance.title);
            update.setString(2, appearance.header);
            update.setString(3, appearance.twoTexts);
            update.setString(4, appearance.backImage);
            update.setInt(5, appearance.backColor.getRed());
            update.setInt(6, appearance.backColor.getGreen());
            update.setInt(7, appearance.backColor.getBlue());
            update.setInt(8, appearance.backColor.getAlpha());
            update.setInt(9, appearance.headerHeight);
            update.setString(10, appearance.headerImage);
            update.setInt(11, appearance.cellHeight);
            update.setInt(12, appearance.cellColor.getRed());
            update.setInt(13, appearance.cellColor.getGreen());
            update.setInt(14, appearance.cellColor.getBlue());
            update.setInt(15, appearance.cellColor.getAlpha());
            update.setInt(16, appearance.mainFontSize);
            update.setInt(17, appearance.mainFontColor.getRed());
            update.setInt(18, appearance.mainFontColor.getGreen());
            update.setInt(19, appearance.mainFontColor.getBlue());
            update.setInt(20, appearance.mainFontColor.getAlpha());
            update.setInt(21, appearance.subFontSize);
            update.setInt(22, appearance.subFontColor.getRed());
            update.setInt(23, appearance.subFontColor.getGreen());
            update.setInt(24, appearance.subFontColor.getBlue());
            update.setInt(25, appearance.subFontColor.getAlpha());
            update.setInt(26, appearance.headerFontSize);
            update.setInt(27, appearance.headerFontColor.getRed());
            update.setInt(28, appearance.headerFontColor.getGreen());
            update.setInt(29, appearance.headerFontColor.getBlue());
            update.setInt(30, appearance.headerFontColor.getAlpha());
            update.setInt(31, appearance.headerCellColor.getRed());
            update.setInt(32, appearance.headerCellColor.getGreen());
            update.setInt(33, appearance.headerCellColor.getBlue());
            update.setInt(34, appearance.headerCellColor.getAlpha());
            update.setString(35, appearance.tableName);
            update.executeUpdate();
        }
        update.close();
    }

    public void saveConfig() throws SQLException, IllegalAccessException {
        PreparedStatement check = connection.prepareStatement("select * from config where key = ?");
        PreparedStatement update = connection.prepareStatement("update config set customValue = ? where key = ?");
        PreparedStatement insert = connection.prepareStatement("insert into config (key, defaultValue) values (?,?)");
        Config config = new Config();
        Field[] fields = Config.class.getFields();
        for (int f = 0; f < fields.length; f++) {
            if (!fields[f].getName().contains("DATABASE")) {
                check.setString(1, fields[f].getName());
                ResultSet resultSet = check.executeQuery();
                if (resultSet.next()) {
                    update.setString(1, (String) fields[f].get(config));
                    update.setString(2, fields[f].getName());
                    update.executeUpdate();
                    update.clearParameters();
                } else {
                    insert.setString(2, (String) fields[f].get(config));
                    insert.setString(1, fields[f].getName());
                    insert.executeUpdate();
                    insert.clearParameters();
                }
                resultSet.close();
            }
        }
        update.close();
        check.close();
        insert.close();
    }

    public void saveMenu() throws SQLException, IllegalAccessException {
        PreparedStatement update = connection.prepareStatement("update menu set text = ?, subText = ? where key = ?");
        for (int i = 1; i <= 5; i++) {
            update.setString(1, Menu.getField("MAIN_" + i));
            update.setString(2, Menu.getField("MAIN_SUB_" + i));
            update.setString(3, ("MAIN_" + i));
            update.executeUpdate();
            update.clearParameters();
            update.setString(1, Menu.getField("UNIT_" + i));
            update.setString(2, Menu.getField("UNIT_SUB_" + i));
            update.setString(3, ("UNIT_" + i));
            update.executeUpdate();
        }
        update.close();
    }

    public void saveGroups(int lessonId, ElementGroups groups, PreparedStatement insert, PreparedStatement update,
                           PreparedStatement insertElement, PreparedStatement updateElement) throws SQLException {
        if (groups.hashCode() == groups.getInitialHashCode()) return;
        Logging.info("Saving lesson id " + lessonId + ", " + groups.getElementClass() + " groups.");
        for (int g = 0; g < groups.size(); g++) {
            ElementGroup<Element> group = groups.get(g);
            if (group.getId() < 0) {
                insert.setInt(1, lessonId);
                insert.setString(2, group.getSouth());
                insert.setString(3, group.getNorth());
                insert.setString(4, group.getEnglish());
                insert.setInt(5, g);
                insert.setInt(6, group.getMemberType());
                insert.executeUpdate();
                ResultSet resultSet = insert.getGeneratedKeys();
                if (resultSet.next()) group.setId(resultSet.getInt(1));
                resultSet.close();
                saveElements(group, insertElement, updateElement);
            } else {
                update.setInt(1, lessonId);
                update.setString(2, group.getSouth());
                update.setString(3, group.getNorth());
                update.setString(4, group.getEnglish());
                update.setInt(5, g);
                update.setInt(6, group.getMemberType());
                update.setInt(7, group.getId());
                update.executeUpdate();
                saveElements(group, insertElement, updateElement);
            }
        }
    }

    public void saveElements(ElementGroup group, PreparedStatement insertElement, PreparedStatement updateElement) throws SQLException {
        Element element;
        for (int e = 0; e < group.size(); e++) {
            element = (Element) group.get(e);
            if (element.getId() < 0) {
                for (int f = 0; f < element.getFieldNames().length; f++)
                    insertElement.setString(f + 1, element.getField(element.getFieldNames()[f]));
                insertElement.setInt(element.getFieldNames().length + 1, e);
                insertElement.setInt(element.getFieldNames().length + 2, group.getId());
                insertElement.executeUpdate();
                ResultSet resultSet = insertElement.getGeneratedKeys();
                if (resultSet.next()) element.setId(resultSet.getInt(1));
                resultSet.close();
            } else {
                for (int f = 0; f < element.getFieldNames().length; f++)
                    updateElement.setString(f + 1, element.getField(element.getFieldNames()[f]));
                updateElement.setInt(element.getFieldNames().length + 1, e);
                updateElement.setInt(element.getFieldNames().length + 2, group.getId());
                updateElement.setInt(element.getFieldNames().length + 3, element.getId());
                updateElement.executeUpdate();
            }
        }
    }

    private PreparedStatement prepareElementInsert(String tableName, String[] fieldNames) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("insert into ").append(tableName).append(" (");
        for (String f : fieldNames) buffer.append(f).append(", ");
        buffer.append("ordering,").append("groupId").append(") values (");
        for (int f = 0; f < fieldNames.length; f++) buffer.append("?, ");
        buffer.append("?, ").append("?").append(")");
        return connection.prepareStatement(buffer.toString());
    }

    private PreparedStatement prepareElementUpdate(String tableName, String[] fieldNames) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("update ").append(tableName).append(" set ");
        for (String f : fieldNames) buffer.append(f).append(" = ?, ");
        buffer.append("ordering = ?, ").append("groupId = ?").append(" where id = ?");
        return connection.prepareStatement(buffer.toString());
    }

    public Vector<Lesson> load() throws SQLException {

        loadConfig();
        loadMenu();
        loadAppearance();

        loadAbouts();

        final Vector<Lesson> lessons = new Vector<Lesson>();
        PreparedStatement statement = connection.prepareStatement("select * from lesson order by ordering");
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            Lesson lesson = new Lesson(result.getInt("id"));
            for (int f = 0; f < lesson.getFieldNames().length; f++)
                lesson.setField(lesson.getFieldNames()[f], result.getString(lesson.getFieldNames()[f]) == null ? "" : result.getString(lesson.getFieldNames()[f]));
            lessons.add(lesson);
        }
        result.close();
        statement.close();

        PreparedStatement[] outers = new PreparedStatement[5];
        PreparedStatement[] inners = new PreparedStatement[5];
        for (int t = 0; t < Element.ELEMENT_TEXTS.length; t++) {
            outers[t] = connection.prepareStatement("select id, memberType, south, north, english from group_header where lessonId = ? and groupType = '" + Element.ELEMENT_TEXTS[t] + "' order by ordering");
            inners[t] = connection.prepareStatement("select * from " + Element.ELEMENT_TEXTS[t] + " where groupId = ? order by ordering");
        }
        for (int l = 0; l < lessons.size(); l++) {
            try {
                for (int t = 0; t < Element.ELEMENT_TEXTS.length; t++)
                    loadGroups(lessons.get(l), Element.ELEMENT_CLASSES[t], Element.ELEMENT_TYPES[t], outers[t], inners[t]);
                lessons.get(l).fill();
                for (int t = 0; t < Element.ELEMENT_TYPES.length; t++)
                    lessons.get(l).getElementGroups(Element.ELEMENT_TYPES[t]).setInitialHashCode(
                            lessons.get(l).getElementGroups(Element.ELEMENT_TYPES[t]).hashCode());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        for (int t = 0; t < Element.ELEMENT_TEXTS.length; t++) {
            outers[t].close();
            inners[t].close();
        }

        System.gc();
        return lessons;
    }

    private void loadAbouts() throws SQLException {
        Main.main.abouts = new ElementGroups<About>(About.class);
        PreparedStatement statement = connection.prepareStatement("select * from group_header where lessonId = ? and groupType = '" + Element.TEXT_ABOUT + "' order by ordering");
        statement.setInt(1, -1);
        ResultSet result = statement.executeQuery();
        PreparedStatement innerStatement = connection.prepareStatement("select * from about where groupId = ? order by ordering");
        while (result.next()) {
            ElementGroup<About> group = new ElementGroup<About>(About.class, result.getInt("id"), result.getInt("memberType"), result.getString("south"), result.getString("north"), result.getString("english"));
            innerStatement.setInt(1, group.getId());
            ResultSet innerResult = innerStatement.executeQuery();
            while (innerResult.next()) {
                About about = new About(innerResult.getInt("id"));
                for (int f = 0; f < about.getFieldNames().length; f++)
                    about.setField(about.getFieldNames()[f], innerResult.getString(about.getFieldNames()[f]) == null ? "" : innerResult.getString(about.getFieldNames()[f]));
                group.add(about);
            }
            innerResult.close();
            Main.main.abouts.add(group);
        }
        innerStatement.close();
        Main.main.abouts.fill();
        Main.main.abouts.setInitialHashCode(Main.main.abouts.hashCode());
        result.close();
        statement.close();
    }

    public Lesson loadGroups(Lesson lesson, Class elementClass, int elementType, PreparedStatement outer, PreparedStatement inner) throws SQLException, IllegalAccessException, InstantiationException {
        outer.setInt(1, lesson.getId());
        ResultSet result = outer.executeQuery();
        while (result.next()) {
            ElementGroup<Element> group = new ElementGroup<Element>(elementClass, result.getInt("id"), result.getInt("memberType"), result.getString("south"), result.getString("north"), result.getString("english"));
            inner.setInt(1, group.getId());
            ResultSet innerResult = inner.executeQuery();
            while (innerResult.next()) {
                Element element = (Element) elementClass.newInstance();
                element.setId(innerResult.getInt("id"));
                for (int f = 0; f < element.getFieldNames().length; f++)
                    element.setField(element.getFieldNames()[f], innerResult.getString(element.getFieldNames()[f]) == null ? "" : innerResult.getString(element.getFieldNames()[f]));
                group.add(element);
            }
            innerResult.close();
            lesson.getElementGroups(elementType).add(group);
        }
        result.close();
        return lesson;
    }

    public void closeConnection() {
        try {
            connection.close();
            Logging.info("Database connection closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clean(Vector<Lesson> lessons) {
        String autoAppend = Config.AUTO_APPEND;
        Config.AUTO_APPEND = "NO";
        Iterator<Lesson> iteratorLesson = lessons.iterator();
        while (iteratorLesson.hasNext()) {
            Lesson lesson = iteratorLesson.next();
            Iterator<ElementGroup<Dialogue>> dialogIterator = lesson.getDialogues().iterator();
            while (dialogIterator.hasNext()) {
                ElementGroup<Dialogue> group = dialogIterator.next();
                Iterator<Dialogue> iterator = group.iterator();
                while (iterator.hasNext()) {
                    Dialogue current = iterator.next();
                    if (current.isRaw()) {
                        Main.main.database.scheduleDeletion(current);
                        iterator.remove();
                    }
                }
                if (group.isRaw()) {
                    Main.main.database.scheduleDeletion(group);
                    dialogIterator.remove();
                }
            }
            Iterator<ElementGroup<Question>> questionIterator = lesson.getQuestions().iterator();
            while (questionIterator.hasNext()) {
                ElementGroup<Question> group = questionIterator.next();
                Iterator<Question> iterator = group.iterator();
                while (iterator.hasNext()) {
                    Question current = iterator.next();
                    if (current.isRaw()) {
                        Main.main.database.scheduleDeletion(current);
                        iterator.remove();
                    }
                }
                if (group.isRaw()) {
                    Main.main.database.scheduleDeletion(group);
                    questionIterator.remove();
                }
            }
            Iterator<ElementGroup<Pattern>> patternIterator = lesson.getPatterns().iterator();
            while (patternIterator.hasNext()) {
                ElementGroup<Pattern> group = patternIterator.next();
                Iterator<Pattern> iterator = group.iterator();
                while (iterator.hasNext()) {
                    Pattern current = iterator.next();
                    if (current.isRaw()) {
                        Main.main.database.scheduleDeletion(current);
                        iterator.remove();
                    }
                }
                if (group.isRaw()) {
                    Main.main.database.scheduleDeletion(group);
                    patternIterator.remove();
                }
            }
            Iterator<ElementGroup<Grammar>> grammarIterator = lesson.getGrammars().iterator();
            while (grammarIterator.hasNext()) {
                ElementGroup<Grammar> group = grammarIterator.next();
                Iterator<Grammar> iterator = group.iterator();
                while (iterator.hasNext()) {
                    Grammar current = iterator.next();
                    if (current.isRaw()) {
                        Main.main.database.scheduleDeletion(current);
                        iterator.remove();
                    }
                }
                if (group.isRaw()) {
                    Main.main.database.scheduleDeletion(group);
                    grammarIterator.remove();
                }
            }
            Iterator<ElementGroup<Vocabulary>> vocabularyIterator = lesson.getVocabularies().iterator();
            while (vocabularyIterator.hasNext()) {
                ElementGroup<Vocabulary> group = vocabularyIterator.next();
                Iterator<Vocabulary> iterator = group.iterator();
                while (iterator.hasNext()) {
                    Vocabulary current = iterator.next();
                    if (current.isRaw()) {
                        Main.main.database.scheduleDeletion(current);
                        iterator.remove();
                    }
                }
                if (group.isRaw()) {
                    Main.main.database.scheduleDeletion(group);
                    vocabularyIterator.remove();
                }
            }

            if (lesson.isRaw()) {
                Main.main.database.scheduleDeletion(lesson);
                iteratorLesson.remove();
            }
        }
        Iterator<ElementGroup<About>> vocabularyIterator = Main.main.abouts.iterator();
        while (vocabularyIterator.hasNext()) {
            ElementGroup<About> group = vocabularyIterator.next();
            Iterator<About> iterator = group.iterator();
            while (iterator.hasNext()) {
                About current = iterator.next();
                if (current.isRaw()) {
                    Main.main.database.scheduleDeletion(current);
                    iterator.remove();
                }
            }
            if (group.isRaw()) {
                Main.main.database.scheduleDeletion(group);
                vocabularyIterator.remove();
            }
        }
        Config.AUTO_APPEND = autoAppend;
    }

}
