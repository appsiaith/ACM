package view.components;

import model.FieldRetrievable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 04/12/2013
 * Time: 15:10
 */
public class ElementTextArea extends JTextArea implements DocumentListener, FocusListener, ElementComponent {
    private String fieldName;
    private FieldRetrievable model;

    public ElementTextArea(String fieldName, FieldRetrievable model, int numRows) {
        super();
        setBorder(BorderFactory.createLineBorder(Color.lightGray));
        numRows = numRows > 2 ? 2 : numRows;
        if (numRows > 1) setRows(numRows);
        setLineWrap(true);
        setWrapStyleWord(true);
        this.fieldName = fieldName;
        ((AbstractDocument) getDocument()).setDocumentFilter(AccentFilter.filter);
        setModel(model);
        addFocusListener(this);
    }

    public void setModel(FieldRetrievable model) {
        ((AccentFilter) ((AbstractDocument) getDocument()).getDocumentFilter()).setEnabled(false);
        getDocument().removeDocumentListener(this);
        this.model = model;
        if (model == null) setText("");
        else setText(model.getField(fieldName));
        getDocument().addDocumentListener(this);
        ((AccentFilter) ((AbstractDocument) getDocument()).getDocumentFilter()).setEnabled(true);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        propagateChange();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        propagateChange();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        propagateChange();
    }

    public void propagateChange() {
        model.setField(fieldName, getText());
    }

    @Override
    public void focusGained(FocusEvent e) {
        setBorder(BorderFactory.createLineBorder(Color.darkGray));
    }

    @Override
    public void focusLost(FocusEvent e) {
        setBorder(BorderFactory.createLineBorder(Color.lightGray));
    }
}
