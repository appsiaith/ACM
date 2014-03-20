package view.components;

import model.Element;
import model.FieldRetrievable;
import model.SearchResult;
import util.Config;
import util.ProgramSettings;
import util.SwingUtils;
import view.Main;
import view.frame.SearchResultFrame;
import view.panel.ElementGroupsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 02/02/14
 * Time: 22:36
 */
public class SwingDialog {

    public static void missingFileDialog(Vector<String> missingFiles) {
        File file = new File(Config.getProjectFolder() + "missing_audios.txt");
        try {
            if (file.exists()) file.createNewFile();
            PrintWriter writer = new PrintWriter(file);
            writer.println("Missing audio files in project " + Config.PROJECT_NAME);
            writer.println(Config.DATABASE_FILE);
            writer.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
            writer.println("- - -");
            for (int i = 0; i < missingFiles.size(); i++) writer.println(missingFiles.get(i));
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            SwingDialog.error("Error creating log file", "Missing Audio File Log");
        } catch (IOException e) {
            SwingDialog.error("Error creating log file", "Missing Audio File Log");
        }
        int option = JOptionPane.showConfirmDialog(null, "<html><body><p style='width: 250px;'>" +
                "During the audio file name optimisation operation, " + missingFiles.size() + " missing audio " + (missingFiles.size() == 1 ? "file has" : "files have") + " been identified, " +
                "and a log file has been stored in the project directory.</p>" +
                "<p style='width: 250px;'>Would you like to view the log now?" + "</p></body></html>", "Audio file rename result",
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            if (file.exists()) {
                Desktop desktop = null;
                if (Desktop.isDesktopSupported()) {
                    desktop = Desktop.getDesktop();
                }
                try {
                    desktop.edit(file);
                } catch (IOException e) {
                    SwingDialog.error("Error showing log file", "Missing Audio File Log");
                }
            }
        }
    }

    public static String audioDialog() {
        FileDialog dialog = new FileDialog(Main.main, "Select Audio ...");
        dialog.setDirectory(ProgramSettings.getLastAudioDirectory());
        dialog.setFile("*.mp3");
        dialog.setVisible(true);
        String fileName = dialog.getFile();
        if (fileName != null && !fileName.isEmpty()) {
            File file = new File(dialog.getDirectory() + File.separator + fileName);
            ProgramSettings.last_AUDIO_ROOT = file.getParent();
            SwingUtils.copyFile(file, Config.getAudioFolder());
            return file.getName();
        }
        return null;
    }

    public static String htmlDialog() {
        FileDialog dialog = new FileDialog(Main.main, "Select HTML ...");
        dialog.setDirectory(ProgramSettings.getLastFileDirectory());
        dialog.setFile("*.html");
        dialog.setVisible(true);
        String fileName = dialog.getFile();
        if (fileName != null && !fileName.isEmpty()) {
            File file = new File(dialog.getDirectory() + File.separator + fileName);
            ProgramSettings.last_FILE_ROOT = file.getParent();
            SwingUtils.copyFile(file, Config.getHTMLFolder());
            return file.getName();
        }
        return null;
    }

    public static String imageDialog() {
        FileDialog dialog = new FileDialog(Main.main, "Select Image ...");
        dialog.setDirectory(ProgramSettings.getLastImageDirectory());
        dialog.setFile("*.jpg;*.jpeg");
        dialog.setVisible(true);
        String fileName = dialog.getFile();
        if (fileName != null && !fileName.isEmpty()) {
            File file = new File(dialog.getDirectory() + File.separator + fileName);
            ProgramSettings.last_IMG_ROOT = file.getParent();
            SwingUtils.copyFile(file, Config.getImageFolder());
            return file.getName();
        }
        return null;
    }

    public static String fileDialog(String extension) {
        FileDialog dialog = new FileDialog(Main.main, "Select File ...");
        dialog.setDirectory((ProgramSettings.last_FILE_ROOT == null || ProgramSettings.last_FILE_ROOT.isEmpty()) ? (new File(Config.DATABASE_FILE).getParent() + File.separator) : ProgramSettings.last_FILE_ROOT);
        dialog.setFile(extension);
        dialog.setVisible(true);
        String fileName = dialog.getFile();
        if (fileName != null && !fileName.isEmpty()) {
            File file = new File(dialog.getDirectory() + File.separator + fileName);
            ProgramSettings.last_FILE_ROOT = file.getParent();
            return file.getAbsolutePath();
        }
        return null;
    }

    public static void findDialog() {
        final JDialog typeDialog = new JDialog();
        typeDialog.setTitle("Find Records");
        typeDialog.setLayout(new GridBagLayout());

        typeDialog.add(new JLabel("Search for"), SwingUtils.makeConstraints(0 + "" + 0 + "1101"));
        final JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 24));
        typeDialog.add(field, SwingUtils.makeConstraints(1 + "" + 0 + "1111"));

//        JPanel checkBoxPanel = new JPanel(new GridBagLayout());
//        JCheckBox matchCase = new JCheckBox("Case Sensitive");
//        JCheckBox matchWord = new JCheckBox("Match Exact Word");
//
//        String keyword = field.getText();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(new ImageButton("ok.png", "OK", 16, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Vector<SearchResult> results = new Vector<SearchResult>();
                for (int lessonIndex = 0; lessonIndex < Main.main.lessons.size(); lessonIndex++) {
                    if (Main.main.lessons.get(lessonIndex).getSouth().contains(field.getText()))
                        results.add(new SearchResult(lessonIndex, Main.main.lessons.get(lessonIndex).getSouth()));
                    if (Main.main.lessons.get(lessonIndex).getNorth().contains(field.getText()))
                        results.add(new SearchResult(lessonIndex, Main.main.lessons.get(lessonIndex).getNorth()));
                    if (Main.main.lessons.get(lessonIndex).getEnglish().contains(field.getText()))
                        results.add(new SearchResult(lessonIndex, Main.main.lessons.get(lessonIndex).getEnglish()));
                    for (int elementType = 0; elementType < 5; elementType++) {
                        for (int groupIndex = 0; groupIndex < Main.main.lessons.get(lessonIndex).getElementGroups(elementType).size(); groupIndex++) {
                            if (Main.main.lessons.get(lessonIndex).getElementGroups(elementType).get(groupIndex).getSouth().contains(field.getText()))
                                results.add(new SearchResult(lessonIndex, elementType, groupIndex, Main.main.lessons.get(lessonIndex).getElementGroups(elementType).get(groupIndex).getSouth()));
                            if (Main.main.lessons.get(lessonIndex).getElementGroups(elementType).get(groupIndex).getNorth().contains(field.getText()))
                                results.add(new SearchResult(lessonIndex, elementType, groupIndex, Main.main.lessons.get(lessonIndex).getElementGroups(elementType).get(groupIndex).getNorth()));
                            if (Main.main.lessons.get(lessonIndex).getElementGroups(elementType).get(groupIndex).getEnglish().contains(field.getText()))
                                results.add(new SearchResult(lessonIndex, elementType, groupIndex, Main.main.lessons.get(lessonIndex).getElementGroups(elementType).get(groupIndex).getEnglish()));
                            for (int elementIndex = 0; elementIndex < Main.main.lessons.get(lessonIndex).getElementGroups(elementType).get(groupIndex).size(); elementIndex++) {
                                for (int fieldIndex = 0; fieldIndex < Main.main.lessons.get(lessonIndex).getElementGroups(elementType).getInstance().getFieldNames().length; fieldIndex++) {
                                    if (((FieldRetrievable) Main.main.lessons.get(lessonIndex).getElementGroups(elementType).get(groupIndex).get(elementIndex)).getField(Main.main.lessons.get(lessonIndex).getElementGroups(elementType).getInstance().getFieldNames()[fieldIndex]).contains(field.getText())) {
                                        results.add(new SearchResult(lessonIndex, elementType, groupIndex, elementIndex, ((FieldRetrievable) Main.main.lessons.get(lessonIndex).getElementGroups(elementType).get(groupIndex).get(elementIndex)).getField(Main.main.lessons.get(lessonIndex).getElementGroups(elementType).getInstance().getFieldNames()[fieldIndex])));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                new SearchResultFrame(field.getText(), results);
                typeDialog.dispose();
            }
        }, SwingUtils.makeConstraints(0 + "" + 0 + "1110"));

        panel.add(new ImageButton("cancel.png", "Cancel", 16, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                typeDialog.dispose();
            }
        }, SwingUtils.makeConstraints(1 + "" + 0 + "1110"));
        typeDialog.add(panel, SwingUtils.makeConstraints(0 + "" + 1 + "2110"));
        typeDialog.setLocation(MouseInfo.getPointerInfo().getLocation());
        typeDialog.pack();
        typeDialog.setVisible(true);
    }

    public static void typeDialog(final ElementGroupsPanel elementGroupsPanel) {
        int numColumns = 2;
        final JDialog typeDialog = new JDialog();
        typeDialog.setTitle("Group Type Setting");
        typeDialog.setLayout(new GridBagLayout());
        final Vector<JCheckBox> checkBoxes = new Vector<JCheckBox>();
        if (elementGroupsPanel.getElementGroups().getInstance().getType() == Element.QUESTION) {
            String[] fieldNames = new String[]{"Title", "Picture", "Question", "Answer", "Question Audio", "Answer Audio"};
            for (String s : fieldNames) checkBoxes.add(new JCheckBox(Config.decorate(s)));
            String binaryType = Integer.toBinaryString(elementGroupsPanel.getElementGroupPanel().getElementGroup().getMemberType());
            while (binaryType.length() < checkBoxes.size()) binaryType = "0" + binaryType;
            for (int c = 0; c < checkBoxes.size(); c++) checkBoxes.get(c).setSelected(binaryType.charAt(c) == '0');
        } else if (elementGroupsPanel.getElementGroups().getInstance().getType() == Element.DIALOG) {
            ButtonGroup group = new ButtonGroup();
            String[] fieldNames = new String[]{"Dialogue", "Monologue"};
            for (int i = 0; i < fieldNames.length; i++) {
                JCheckBox checkBox = new JCheckBox(Config.decorate(fieldNames[i]));
                checkBox.setSelected(elementGroupsPanel.getElementGroupPanel().getElementGroup().getMemberType() == i);
                checkBoxes.add(checkBox);
                group.add(checkBox);
            }
        }
        if (checkBoxes.isEmpty()) return;

        for (int c = 0; c < checkBoxes.size(); c++) {
            String hint = c % numColumns + "" + c / numColumns + "1110";
            typeDialog.add(checkBoxes.get(c), SwingUtils.makeConstraints(hint));
        }

        typeDialog.add(new ImageButton("ok.png", "OK", 16, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                StringBuffer buffer = new StringBuffer();
                int type = -1;
                if (elementGroupsPanel.getElementGroups().getInstance().getType() == Element.QUESTION) {
                    for (int c = 0; c < checkBoxes.size(); c++)
                        buffer.append(checkBoxes.get(c).isSelected() ? 0 : 1);
                    type = Integer.parseInt(buffer.toString(), 2);
                } else if (elementGroupsPanel.getElementGroups().getInstance().getType() == Element.DIALOG) {
                    type = checkBoxes.get(0).isSelected() ? 0 : 1;
                }
                elementGroupsPanel.getElementGroupPanel().getElementGroup().setMemberType(type);
                elementGroupsPanel.getElementGroupPanel().refresh();
                typeDialog.dispose();
            }
        }, SwingUtils.makeConstraints(0 + "" + checkBoxes.size() / numColumns + 1 + "1110"));

        typeDialog.add(new ImageButton("cancel.png", "Cancel", 16, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                typeDialog.dispose();
            }
        }, SwingUtils.makeConstraints(1 + "" + checkBoxes.size() / numColumns + 1 + "1110"));

        typeDialog.setLocation(MouseInfo.getPointerInfo().getLocation());
        typeDialog.pack();
        typeDialog.setVisible(true);
    }

    public static void error(String message, String location) {
        if (message.trim().isEmpty()) return;
        JOptionPane.showMessageDialog(null, "<html><body><p style='width: 250px;'>" + message + "</p></body></html>", "Error: " + location, JOptionPane.ERROR_MESSAGE);
    }

    public static void error(ArrayList<String> messages, String location) {
        if (messages.size() == 0) return;
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><body>");
        for (int m = 0; m < messages.size(); m++) {
            buffer.append("<p style='width: 250px;'>");
            buffer.append(messages.get(m));
            buffer.append("</p>");
        }
        buffer.append("</body></html>");
        JOptionPane.showMessageDialog(null, buffer.toString(), "Error: " + location, JOptionPane.ERROR_MESSAGE);
    }

}
