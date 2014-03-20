package view.detail;

import model.Element;
import util.Config;
import util.SwingUtils;
import view.components.*;
import view.panel.ElementGroupPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 09/01/2014
 * Time: 13:31
 */
public class DefaultDetailPanel<T extends Element> extends DetailPanel<T> {
    Vector<JLabel> labels;// = new Vector<JLabel>();
    Vector<ElementComponent> textFields;// = new Vector<JComponent>();

    public DefaultDetailPanel(final ElementGroupPanel panel) {
        super(panel);
    }

    @Override
    void addComponents() {
        labels = new Vector<JLabel>();
        textFields = new Vector<ElementComponent>();
        String[] fieldNames = element.getFieldNames();
        for (int f = 0; f < fieldNames.length; f++) {
            JLabel label = new JLabel(Config.decorate(fieldNames[f]), SwingConstants.LEFT);
            add(label, SwingUtils.makeConstraints("0," + f + ",1,1,0,0"));
            labels.add(label);
            if (fieldNames[f].contains("wordClass")) {
                String[] wordClasses = {
                        "noun",
                        "verb",
                        "pronoun",
                        "adj",
                        "adv",
                        "conj",
                        "v/n.m.",
                        "prep",
                        "phrase",
                        "aspect marker",
                        "card"
                };
                ElementJComboBox wordClassList = new ElementJComboBox(fieldNames[f], element, wordClasses);
                wordClassList.setEditable(true);
                wordClassList.setBorder(BorderFactory.createEmptyBorder());
                add(wordClassList, SwingUtils.makeConstraints("1," + f + ",3,1,1,0"));
                textFields.add(wordClassList);
            } else if (fieldNames[f].contains("gender")) {
                ElementJComboBox comboBox = new ElementJComboBox(fieldNames[f], element, new String[]{"m", "f"});
                comboBox.setEditable(true);
                comboBox.setBorder(BorderFactory.createEmptyBorder());
                add(comboBox, SwingUtils.makeConstraints("1," + f + ",3,1,1,0"));
                textFields.add(comboBox);
            } else if (fieldNames[f].contains("linkType")) {
                ElementJComboBox comboBox = new ElementJComboBox(fieldNames[f], element, new String[]{"local", "remote"});
                comboBox.setEditable(true);
                comboBox.setBorder(BorderFactory.createEmptyBorder());
                add(comboBox, SwingUtils.makeConstraints("1," + f + ",3,1,1,0"));
                textFields.add(comboBox);
            } else if (fieldNames[f].contains("audio")) {
                final ElementTextField textField = new ElementTextField(fieldNames[f], element);
                add(textField, SwingUtils.makeConstraints("1," + f + ",1,1,1,0"));
                textFields.add(textField);
                final int finalF = f;
                ImageButton findButton = new ImageButton("find.png", 20) {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        String fileName = SwingDialog.audioDialog();
                        if (fileName != null) textField.setText(fileName);
                    }
                };
                findButton.setFocusable(false);
                add(findButton, SwingUtils.makeConstraints("2," + f + ",1,1,0,0"));
                ImageButton playButton = new ImageButton("play.png", 20) {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            new MP3(((ElementTextField) textFields.get(finalF)).getText()).play();
                        } catch (IOException e) {
                            SwingDialog.error(e.getMessage(), "Play Audio");
                        }
                    }
                };
                playButton.setFocusable(false);
                add(playButton, SwingUtils.makeConstraints("3," + f + ",1,1,0,0"));
            } else if (fieldNames[f].equals("url")) {
                final ElementTextField textField = new ElementTextField(fieldNames[f], element);
                add(textField, SwingUtils.makeConstraints("1," + f + ",2,1,1,0"));
                textFields.add(textField);
                ImageButton findButton = new ImageButton("find.png", 20) {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        String fileName = SwingDialog.htmlDialog();
                        if (fileName != null) textField.setText(fileName);
                    }
                };
                findButton.setFocusable(false);
                add(findButton, SwingUtils.makeConstraints("3," + f + ",1,1,0,0"));
            }else if (fieldNames[f].equals("title")) {
                final ElementTextField textField = new ElementTextField(fieldNames[f], element);
                add(textField, SwingUtils.makeConstraints("1," + f + ",3,1,1,0"));
                textFields.add(textField);
            } else {
                ElementTextArea textField = new ElementTextArea(fieldNames[f], element, 5);
                add(textField, SwingUtils.makeConstraints("1," + f + ",3,1,1,1"));
                textFields.add(textField);
            }
        }
    }

    @Override
    void updateComponents() {
        for (int i = 0; i < textFields.size(); i++) textFields.get(i).setModel(element);
    }
}
