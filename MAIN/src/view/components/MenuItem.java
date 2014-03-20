package view.components;

import util.ProgramSettings;
import util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 28/02/2014
 * Time: 16:02
 */
public abstract class MenuItem extends JMenuItem implements ActionListener {
    public MenuItem(String text, String imageFile, int accelerator, String description) {
        super(text);
        //a accentButtonGroup of JMenuItems
        Image image = null;
        try {
            image = SwingUtils.resize(SwingUtils.class.getClass().getResource(ProgramSettings.IMG_ROOT + imageFile), 16, 16);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (accelerator != -1) setMnemonic(accelerator);
        setIcon(new ImageIcon(image));
        if (accelerator != -1) setAccelerator(KeyStroke.getKeyStroke(accelerator, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        getAccessibleContext().setAccessibleDescription(description);
        addActionListener(this);
    }

    public MenuItem(String text) {
        super(text);
        addActionListener(this);
    }
}
