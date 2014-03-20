package view.detail;

import model.Element;
import util.SwingUtils;
import view.panel.ElementGroupPanel;
import view.panel.MyPanel;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 09/01/2014
 * Time: 13:33
 */
public abstract class DetailPanel<T extends Element> extends MyPanel {
    protected T element;
    protected ElementGroupPanel<T> elementGroupPanel;

    public DetailPanel(ElementGroupPanel<T> panel) {
        super();
        elementGroupPanel = panel;
    }

    public void setElement(T element) {
        this.element = element;
    }

    @Override
    public void refresh() {
//        Logging.info("Refreshing Detail Panel");
        if (element == null) {
            removeAll();
            addBlank();
        } else if (getComponents().length > 1) updateComponents();
        else {
            removeAll();
            addComponents();
        }
        elementGroupPanel.navigationPanel.setVisible(true);
        SwingUtils.disableTabbingInTextAreas(this);
        revalidate();
        repaint();
    }

    void addBlank() {
        JLabel label = new JLabel("No Record");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, SwingUtils.makeConstraints("001111"));
    }

    abstract void addComponents();

    abstract void updateComponents();

    public T getElement() {
        return element;
    }
}
