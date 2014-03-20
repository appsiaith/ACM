package view.panel;

import util.SwingUtils;
import view.components.AccentFilter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 27/02/2014
 * Time: 17:05
 */
public class MenuPanel extends JPanel {

    private Vector<JTextField> menuItems = new Vector<JTextField>();
    private Vector<JTextField> subMenuItems = new Vector<JTextField>();

    public MenuPanel() {
        super(new GridBagLayout());
        int row = 0;

        for (int m = 1; m <= 5; m++) {
            row++;
            add(new JLabel("Main Menu " + (m)), SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
            JTextField field = new ConfigField("MAIN_" + m);
            menuItems.add(field);
            add(field, SwingUtils.makeConstraints("1," + row + ",1,1,1,1"));
            row++;
            add(new JLabel("Main Menu Sub " + (m)), SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
            JTextField fieldSub = new ConfigField("MAIN_SUB_" + m);
            subMenuItems.add(fieldSub);
            add(fieldSub, SwingUtils.makeConstraints("1," + row + ",1,1,1,1"));
        }

        row++;
        add(new JSeparator(JSeparator.HORIZONTAL), SwingUtils.makeConstraints("0," + row + ",2,1,1,0"));

        for (int m = 1; m <= 5; m++) {
            row++;
            add(new JLabel("Unit Menu " + (m)), SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
            JTextField field = new ConfigField("UNIT_" + m);
            menuItems.add(field);
            add(field, SwingUtils.makeConstraints("1," + row + ",1,1,1,1"));
            row++;
            add(new JLabel("Unit Menu Sub " + (m)), SwingUtils.makeConstraints("0," + row + ",1,1,0,0"));
            JTextField fieldSub = new ConfigField("UNIT_SUB_" + m);
            subMenuItems.add(fieldSub);
            add(fieldSub, SwingUtils.makeConstraints("1," + row + ",1,1,1,1"));
        }
    }

    class ConfigField extends JTextField implements FocusListener, DocumentListener {
        String key;

        public ConfigField(String keyInput) {
            super();
            this.key = keyInput;
            setText(util.Menu.getField(key));
            getDocument().addDocumentListener(this);
            setBorder(BorderFactory.createLineBorder(Color.lightGray));
            ((AbstractDocument) getDocument()).setDocumentFilter(AccentFilter.filter);
            addFocusListener(this);
        }

        public void refresh() {
            setText(util.Menu.getField(key));
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            util.Menu.setField(key, getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            util.Menu.setField(key, getText());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            util.Menu.setField(key, getText());
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
}
