package view.panel;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 06/03/2014
 * Time: 11:11
 */
public abstract class MyPanel extends JPanel {

    public MyPanel() {
        super(new GridBagLayout());
    }

    public abstract void refresh();
}
