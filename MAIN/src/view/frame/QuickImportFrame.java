package view.frame;

import model.Element;
import model.ImportData;
import util.Config;
import util.ProgramSettings;
import util.SwingUtils;
import view.Main;
import view.components.ImageButton;
import view.components.SwingDialog;
import view.panel.HeaderPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 05/02/2014
 * Time: 09:33
 */
public class QuickImportFrame extends MyFrame {

    //private File inputFile;
    private ImportData importData;
    private JComboBox delimiterList;
    private String[] delimiters = new String[]{"Comma", "Colon", "Tab"};
    private String[] delimiterChars = new String[]{";", ":", "\t"};
    private final JCheckBox hasHeaders = new JCheckBox("Contain Headers", true);
    private Vector<DefaultComboBoxModel> modelGroup = new Vector<DefaultComboBoxModel>();
    private Vector<JLabel> labelGroup = new Vector<JLabel>();
    private Vector<JComboBox> comboGroup = new Vector<JComboBox>();
    private String[] fieldNames = new String[0];
    private HeaderPanel headerPanel;
    private JComboBox typeList = new JComboBox(new String[]{
            Config.PATTERN_NAME,
            Config.QUESTION_NAME,
            Config.VOCABULARY_NAME,
            Config.GRAMMAR_NAME,
            Config.DIALOG_NAME,
//            Config.ABOUT_NAME
    });
    private JComboBox lessonList = new JComboBox();
    private int lessonIndex;
    private int elementType;

    public QuickImportFrame(int lessonIndex, int elementType) {
        super("Perform a quick import from the Clipboard");
        this.lessonIndex = lessonIndex;
        this.elementType = elementType;
        setLayout(new GridBagLayout());
//        add(new JLabel("Perform a quick import from the Clipboard"), SwingUtils.makeConstraints("002110"));
        add(createTypePanel(), SwingUtils.makeConstraints("012110"));
        add(createMiscPanel(), SwingUtils.makeConstraints("022110"));
        headerPanel = new HeaderPanel();
        add(headerPanel, SwingUtils.makeConstraints("032110"));
        add(new ImageButton("ok.png", "Import", 32, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (!validateInputs()) return;
                String message = "";
                Vector<String> fieldNames = headerPanel.getSourceFieldNames();
                message += importData.process(fieldNames, hasHeaders.isSelected(), lessonList.getSelectedIndex());
                Main.main.remakeLessonFrame(Main.main.lessons.get(lessonList.getSelectedIndex()), typeList.getSelectedIndex());
                JOptionPane.showMessageDialog(null, message, "Import Successful", JOptionPane.INFORMATION_MESSAGE);
                exitAction(null);
            }
        }, SwingUtils.makeConstraints("041110"));
        add(new ImageButton("cancel.png", "Cancel", 32, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                dispose();
                exitAction(null);
            }
        }, SwingUtils.makeConstraints("141110"));
        reset();
        pack();
        setLocation(MouseInfo.getPointerInfo().getLocation());
        setVisible(true);
    }

    private boolean validateInputs() {
        ArrayList<String> messages = new ArrayList<String>();
        if (lessonList.getSelectedItem() == null) {
            messages.add("&nbsp;&nbsp;&middot;&nbsp;Destination Unit");
        }
        if (typeList.getSelectedItem() == null) {
            messages.add("&nbsp;&nbsp;&middot;&nbsp;Destination Data Type");
        }
        if (messages.size() > 0) {
            messages.add(0, "Please Specfiy the Following Information:");
            SwingDialog.error(messages, "Import Frame");
            return false;
        } else return true;
    }

    private JPanel createTypePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        typeList.setSelectedIndex(elementType);
        typeList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (lessonList.getSelectedItem() != null && typeList.getSelectedItem() != null) {
                    reset();
                }
            }
        });
        lessonList.setModel(new DefaultComboBoxModel() {
            @Override
            public int getSize() {
                return Main.main.lessons.size();
            }

            @Override
            public String getElementAt(int index) {
                return Config.LESSON_NAME + " " + (index + 1) + ": " +
                        (Main.main.lessons.get(index).toString().length() > ProgramSettings.LESSON_LENGTH ?
                                Main.main.lessons.get(index).toString().substring(0, ProgramSettings.LESSON_LENGTH) + " ..." :
                                Main.main.lessons.get(index).toString());
            }
        });
        lessonList.setSelectedIndex(lessonIndex);
        lessonList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (lessonList.getSelectedItem() != null && typeList.getSelectedItem() != null) {
                    reset();
                }
            }
        });
        panel.add(new MenuLabel("Unit and Data Type"), SwingUtils.makeConstraints("002110"));
        panel.add(lessonList, SwingUtils.makeConstraints("011110"));
        panel.add(typeList, SwingUtils.makeConstraints("111110"));
        return panel;
    }

    private JPanel createMiscPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        delimiterList = new JComboBox(delimiters);
        delimiterList.setSelectedItem("Tab");
        hasHeaders.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                reset();
            }
        });
        panel.add(new MenuLabel("Miscellaneous"), SwingUtils.makeConstraints("003110"));
        panel.add(new JLabel("Delimiter"), SwingUtils.makeConstraints("011100"));
        panel.add(delimiterList, SwingUtils.makeConstraints("111110"));
        panel.add(hasHeaders, SwingUtils.makeConstraints("211100"));
        return panel;
    }

    private void reset() {
        importData = new ImportData(Element.ELEMENT_CLASSES[typeList.getSelectedIndex()], Element.ELEMENT_TYPES[typeList.getSelectedIndex()], Element.ELEMENT_TEXTS[typeList.getSelectedIndex()], null, "UTF-8", delimiterChars[delimiterList.getSelectedIndex()]);
        headerPanel.reset(importData.getHeader(), Config.getElementFieldNames()[typeList.getSelectedIndex()], 4);
        pack();
        repaint();
    }

    /*private void createHeaderPanel() {
        int numColumns = 4;
        labelGroup.clear();
        modelGroup.clear();
        comboGroup.clear();
        if (headerPanel != null) headerPanel.removeAll();
        Vector<String> originalHeaders = importData.getHeader();
        switch (typeList.getSelectedIndex()) {
            case Element.PATTERN:
                fieldNames = (new Pattern()).getFieldNames();
                break;
            case Element.QUESTION:
                fieldNames = (new Question()).getFieldNames();
                break;
            case Element.VOCABULARY:
                fieldNames = (new Vocabulary()).getFieldNames();
                break;
            case Element.GRAMMAR:
                fieldNames = (new Grammar()).getFieldNames();
                break;
            case Element.DIALOG:
                fieldNames = (new Dialogue()).getFieldNames();
                break;
            case Element.ABOUT:
                fieldNames = (new About()).getFieldNames();
                break;
            default:
                break;
        }
        for (int i = 0; i < originalHeaders.size(); i++) {
            JLabel label = new JLabel(originalHeaders.get(i));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(120, 20));
            labelGroup.add(label);
            headerPanel.add(label, SwingUtils.makeConstraints(i % numColumns + "" + (i / numColumns * 2 + 1) + "1110"));
            final DefaultComboBoxModel model = new DefaultComboBoxModel();
            for (int f = 0; f < fieldNames.length; f++)
                model.addElement(fieldNames[f]);
            model.addElement("grouping");
            model.addElement("ordering");
            model.addElement(Config.LESSON_NAME.toLowerCase() + "Id");
            model.addElement(null);
            JComboBox comboBox = new JComboBox(model);
            comboBox.setSelectedItem(null);
            modelGroup.add(model);
            comboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED)
                        for (int i = 0; i < modelGroup.size(); i++) {
                            if (!modelGroup.get(i).equals(model)) modelGroup.get(i).removeElement(e.getItem());
                        }
                    else if (e.getStateChange() == ItemEvent.DESELECTED) {
                        for (int i = 0; i < modelGroup.size(); i++) {
                            if (!modelGroup.get(i).equals(model)) {
                                modelGroup.get(i).removeElement(e.getItem());
                                modelGroup.get(i).addElement((String) e.getItem());
                            }
                        }
                    }
                }
            });
            comboBox.setMaximumRowCount(comboBox.getModel().getSize());
            comboBox.setPreferredSize(new Dimension(120, 20));
            comboGroup.add(comboBox);
            headerPanel.add(comboBox, SwingUtils.makeConstraints(i % numColumns + "" + (i / numColumns * 2 + 2) + "1110"));
        }
        headerPanel.add(new MenuLabel("Data Heading"), SwingUtils.makeConstraints("00" + numColumns + "110"));
        autoComplete();
        revalidate();
        pack();
        repaint();
    }
*/
    private void autoComplete() {
        for (int i = 0; i < labelGroup.size(); i++) {
            innerloop:
            for (int j = 0; j < modelGroup.get(i).getSize(); j++) {
                if (modelGroup.get(i).getElementAt(j) != null) {
                    if (((String)modelGroup.get(i).getElementAt(j)).toUpperCase().equals(labelGroup.get(i).getText().toUpperCase())) {
                        comboGroup.get(i).setSelectedIndex(j);
                        break innerloop;
                    }
                    if (modelGroup.get(i).getElementAt(j).equals(ProgramSettings.convertKeyword(labelGroup.get(i).getText()))) {
                        comboGroup.get(i).setSelectedIndex(j);
                        break innerloop;
                    }
                }
            }
        }
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

    class MenuLabel extends JLabel {
        public MenuLabel(String title) {
            super(title);
            setFont(new javax.swing.plaf.FontUIResource(new Font("Tahoma", Font.BOLD, 14)));
            setHorizontalAlignment(SwingConstants.CENTER);
            setBackground(Color.BLACK);
            setForeground(Color.LIGHT_GRAY);
            setOpaque(true);
        }
    }
}
