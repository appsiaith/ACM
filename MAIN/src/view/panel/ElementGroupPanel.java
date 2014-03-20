package view.panel;

import model.Element;
import model.ElementGroup;
import util.Config;
import util.Logging;
import util.ProgramSettings;
import util.SwingUtils;
import view.Main;
import view.components.ElementTextField;
import view.components.ImageButton;
import view.detail.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 06/03/2014
 * Time: 09:22
 */
public class ElementGroupPanel<T extends Element> extends MyPanel {
    private Vector<JComponent> components = new Vector<JComponent>();
    private JLabel indexLabel = new JLabel();
    private volatile ElementGroup<T> elementGroup;
    private ElementTextField[] textFields = new ElementTextField[3];

    protected DetailPanel detailPanel;
    protected TablePanel tablePanel;
    protected JTabbedPane tabs = new JTabbedPane();
    protected ElementGroupsPanel elementGroupsPanel;
    public JPanel navigationPanel;

    public ElementGroupPanel(ElementGroupsPanel elementGroupsPanel) {
        super();
        this.elementGroupsPanel = elementGroupsPanel;
        add(makeTextPanel(), SwingUtils.makeConstraints("001110"));
        add(makeEditPanel(), SwingUtils.makeConstraints("011110"));
        switch (elementGroupsPanel.elementGroups.getInstance().getType()) {
            case Element.DIALOG:
                detailPanel = new MonologueDetailPanel(this);
                break;
            case Element.GRAMMAR:
                detailPanel = new GrammarDetailPanel(this);
                break;
            case Element.QUESTION:
                detailPanel = new QuestionDetailPanel(this);
                break;
            default:
                detailPanel = new DefaultDetailPanel(this);
                break;
        }
        tabs.addTab("Record View", detailPanel);
        components.add(detailPanel);

        tablePanel = new TablePanel(this);
        tabs.addTab("Table View", tablePanel);
        components.add(tablePanel);

        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if (tablePanel.table.isEditing()) tablePanel.table.getCellEditor().stopCellEditing();
                refresh();
            }
        });

        GridBagConstraints constraints = SwingUtils.makeConstraints("021111");
        constraints.insets = new Insets(0, 0, 0, 0);
        add(tabs, constraints);
        components.add(tabs);
//        tabs.setMinimumSize(new Dimension(ProgramSettings.DETAIL_PANEL_WIDTH, ProgramSettings.DETAIL_PANEL_HEIGHT));
        navigationPanel = makeNavigationPanel();
        add(navigationPanel, SwingUtils.makeConstraints("031110"));
        firstAction();
    }

    private JPanel makeTextPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel englishLabel = new JLabel(Config.ENGLISH_NAME);
        panel.add(englishLabel, SwingUtils.makeConstraints("021100"));
        components.add(englishLabel);
        ElementTextField english = new ElementTextField("english", elementGroup);
        panel.add(english, SwingUtils.makeConstraints("122110"));
        components.add(english);
        textFields[0] = english;
        JLabel southLabel = new JLabel(Config.SOUTH_NAME);
        panel.add(southLabel, SwingUtils.makeConstraints("031100"));
        components.add(southLabel);
        ElementTextField south = new ElementTextField("south", elementGroup);
        panel.add(south, SwingUtils.makeConstraints("132110"));
        components.add(south);
        textFields[1] = south;
        JLabel northLabel = new JLabel(Config.NORTH_NAME);
        panel.add(northLabel, SwingUtils.makeConstraints("041100"));
        components.add(northLabel);
        ElementTextField north = new ElementTextField("north", elementGroup);
        panel.add(north, SwingUtils.makeConstraints("142110"));
        components.add(north);
        textFields[2] = north;
        return panel;
    }

    private JPanel makeEditPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        ImageButton plusButton = new ImageButton("plus.png", "New Entry", 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                plusAction();
            }
        };
        plusButton.setFocusable(false);
        plusButton.setPreferredSize(new Dimension(plusButton.getPreferredSize().width, ProgramSettings.BUTTON_HEIGHT));
        panel.add(plusButton, SwingUtils.makeConstraints("001110"));
        components.add(plusButton);
        ImageButton minusButton = new ImageButton("minus.png", "Remove Entry", 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                minusAction();
            }
        };
        minusButton.setFocusable(false);
        minusButton.setPreferredSize(new Dimension(minusButton.getPreferredSize().width, ProgramSettings.BUTTON_HEIGHT));
        panel.add(minusButton, SwingUtils.makeConstraints("101110"));
        components.add(minusButton);
        return panel;
    }

    private JPanel makeNavigationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        ImageButton firstButton = new ImageButton("first.png", "First", 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                firstAction();
            }
        };
        firstButton.setFocusable(false);
        firstButton.setPreferredSize(new Dimension(100, ProgramSettings.BUTTON_HEIGHT));
        panel.add(firstButton, SwingUtils.makeConstraints("0,3,1,1,0,0"));
        components.add(firstButton);
        ImageButton previousButton = new ImageButton("prev.png", "Prev", 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                prevAction();
            }
        };
        previousButton.setFocusable(false);
        previousButton.setPreferredSize(new Dimension(100, ProgramSettings.BUTTON_HEIGHT));
        panel.add(previousButton, SwingUtils.makeConstraints("1,3,1,1,0,0"));
        components.add(previousButton);

        updateLabel();
        indexLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(indexLabel, SwingUtils.makeConstraints("2,3,1,1,1,0"));
        components.add(indexLabel);

        ImageButton nextButton = new ImageButton("next.png", "Next", 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                nextAction();
            }
        };
        nextButton.setFocusable(false);
        nextButton.setPreferredSize(new Dimension(100, ProgramSettings.BUTTON_HEIGHT));
        panel.add(nextButton, SwingUtils.makeConstraints("3,3,1,1,0,0"));
        components.add(nextButton);
        ImageButton lastButton = new ImageButton("last.png", "Last", 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                lastAction();
            }
        };
        lastButton.setFocusable(false);
        lastButton.setPreferredSize(new Dimension(100, ProgramSettings.BUTTON_HEIGHT));
        panel.add(lastButton, SwingUtils.makeConstraints("4,3,1,1,0,0"));
        components.add(lastButton);
        return panel;
    }

    public void updateLabel() {
        if (elementGroup == null || elementGroup.isEmpty()) indexLabel.setText("Empty Group");
        else
            indexLabel.setText((Main.getOrdering(detailPanel.getElement(), elementGroup) + 1) + " of "
                    + elementGroup.size());
    }

    public void firstAction() {
        if (elementGroup == null || elementGroup.isEmpty()) detailPanel.setElement(null);
        else detailPanel.setElement(elementGroup.get(0));
        refresh();
    }

    public void lastAction() {
        if (elementGroup == null || elementGroup.isEmpty()) detailPanel.setElement(null);
        else detailPanel.setElement(elementGroup.get(elementGroup.size() - 1));
        refresh();
    }

    public void prevAction() {
        if (elementGroup == null || elementGroup.isEmpty()) detailPanel.setElement(null);
        else {
            int index = Main.getOrdering(detailPanel.getElement(), elementGroup);
            if (--index < 0) index = elementGroup.size() - 1;
            detailPanel.setElement(elementGroup.get(index));
        }
        refresh();
    }

    public void nextAction() {
        if (elementGroup == null || elementGroup.isEmpty()) detailPanel.setElement(null);
        else {
            int index = Main.getOrdering(detailPanel.getElement(), elementGroup);
            if (++index > elementGroup.size() - 1) index = 0;
            detailPanel.setElement(elementGroup.get(index));
        }
        refresh();
    }

    public void plusAction() {
        if (elementGroup == null) return;
        if (tabs.getSelectedIndex() == 0) {
            elementGroup.addRaw(Main.getOrdering(detailPanel.getElement(), elementGroup) + 1);
            nextAction();
            refresh();
        } else {
            int index = tablePanel.table.getSelectedRows().length > 0 ?
                    tablePanel.table.getSelectedRows()[tablePanel.table.getSelectedRows().length - 1] : elementGroup.size() - 1;
            elementGroup.addRaw(index + 1);
            refresh();
            tablePanel.setSelectedRows(index + 1, index + 1);
        }
    }

    public void minusAction() {
        if (elementGroup == null || elementGroup.isEmpty()) return;
        if (tabs.getSelectedIndex() == 0) {
            int index = Main.getOrdering(detailPanel.getElement(), elementGroup);
            elementGroup.remove(index);
            prevAction();
            refresh();
        } else {
            int[] selectedRows = tablePanel.table.getSelectedRows();
            if (selectedRows.length <= 0) return;
            for (int i = 0; i < selectedRows.length; i++) {
                Main.main.database.scheduleDeletion(elementGroup.get(selectedRows[i] - i));
                elementGroup.remove(selectedRows[i] - i);
            }
            int index = selectedRows[0] - 1 < 0 ? 0 : selectedRows[0] - 1;
            refresh();
            tablePanel.setSelectedRows(index, index);
        }
    }

    public void setElementGroup(ElementGroup group) {
        if (tablePanel.table.isEditing()) tablePanel.table.getCellEditor().stopCellEditing();
        elementGroup = group;
        textFields[0].setModel(elementGroup);
        textFields[1].setModel(elementGroup);
        textFields[2].setModel(elementGroup);
        for (int c = 0; c < components.size(); c++) components.get(c).setEnabled(elementGroup != null);
        detailPanel.setEnabled(elementGroup != null);
        tablePanel.setEnabled(elementGroup != null);
        firstAction();
    }

    public void refresh() {
        updateLabel();
        ((MyPanel) tabs.getSelectedComponent()).refresh();
        if (detailPanel.getElement() == null && elementGroup != null && !elementGroup.isEmpty())
        {
            Logging.info("Forced selection of first element for being NULL.");
            firstAction();
        }
        revalidate();
        repaint();
    }

    public ElementGroup<T> getElementGroup() {
        return elementGroup;
    }

    public TablePanel getTablePanel() {
        return tablePanel;
    }

    public ElementGroupsPanel getElementGroupsPanel() {
        return elementGroupsPanel;
    }

    public void setSelectedRecord(int elementIndex) {
        if (elementGroup == null || elementGroup.isEmpty()) detailPanel.setElement(null);
        else detailPanel.setElement(elementGroup.get(elementIndex));
        refresh();
    }
}
