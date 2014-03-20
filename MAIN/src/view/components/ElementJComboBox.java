package view.components;

import model.FieldRetrievable;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 04/12/2013
 * Time: 15:10
 */
public class ElementJComboBox extends JComboBox implements ItemListener, ElementComponent {
    private String fieldName;
    private FieldRetrievable model;

    public ElementJComboBox(String fieldName, FieldRetrievable model, String[] options) {
        super(options);
        setEditable(true);
        this.fieldName = fieldName;
        if (model != null) setModel(model);
    }

    public void setModel(FieldRetrievable model) {
        removeItemListener(this);
        this.model = model;
        setSelectedItem(model.getField(fieldName));
        addItemListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (model == null) return;
        model.setField(fieldName, (String) getSelectedItem());
    }
}
