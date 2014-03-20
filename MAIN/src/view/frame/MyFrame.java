package view.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 03/03/2014
 * Time: 12:42
 */
public abstract class MyFrame extends JFrame {
    private boolean focused = true;

    public MyFrame(String title) {
        super(title);
        setLayout(new GridBagLayout());
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                focused = true;
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                focused = false;
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitAction(null);
            }
        });
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (focused) {
                    if (!hotkeyAction(e))
                        return exitAction(e);
                }
                return false;
            }
        });
    }

    protected abstract void refresh();

    protected abstract boolean exitAction(KeyEvent e);

    protected abstract boolean hotkeyAction(KeyEvent e);
}
