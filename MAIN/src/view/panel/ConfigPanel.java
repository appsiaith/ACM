package view.panel;

import util.Config;
import util.SwingUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 18/01/14
 * Time: 16:21
 */
public class ConfigPanel extends JPanel {
    private JLabel[] labels = new JLabel[]{
            new JLabel("Project Name"),
            new JLabel("Unit"),
            new JLabel("Pattern"),
            new JLabel("Question"),
            new JLabel("Grammar"),
            new JLabel("Vocabulary"),
            new JLabel("Dialogue"),
            new JLabel("About"),
            new JLabel("English"),
            new JLabel("South Wales"),
            new JLabel("North Wales")
    };
    private JTextField[] textFields = new JTextField[]{
            new JTextField(Config.PROJECT_NAME),
            new JTextField(Config.LESSON_NAME),
            new JTextField(Config.PATTERN_NAME),
            new JTextField(Config.QUESTION_NAME),
            new JTextField(Config.GRAMMAR_NAME),
            new JTextField(Config.VOCABULARY_NAME),
            new JTextField(Config.DIALOG_NAME),
            new JTextField(Config.ABOUT_NAME),
            new JTextField(Config.ENGLISH_NAME),
            new JTextField(Config.SOUTH_NAME),
            new JTextField(Config.NORTH_NAME)
    };
    private String[] fieldNames = new String[]{
            "PROJECT_NAME",
            "LESSON_NAME",
            "PATTERN_NAME",
            "QUESTION_NAME",
            "GRAMMAR_NAME",
            "VOCABULARY_NAME",
            "DIALOG_NAME",
            "ABOUT_NAME",
            "ENGLISH_NAME",
            "SOUTH_NAME",
            "NORTH_NAME"
    };

    public ConfigPanel() {
        super(new GridBagLayout());
        final Config config = new Config();
        JLabel noticeLabel = new JLabel("<html><p style='width: 450px;'><b>Note that the settings shown in this panel only affect the appearance of the content manager itself, and have no impact on the apps.</b></p></html>");
        noticeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(noticeLabel, SwingUtils.makeConstraints("003110"));

        add(new JLabel("Database Path"), SwingUtils.makeConstraints("0," + 1 + ",1,1,0,1"));
        add(new JLabel(Config.DATABASE_FILE), SwingUtils.makeConstraints("1," + 1 + ",2,1,1,1"));
        int row = 2;
        for (int i = 0; i < labels.length; i++) {
            row++;
            add(labels[i], SwingUtils.makeConstraints("0," + row + ",1,1,0,1"));
            add(textFields[i], SwingUtils.makeConstraints("1," + row + ",2,1,1,1"));
            try {
                final Field field = Config.class.getDeclaredField(fieldNames[i]);
                final int finalI = i;
                textFields[i].getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        try {
                            field.set(config, textFields[finalI].getText());
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        try {
                            field.set(config, textFields[finalI].getText());
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        try {
                            field.set(config, textFields[finalI].getText());
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        try {
            row++;
            add(new JLabel("Auto Append"), SwingUtils.makeConstraints("0," + row + ",1,1,0,1"));
            final Field field = Config.class.getDeclaredField("AUTO_APPEND");
            final JComboBox comboBox = new JComboBox(new String[]{"YES", "NO"});
            comboBox.setSelectedItem(field.get(config));
            comboBox.setBorder(BorderFactory.createEmptyBorder());
            comboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    try {
                        field.set(config, comboBox.getSelectedItem());
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            add(comboBox, SwingUtils.makeConstraints("1," + (row) + ",2,1,1,1"));
            row++;
            add(new JLabel("Auto Clean"), SwingUtils.makeConstraints("0," + row + ",1,1,0,1"));
            final Field field2 = Config.class.getDeclaredField("CLEAN_BEFORE_SAVE");
            final JComboBox comboBox2 = new JComboBox(new String[]{"YES", "NO"});
            comboBox2.setSelectedItem(field2.get(config));
            comboBox2.setBorder(BorderFactory.createEmptyBorder());
            comboBox2.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    try {
                        field2.set(config, comboBox2.getSelectedItem());
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            add(comboBox2, SwingUtils.makeConstraints("1," + (row) + ",2,1,1,1"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
