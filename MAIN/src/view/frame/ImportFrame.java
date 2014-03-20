package view.frame;

import model.Element;
import model.ImportData;
import model.Lesson;
import util.AlphanumComparator;
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
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 08/03/14
 * Time: 12:24
 */
public class ImportFrame extends MyFrame {
    Vector<JTextField> sourceFiles = new Vector<JTextField>(5);
    Vector<ImportData> importData = new Vector<ImportData>(5);
    Vector<HeaderPanel> headerPanels = new Vector<HeaderPanel>(5);
    Vector<DestinationList> destinationLessons = new Vector<DestinationList>();
    Vector<String> candidateLessonIds = new Vector<String>();
    private final JComboBox delimiterList = new JComboBox(new String[]{"Tab", "Comma", "Colon"});
    private final JCheckBox hasHeaders = new JCheckBox("Contain Headers", true);
    private final JComboBox encodingComboBox = new JComboBox(new String[]{"UTF-16", "UTF-8"});
    HashMap<String, Lesson> mapping;
    JPanel background = new JPanel(new GridBagLayout());
    JPanel buttonPanel = new JPanel(new GridBagLayout());
    JPanel filePanel;
    JPanel matchPanel;

    public ImportFrame() {
        super("Configure Source Files");
        JScrollPane scrollPane = new JScrollPane();
        if (filePanel == null) {
            int row = 0;
            filePanel = new JPanel(new GridBagLayout());
            filePanel.add(createSourcePanel(), SwingUtils.makeConstraints("0" + row++ + "1110"));
            filePanel.add(createMiscPanel(), SwingUtils.makeConstraints("0" + row++ + "1110"));
        }
        background.add(filePanel, SwingUtils.makeConstraints("0" + 0 + "1110"));
        scrollPane.setViewportView(background);
        add(scrollPane, SwingUtils.makeConstraints("001111"));
        buttonPanel.add(new ImageButton("next.png", "Next", 32, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                firstNext();
            }
        }, SwingUtils.makeConstraints("0" + 0 + "1110"));
        add(buttonPanel, SwingUtils.makeConstraints("011110"));
        pack();
        setPreferredSize(new Dimension(ProgramSettings.DETAIL_PANEL_WIDTH, ProgramSettings.DETAIL_PANEL_HEIGHT));
        setLocation(MouseInfo.getPointerInfo().getLocation());
        setVisible(true);
    }

    boolean checkFiles() {
        boolean ok = false;
        importData.clear();
        for (int i = 0; i < sourceFiles.size(); i++) {
            if (!sourceFiles.get(i).getText().trim().isEmpty()) {
                importData.add(new ImportData(Element.ELEMENT_CLASSES[i], Element.ELEMENT_TYPES[i], Element.ELEMENT_TEXTS[i], sourceFiles.get(i).getText().trim(),
                        encodingComboBox.getSelectedItem().toString().trim(), getDelimiter()));
                ok = true;
            } else importData.add(null);
        }
        return ok;
    }

    boolean checkHeaders() {
        boolean ok = false;
        candidateLessonIds.clear();
        for (int h = 0; h < headerPanels.size(); h++) {
            if (headerPanels.get(h) != null) {
                Vector<String> headers = headerPanels.get(h).getSourceFieldNames();
                int lessonIndex = headers.indexOf(Config.LESSON_NAME.toLowerCase() + "Id");
                importData.get(h).extractLessonIds(lessonIndex, hasHeader(), candidateLessonIds);
                ok = true;
            }
        }
        Collections.sort(candidateLessonIds, new AlphanumComparator());
        return ok;
    }

    void firstNext() {
        if (!checkFiles()) {
            SwingDialog.error("Unable to read source file(s)", "Import from File");
            return;
        }
        setTitle("Match Headers");
        background.removeAll();
        int row = 0;
        matchPanel = createHeaderPanel();
        background.add(matchPanel, SwingUtils.makeConstraints("0" + 0 + "2110"));
        buttonPanel.removeAll();
        buttonPanel.add(new ImageButton("prev.png", "Back", 32, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                secondBack();
            }
        }, SwingUtils.makeConstraints("0" + row + "1110"));
        buttonPanel.add(new ImageButton("next.png", "Next", 32, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                secondNext();
            }
        }, SwingUtils.makeConstraints("1" + row + "1110"));
        getRootPane().revalidate();
        pack();
        repaint();
    }

    void secondNext() {
        if (!checkHeaders()) {
            SwingDialog.error("Problem to read column titles", "Header Matching");
            return;
        }
        setTitle("Assign Destination " + Config.LESSON_NAME + "s");
        background.removeAll();
        int row = 0;
        background.add(createDestinationPanel(), SwingUtils.makeConstraints("0" + row + "1110"));
        buttonPanel.removeAll();
        buttonPanel.add(createButtonPanel(), SwingUtils.makeConstraints("0" + row + "1110"));
        getRootPane().revalidate();
        pack();
        repaint();
    }

    void secondBack() {
        setTitle("Configure Source Files");
        background.removeAll();
        background.add(filePanel, SwingUtils.makeConstraints("0" + 0 + "1110"));
        buttonPanel.removeAll();
        buttonPanel.add(new ImageButton("next.png", "Next", 32, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                firstNext();
            }
        }, SwingUtils.makeConstraints("0" + 0 + "1110"));
        getRootPane().revalidate();
        pack();
        repaint();
    }

    void thirdBack() {
        setTitle("Match Headers");
        background.removeAll();
        int row = 0;
        background.add(matchPanel, SwingUtils.makeConstraints("0" + 0 + "2110"));
        buttonPanel.removeAll();
        buttonPanel.add(new ImageButton("prev.png", "Back", 32, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                secondBack();
            }
        }, SwingUtils.makeConstraints("0" + row + "1110"));
        buttonPanel.add(new ImageButton("next.png", "Next", 32, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                secondNext();
            }
        }, SwingUtils.makeConstraints("1" + row + "1110"));
        getRootPane().revalidate();
        pack();
        repaint();
    }

    /*
    new ImageButton("config.png", "Configure Imports", 32, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                headerPanel.removeAll();
                headerPanel.add(createHeaderPanel(), SwingUtils.makeConstraints("0" + 0 + "1110"));
                destinationPanel.removeAll();
                destinationPanel.add(createDestinationPanel(), SwingUtils.makeConstraints("0" + 0 + "1110"));
                buttonPanel.removeAll();
                buttonPanel.add(createButtonPanel(), SwingUtils.makeConstraints("0" + 0 + "1110"));
                revalidate();
                pack();
                repaint();
            }
        }
     */
    private JPanel createSourcePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
//        panel.add(new MenuLabel("Source Files"), SwingUtils.makeConstraints("003110"));
        for (int i = 0; i < Config.getElementNames().length; i++) {
            JLabel typeLabel = new JLabel(Config.getElementNames()[i]);
            panel.add(typeLabel, SwingUtils.makeConstraints("0" + (i + 1) + "1100"));
            final JTextField sourceField = new JTextField();
            sourceField.setPreferredSize(new Dimension(200, sourceField.getPreferredSize().height));
            panel.add(sourceField, SwingUtils.makeConstraints("1" + (i + 1) + "1110"));
            sourceFiles.add(sourceField);
            final ImageButton openButton = new ImageButton("find.png", 20) {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    String fileName = SwingDialog.fileDialog("*.txt;*.xls");
                    if (fileName != null) sourceField.setText(fileName);
                    else requestFocus();
                }
            };
            panel.add(openButton, SwingUtils.makeConstraints("2" + (i + 1) + "1100"));
        }
        return panel;
    }

    private JPanel createDestinationPanel() {
        destinationLessons.clear();
        JPanel panel = new JPanel(new GridBagLayout());
//        panel.add(new MenuLabel("Destination " + Config.LESSON_NAME + "s"), SwingUtils.makeConstraints("002110"));
        if (candidateLessonIds.isEmpty()) {
            JLabel candidateLessonIdLabel = new JLabel("Source " + Config.LESSON_NAME + "Id: " + "N/A" + " => ");
//            candidateLessonIdLabel.setPreferredSize(new Dimension(20, candidateLessonIdLabel.getPreferredSize().height));
//            candidateLessonIdLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(candidateLessonIdLabel, SwingUtils.makeConstraints("0," + 1 + ",1,1,0,0"));
            DestinationList lessonList = new DestinationList("N/A");
            lessonList.addItem("Create New " + Config.LESSON_NAME);
            for (int l = 0; l < Main.main.lessons.size(); l++)
                lessonList.addItem(Config.LESSON_NAME + " " + (l + 1) + ": " +
                        (Main.main.lessons.get(l).toString().length() > ProgramSettings.LESSON_LENGTH ?
                                Main.main.lessons.get(l).toString().substring(0, ProgramSettings.LESSON_LENGTH) + " ..." :
                                Main.main.lessons.get(l).toString()));
            panel.add(lessonList, SwingUtils.makeConstraints("1," + 1 + ",1,1,1,0"));
            destinationLessons.add(lessonList);
        } else {
            for (int c = 0; c < candidateLessonIds.size(); c++) {
                JLabel candidateLessonIdLabel = new JLabel("Source " + Config.LESSON_NAME + "Id: " + candidateLessonIds.get(c) + " => ");
//                candidateLessonIdLabel.setPreferredSize(new Dimension(20, candidateLessonIdLabel.getPreferredSize().height));
//                candidateLessonIdLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(candidateLessonIdLabel, SwingUtils.makeConstraints("0," + (c + 1) + ",1,1,0,0"));
                DestinationList lessonList = new DestinationList(candidateLessonIds.get(c));
                lessonList.addItem("Create New " + Config.LESSON_NAME);
                for (int l = 0; l < Main.main.lessons.size(); l++)
                    lessonList.addItem(Config.LESSON_NAME + " " + (l + 1) + ": " +
                            (Main.main.lessons.get(l).toString().length() > ProgramSettings.LESSON_LENGTH ?
                                    Main.main.lessons.get(l).toString().substring(0, ProgramSettings.LESSON_LENGTH) + " ..." :
                                    Main.main.lessons.get(l).toString()));
                panel.add(lessonList, SwingUtils.makeConstraints("1," + (c + 1) + ",1,1,1,0"));
                destinationLessons.add(lessonList);
            }
        }
        return panel;
    }

    class DestinationList extends JComboBox {
        String candidateLessonId;

        public DestinationList(String id) {
            candidateLessonId = id;
        }
    }

    private JPanel createMiscPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
//        panel.add(new MenuLabel("Import Settings"), SwingUtils.makeConstraints("005110"));
        panel.add(new JLabel("Delimiter"), SwingUtils.makeConstraints("011100"));
        delimiterList.setEditable(true);
        panel.add(delimiterList, SwingUtils.makeConstraints("111110"));
        panel.add(new JLabel("Encoding"), SwingUtils.makeConstraints("211100"));
        encodingComboBox.setEditable(true);
        panel.add(encodingComboBox, SwingUtils.makeConstraints("311100"));
        panel.add(hasHeaders, SwingUtils.makeConstraints("411100"));
        return panel;
    }

    String getDelimiter() {
        if (delimiterList.getSelectedItem().toString().trim().toLowerCase().equals("tab")) return "\t";
        if (delimiterList.getSelectedItem().toString().trim().toLowerCase().equals("colon")) return ":";
        if (delimiterList.getSelectedItem().toString().trim().toLowerCase().equals("comma")) return ";";
        return delimiterList.getSelectedItem().toString().trim();
    }

    boolean hasHeader() {
        return hasHeaders.isSelected();
    }

    private JPanel createHeaderPanel() {
        int numColumns = 4;
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel noticeLabel = new JLabel("<html><p>" +
                "<b>If the input isn't displaying properly (strange characters),</b><br><b>try to select an alternative encoding in the previous step.</b></p></html>");
        noticeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(noticeLabel, SwingUtils.makeConstraints("00" + 2 + "110"));
//        panel.add(new MenuLabel("Match Headers"), SwingUtils.makeConstraints("00" + 2 + "110"));
        headerPanels.clear();
        int row = 1;
        for (int e = 0; e < importData.size(); e++) {
            if (importData.get(e) != null) {
                row++;
                panel.add(new JLabel(Config.getElementNames()[e]), SwingUtils.makeConstraints("0," + row + ",1,1,0,1"));
                HeaderPanel headerPanel = new HeaderPanel(importData.get(e).getHeader(), Config.getElementFieldNames()[e], numColumns);
                panel.add(headerPanel, SwingUtils.makeConstraints("1," + row + ",1,1,1,1"));
                headerPanels.add(headerPanel);
            } else headerPanels.add(null);
        }
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(new ImageButton("prev.png", "Back", 32, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                thirdBack();
            }
        }, SwingUtils.makeConstraints("001110"));
        panel.add(new ImageButton("ok.png", "Import", 32, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String message = "";
                int lessonCount = getLessonMappings();
                message += "Created " + lessonCount + " new " + Config.LESSON_NAME + "(s).\n";
                for (int s = 0; s < importData.size(); s++) {
                    if (importData.get(s) != null) {
                        Vector<String> fieldNames = headerPanels.get(s).getSourceFieldNames();
                        message += importData.get(s).process(fieldNames, mapping, hasHeader(), candidateLessonIds) + "\n";
                    }
                }
                JOptionPane.showMessageDialog(null, message, "Import Successful", JOptionPane.INFORMATION_MESSAGE);
                exitAction(null);
            }
        }, SwingUtils.makeConstraints("101110"));
        return panel;
    }

    @Override
    protected void refresh() {

    }

    @Override
    protected boolean exitAction(KeyEvent e) {
        if (e == null || (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE)) {
            Main.main.refresh();
            Main.main.configurationFrame = null;
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

    private int getLessonMappings() {
        int lessonCount = 0;
        mapping = new HashMap<String, Lesson>();
        for (int c = 0; c < destinationLessons.size(); c++) {
            if (destinationLessons.get(c).getSelectedIndex() == 0) {
                Lesson lesson = new Lesson();
                lessonCount++;
                Main.main.lessons.add(lesson);
                mapping.put(destinationLessons.get(c).candidateLessonId, lesson);
            } else {
                mapping.put(destinationLessons.get(c).candidateLessonId,
                        Main.main.lessons.get(destinationLessons.get(c).getSelectedIndex() - 1));
            }
        }
        return lessonCount;
    }
}
