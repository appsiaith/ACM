package view.panel;

import util.Config;
import util.ProgramSettings;
import util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 14/03/2014
 * Time: 11:52
 */
public class HeaderPanel extends MyPanel {
    final Vector<DefaultComboBoxModel> modelGroup = new Vector<DefaultComboBoxModel>();
    Vector<JLabel> labelGroup = new Vector<JLabel>();
    Vector<JComboBox> comboGroup = new Vector<JComboBox>();

    public HeaderPanel(Vector<String> sourceHeaders, String[] fieldNames, int numColumns) {
        super();
        for (int s = 0; s < sourceHeaders.size(); s++) {
            JLabel label = new JLabel(sourceHeaders.get(s));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(120, 20));
            labelGroup.add(label);
            add(label, SwingUtils.makeConstraints(s % numColumns + "" + (s / numColumns * 2 + 1) + "1110"));
            final DefaultComboBoxModel model = new DefaultComboBoxModel();
            for (int f = 0; f < fieldNames.length; f++) model.addElement(fieldNames[f]);
            model.addElement("grouping");
            model.addElement("ordering");
            model.addElement(Config.LESSON_NAME.toLowerCase() + "Id");
            model.addElement(null);
            final JComboBox comboBox = new JComboBox(model);
            comboBox.setSelectedItem(null);
            comboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED)
                        for (int i = 0; i < modelGroup.size(); i++) {
                            if (!modelGroup.get(i).equals(model)) modelGroup.get(i).removeElement(e.getItem());
                        }
                    else if (e.getStateChange() == ItemEvent.DESELECTED) {
                        for (int i = 0; i < modelGroup.size(); i++) {
                            /*if (!modelGroup.get(i).equals(model)) {
                                modelGroup.get(i).removeElement(e.getItem());
                                modelGroup.get(i).addElement((String) e.getItem());
                            }*/
                            if (!modelGroup.get(i).equals(model) && modelGroup.get(i).getIndexOf(e.getItem()) < 0) {
//                                modelGroup.get(i).removeElement(e.getItem());
                                modelGroup.get(i).addElement((String) e.getItem());
                            }
                        }
                    }
                }
            });
            comboBox.setMaximumRowCount(comboBox.getModel().getSize());
            comboBox.setPreferredSize(new Dimension(120, 20));
            comboGroup.add(comboBox);
            modelGroup.add(model);
            add(comboBox, SwingUtils.makeConstraints(s % numColumns + "" + (s / numColumns * 2 + 2) + "1110"));
        }
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

    public void reset(Vector<String> sourceHeaders, String[] fieldNames, int numColumns) {
        removeAll();
        modelGroup.clear();
        labelGroup.clear();
        comboGroup.clear();
        for (int s = 0; s < sourceHeaders.size(); s++) {
            JLabel label = new JLabel(sourceHeaders.get(s));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(120, 20));
            labelGroup.add(label);
            add(label, SwingUtils.makeConstraints(s % numColumns + "" + (s / numColumns * 2 + 1) + "1110"));
            final DefaultComboBoxModel model = new DefaultComboBoxModel();
            for (int f = 0; f < fieldNames.length; f++) model.addElement(fieldNames[f]);
            model.addElement("grouping");
            model.addElement("ordering");
            model.addElement(null);
            final JComboBox comboBox = new JComboBox(model);
            comboBox.setSelectedItem(null);
            comboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED)
                        for (int i = 0; i < modelGroup.size(); i++) {
                            if (!modelGroup.get(i).equals(model)) modelGroup.get(i).removeElement(e.getItem());
                        }
                    else if (e.getStateChange() == ItemEvent.DESELECTED) {
                        for (int i = 0; i < modelGroup.size(); i++) {
                            if (!modelGroup.get(i).equals(model) && modelGroup.get(i).getIndexOf(e.getItem()) < 0) {
//                                modelGroup.get(i).removeElement(e.getItem());
                                modelGroup.get(i).addElement((String) e.getItem());
                            }
                        }
                    }
                }
            });
            comboBox.setMaximumRowCount(comboBox.getModel().getSize());
            comboBox.setPreferredSize(new Dimension(120, 20));
            comboGroup.add(comboBox);
            modelGroup.add(model);
            add(comboBox, SwingUtils.makeConstraints(s % numColumns + "" + (s / numColumns * 2 + 2) + "1110"));
        }
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

    public HeaderPanel() {
        super();
    }

    public Vector<String> getSourceFieldNames() {
        Vector<String> fieldNames = new Vector<String>();
        for (int c = 0; c < comboGroup.size(); c++) fieldNames.add((String) comboGroup.get(c).getSelectedItem());
        return fieldNames;
    }

    @Override
    public void refresh() {

    }
}