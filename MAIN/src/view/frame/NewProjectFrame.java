package view.frame;

import database.Database;
import model.Lesson;
import util.Config;
import util.SwingUtils;
import view.Main;
import view.components.ImageButton;
import view.components.SwingDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 07/02/2014
 * Time: 12:46
 */
public class NewProjectFrame extends MyFrame {

    private JLabel projectNameLabel = new JLabel("Project Name:");
    private JTextField projectNameField = new JTextField();
    private JLabel projectLocationLabel = new JLabel("Project Directory:");
    private JTextField projectLocationField = new JTextField();
    private ImageButton projectLocationButton;
    private JLabel imageLocationLabel = new JLabel("Image Location:");
    private JLabel imageLocationLabelFront = new JLabel("Project Directory /");
    private JTextField imageLocationField = new JTextField("image");
    private JLabel audioLocationLabel = new JLabel("Audio Location:");
    private JLabel audioLocationLabelFront = new JLabel("Project Directory /");
    private JTextField audioLocationField = new JTextField("audio");
    private JLabel htmlLocationLabel = new JLabel("HTML Location:");
    private JLabel htmlLocationLabelFront = new JLabel("Project Directory /");
    private JTextField htmlLocationField = new JTextField("html");

    private JLabel ABOUT_NAME_LABEL = new JLabel("About");
    private JLabel LESSON_NAME_LABEL = new JLabel("Unit");
    private JLabel DIALOG_NAME_LABEL = new JLabel("Dialogue");
    private JLabel GRAMMAR_NAME_LABEL = new JLabel("Grammar");
    private JLabel QUESTION_NAME_LABEL = new JLabel("Question");
    private JLabel PATTERN_NAME_LABEL = new JLabel("Pattern");
    private JLabel VOCABULARY_NAME_LABEL = new JLabel("Vocabulary");
    private JLabel TEXT_NAME_LABEL = new JLabel("South Wales");
    private JLabel TEXT_ALT_NAME_LABEL = new JLabel("North Wales");
    private JLabel TRANSLATION_NAME_LABEL = new JLabel("English");

    private JLabel AUTO_APPEND_LABEL = new JLabel("Always Append Empty Records to the End of Group");
    private JLabel CLEAN_BEFORE_SAVE_LABEL = new JLabel("Always Clean Database before Saving");

    private JTextField ABOUT_NAME_FIELD = new JTextField(Config.ABOUT_NAME);
    private JTextField LESSON_NAME_FIELD = new JTextField(Config.LESSON_NAME);
    private JTextField DIALOG_NAME_FIELD = new JTextField(Config.DIALOG_NAME);
    private JTextField GRAMMAR_NAME_FIELD = new JTextField(Config.GRAMMAR_NAME);
    private JTextField QUESTION_NAME_FIELD = new JTextField(Config.QUESTION_NAME);
    private JTextField PATTERN_NAME_FIELD = new JTextField(Config.PATTERN_NAME);
    private JTextField VOCABULARY_NAME_FIELD = new JTextField(Config.VOCABULARY_NAME);
    private JTextField TEXT_NAME_FIELD = new JTextField(Config.SOUTH_NAME);
    private JTextField TEXT_ALT_NAME_FIELD = new JTextField(Config.NORTH_NAME);
    private JTextField TRANSLATION_NAME_FIELD = new JTextField(Config.ENGLISH_NAME);

    private JComboBox AUTO_APPEND_FIELD = new JComboBox();
    private JComboBox CLEAN_BEFORE_SAVE_FIELD = new JComboBox();

    private JPanel folderPanel = new JPanel(new GridBagLayout());
    private JPanel textSettingPanel = new JPanel(new GridBagLayout());
    private JPanel advancedPanel = new JPanel(new GridBagLayout());

    public NewProjectFrame() {
        super("New Project");
        setLayout(new GridBagLayout());
        int row = 0;

//        add(makeSeperatorPanel("Project Settings", 0), SwingUtils.makeConstraints("0" + row + "2110"));

//        row++;
        add(projectNameLabel, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
        JPanel projectNameFieldPanel = new JPanel(new GridBagLayout());
        projectNameFieldPanel.add(projectNameField, SwingUtils.makeConstraints("001110"));
        add(projectNameFieldPanel, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));
        projectNameField.setPreferredSize(new Dimension(300, projectNameField.getPreferredSize().height));

        projectLocationButton = new ImageButton("find.png", 20) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser dialog = new JFileChooser(projectLocationField.getText());
//                FileFilter ft = new FileNameExtensionFilter("Sqlite Files", "sqlite3");
//                dialog.setAcceptAllFileFilterUsed(false);
//                dialog.addChoosableFileFilter(ft);
                dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = dialog.showSaveDialog(NewProjectFrame.this);
                if (option == JFileChooser.APPROVE_OPTION) {
//                    if (dialog.getSelectedFile().getAbsolutePath().endsWith(".sqlite3"))
//                        assignProjectLocation(dialog.getSelectedFile().getAbsolutePath());
//                    else
                    assignProjectLocation(dialog.getSelectedFile().getAbsolutePath());
                }
                /*JFileChooser dialog = new JFileChooser(projectLocationField.getText());
                FileFilter ft = new FileNameExtensionFilter("Sqlite Files", "sqlite3");
                dialog.setAcceptAllFileFilterUsed(false);
                dialog.addChoosableFileFilter(ft);
                int option = dialog.showSaveDialog(NewProjectFrame.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    if (dialog.getSelectedFile().getAbsolutePath().endsWith(".sqlite3"))
                        assignProjectLocation(dialog.getSelectedFile().getAbsolutePath());
                    else
                        assignProjectLocation(dialog.getSelectedFile().getAbsolutePath() + ".sqlite3");
                }*/
            }
        };
        row++;
        add(projectLocationLabel, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
        JPanel fileNamePanel = new JPanel(new GridBagLayout());
        fileNamePanel.add(projectLocationField, SwingUtils.makeConstraints("0" + 0 + "1110"));
        fileNamePanel.add(projectLocationButton, SwingUtils.makeConstraints("1" + 0 + "1100"));
        add(fileNamePanel, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));

        row++;
        add(makeSeperatorPanel("Folder Settings", 1), SwingUtils.makeConstraints("0" + row + "2110"));

        row++;
        add(folderPanel, SwingUtils.makeConstraints("0" + row + "2110"));

        row++;
        add(makeSeperatorPanel("Text Settings", 2), SwingUtils.makeConstraints("0" + row + "2110"));

        row++;
        add(textSettingPanel, SwingUtils.makeConstraints("0" + row + "2110"));

        row++;
        add(makeSeperatorPanel("Advanced Settings", 3), SwingUtils.makeConstraints("0" + row + "2110"));

        row++;
        add(advancedPanel, SwingUtils.makeConstraints("0" + row + "2110"));

        row++;
        add(makeButtonPanel(), SwingUtils.makeConstraints("0," + row + ",3,1,1,0"));

        setLocation(MouseInfo.getPointerInfo().getLocation());
        pack();
        setVisible(true);
    }

    private void assignProjectLocation(String absolutePath) {
        projectLocationField.setText(absolutePath);
    }

    private JPanel makeButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.add(new ImageButton("ok.png", "OK", 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (JOptionPane.showConfirmDialog(null,
                        "<html><body><p style='width: 400px;'>"
                                + "The project database will be stored in the directory:" + "<br>"
                                + projectLocationField.getText().trim() + "<br>"
                                + "as 'langDB.sqlite3'," + "<br>"
                                + "and the following sub-directories will be created:" + "<br>"
                                + projectLocationField.getText() + File.separator + imageLocationField.getText().trim() + "<br>"
                                + projectLocationField.getText() + File.separator + audioLocationField.getText().trim() + "<br>"
                                + projectLocationField.getText() + File.separator + htmlLocationField.getText().trim() + "<br><br>"
                                + "Are you sure?"
                                + "</p></body></html>",
                        "New Project Creation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    makeProject();
                    Main.main.requestFocus();
                    dispose();
                }
            }
        }, SwingUtils.makeConstraints("0" + 0 + "1110"));
        buttonPanel.add(new ImageButton("cancel.png", "Cancel", 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Main.main.requestFocus();
                dispose();
            }
        }, SwingUtils.makeConstraints("1" + 0 + "1110"));
        return buttonPanel;
    }

    private JPanel makeSeperatorPanel(String heading, int hint) {
        JPanel textPanel = new JPanel(new GridBagLayout());
        JLabel textSettings = new JLabel(heading);
        textPanel.add(new JSeparator(JSeparator.HORIZONTAL), SwingUtils.makeConstraints("0" + 0 + "1110"));
        textPanel.add(textSettings, SwingUtils.makeConstraints("1" + 0 + "1100"));
        if (hint == 1) {
            JLabel label = new JLabel(" + ");
            label.setOpaque(true);
            label.setBackground(Color.darkGray);
            label.setForeground(Color.white);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (folderPanel.getComponents().length > 0) folderPanel.removeAll();
                    else {
                        int row = 0;
                        folderPanel.add(imageLocationLabel, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        JPanel imageNamePanel = new JPanel(new GridBagLayout());
                        imageNamePanel.add(imageLocationLabelFront, SwingUtils.makeConstraints("0" + 0 + "1100"));
                        imageNamePanel.add(imageLocationField, SwingUtils.makeConstraints("1" + 0 + "1110"));
                        folderPanel.add(imageNamePanel, SwingUtils.makeConstraints("1" + row + "1110"));

                        row++;
                        folderPanel.add(audioLocationLabel, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        JPanel audioNamePanel = new JPanel(new GridBagLayout());
                        audioNamePanel.add(audioLocationLabelFront, SwingUtils.makeConstraints("0" + 0 + "1100"));
                        audioNamePanel.add(audioLocationField, SwingUtils.makeConstraints("1" + 0 + "1110"));
                        folderPanel.add(audioNamePanel, SwingUtils.makeConstraints("1" + row + "1110"));

                        row++;
                        folderPanel.add(htmlLocationLabel, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        JPanel htmlNamePanel = new JPanel(new GridBagLayout());
                        htmlNamePanel.add(htmlLocationLabelFront, SwingUtils.makeConstraints("0" + 0 + "1100"));
                        htmlNamePanel.add(htmlLocationField, SwingUtils.makeConstraints("1" + 0 + "1110"));
                        folderPanel.add(htmlNamePanel, SwingUtils.makeConstraints("1" + row + "1110"));
                    }
                    folderPanel.revalidate();
                    folderPanel.repaint();
                    NewProjectFrame.this.pack();
                    NewProjectFrame.this.repaint();
                }
            });
            textPanel.add(label, SwingUtils.makeConstraints("2" + 0 + "1100"));
            textPanel.add(new JSeparator(JSeparator.HORIZONTAL), SwingUtils.makeConstraints("3" + 0 + "1110"));
        } else if (hint == 2) {
            JLabel label = new JLabel(" + ");
            label.setOpaque(true);
            label.setBackground(Color.darkGray);
            label.setForeground(Color.white);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (textSettingPanel.getComponents().length > 0) textSettingPanel.removeAll();
                    else {
                        int row = 0;
                        textSettingPanel.add(ABOUT_NAME_LABEL, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        textSettingPanel.add(ABOUT_NAME_FIELD, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));
                        row++;
                        textSettingPanel.add(LESSON_NAME_LABEL, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        textSettingPanel.add(LESSON_NAME_FIELD, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));
                        row++;
                        textSettingPanel.add(DIALOG_NAME_LABEL, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        textSettingPanel.add(DIALOG_NAME_FIELD, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));
                        row++;
                        textSettingPanel.add(GRAMMAR_NAME_LABEL, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        textSettingPanel.add(GRAMMAR_NAME_FIELD, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));
                        row++;
                        textSettingPanel.add(QUESTION_NAME_LABEL, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        textSettingPanel.add(QUESTION_NAME_FIELD, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));
                        row++;
                        textSettingPanel.add(PATTERN_NAME_LABEL, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        textSettingPanel.add(PATTERN_NAME_FIELD, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));
                        row++;
                        textSettingPanel.add(VOCABULARY_NAME_LABEL, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        textSettingPanel.add(VOCABULARY_NAME_FIELD, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));
                        row++;
                        textSettingPanel.add(TEXT_NAME_LABEL, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        textSettingPanel.add(TEXT_NAME_FIELD, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));
                        row++;
                        textSettingPanel.add(TEXT_ALT_NAME_LABEL, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        textSettingPanel.add(TEXT_ALT_NAME_FIELD, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));
                        row++;
                        textSettingPanel.add(TRANSLATION_NAME_LABEL, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        textSettingPanel.add(TRANSLATION_NAME_FIELD, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));
                    }
                    textSettingPanel.revalidate();
                    textSettingPanel.repaint();
                    NewProjectFrame.this.pack();
                    NewProjectFrame.this.repaint();
                }
            });
            textPanel.add(label, SwingUtils.makeConstraints("2" + 0 + "1100"));
            textPanel.add(new JSeparator(JSeparator.HORIZONTAL), SwingUtils.makeConstraints("3" + 0 + "1110"));
        } else if (hint == 3) {
            JLabel label = new JLabel(" + ");
            label.setOpaque(true);
            label.setBackground(Color.darkGray);
            label.setForeground(Color.white);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (advancedPanel.getComponents().length > 0) advancedPanel.removeAll();
                    else {
                        int row = 0;
                        advancedPanel.add(AUTO_APPEND_LABEL, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        AUTO_APPEND_FIELD = new JComboBox(new String[]{"YES", "NO"});
                        AUTO_APPEND_FIELD.setSelectedItem(Config.AUTO_APPEND);
                        AUTO_APPEND_FIELD.setBorder(BorderFactory.createEmptyBorder());
                        advancedPanel.add(AUTO_APPEND_FIELD, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));
                        row++;
                        advancedPanel.add(CLEAN_BEFORE_SAVE_LABEL, SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
                        CLEAN_BEFORE_SAVE_FIELD = new JComboBox(new String[]{"YES", "NO"});
                        CLEAN_BEFORE_SAVE_FIELD.setSelectedItem(Config.CLEAN_BEFORE_SAVE);
                        CLEAN_BEFORE_SAVE_FIELD.setBorder(BorderFactory.createEmptyBorder());
                        advancedPanel.add(CLEAN_BEFORE_SAVE_FIELD, SwingUtils.makeConstraints("1," + row + ",1,1,1,0"));
                    }
                    advancedPanel.revalidate();
                    advancedPanel.repaint();
                    NewProjectFrame.this.pack();
                    NewProjectFrame.this.repaint();
                }
            });
            textPanel.add(label, SwingUtils.makeConstraints("2" + 0 + "1100"));
            textPanel.add(new JSeparator(JSeparator.HORIZONTAL), SwingUtils.makeConstraints("3" + 0 + "1110"));
        } else textPanel.add(new JSeparator(JSeparator.HORIZONTAL), SwingUtils.makeConstraints("2" + 0 + "1110"));
        return textPanel;
    }

    public static void main(String[] args) {
        new NewProjectFrame();
    }

    private void makeProject() {
        Config.PROJECT_NAME = projectNameField.getText().trim();
        Config.AUDIO_FOLDER = audioLocationField.getText().trim();
        Config.HTML_FOLDER = htmlLocationField.getText().trim();
        Config.IMAGE_FOLDER = imageLocationField.getText().trim();
        Config.DATABASE_FILE = projectLocationField.getText().trim() + File.separator + "langDB" + ".sqlite3";
        Config.ABOUT_NAME = ABOUT_NAME_FIELD.getText().trim();
        Config.LESSON_NAME = LESSON_NAME_FIELD.getText().trim();
        Config.DIALOG_NAME = DIALOG_NAME_FIELD.getText().trim();
        Config.GRAMMAR_NAME = GRAMMAR_NAME_FIELD.getText().trim();
        Config.QUESTION_NAME = QUESTION_NAME_FIELD.getText().trim();
        Config.PATTERN_NAME = PATTERN_NAME_FIELD.getText().trim();
        Config.VOCABULARY_NAME = VOCABULARY_NAME_FIELD.getText().trim();
        Config.SOUTH_NAME = TEXT_NAME_FIELD.getText().trim();
        Config.NORTH_NAME = TEXT_ALT_NAME_FIELD.getText().trim();
        Config.ENGLISH_NAME = TRANSLATION_NAME_FIELD.getText().trim();
        Config.AUTO_APPEND = (String) AUTO_APPEND_FIELD.getSelectedItem() == null ? "NO" : (String) AUTO_APPEND_FIELD.getSelectedItem();
        Config.CLEAN_BEFORE_SAVE = (String) CLEAN_BEFORE_SAVE_FIELD.getSelectedItem() == null ? "YES" : (String) CLEAN_BEFORE_SAVE_FIELD.getSelectedItem();

        File projectRoot = new File(projectLocationField.getText().trim());
        if (!projectRoot.exists()) projectRoot.mkdir();

        PrintWriter writer = null;
        try {
            if (new File(Config.DATABASE_FILE).exists()) new File(Config.DATABASE_FILE).delete();
            writer = new PrintWriter(new File(Config.DATABASE_FILE), "UTF-8");
        } catch (FileNotFoundException e) {
            SwingDialog.error(e.getMessage(), "Config");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.flush();
        writer.close();

        File imageRoot = new File(Config.getImageFolder());
        if (!imageRoot.exists()) imageRoot.mkdir();
        File audioRoot = new File(Config.getAudioFolder());
        if (!audioRoot.exists()) audioRoot.mkdir();
        File htmlRoot = new File(Config.getHTMLFolder());
        if (!htmlRoot.exists()) htmlRoot.mkdir();

        Database database = null;
        try {
            database = new Database(Config.DATABASE_FILE);
            database.createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Main.main.load(database);
        if (Config.isAutoAppend()) Main.main.lessons.add(new Lesson().fill());
    }

    @Override
    protected void refresh() {

    }

    @Override
    protected boolean exitAction(KeyEvent e) {
        if (e == null || (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE)) {
            dispose();
            Main.main.requestFocus();
            return true;
        }
        return false;
    }

    @Override
    protected boolean hotkeyAction(KeyEvent e) {
        return false;
    }
}
