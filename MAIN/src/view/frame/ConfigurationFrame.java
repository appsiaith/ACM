package view.frame;

import util.SwingUtils;
import view.Main;
import view.panel.AppearancePanel;
import view.panel.ConfigPanel;
import view.panel.ElementGroupsPanel;
import view.panel.MenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 25/02/2014
 * Time: 16:52
 */
public class ConfigurationFrame extends MyFrame {

    public ConfigurationFrame() {
        super("Configuration");
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Useful Information", new ElementGroupsPanel(null, Main.main.abouts));
        tabs.addTab("Appearance", new AppearancePanel());
        tabs.addTab("Menu Items", new MenuPanel());
        tabs.addTab("Program Settings", new ConfigPanel());
        add(tabs, SwingUtils.makeConstraints("001111"));
        pack();
        Dimension dimension = getPreferredSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (screenSize.getWidth() / 2 - dimension.getWidth() / 2), (int) (screenSize.getHeight() / 2 - dimension.getHeight() / 2));
        setVisible(true);
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
}
