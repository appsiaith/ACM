package view.frame;

import model.Element;
import model.Lesson;
import util.Config;
import util.SwingUtils;
import view.Main;
import view.components.ElementTextField;
import view.panel.ElementGroupsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 04/11/2013
 * Time: 14:59
 */
public class LessonFrame extends MyFrame {
    protected JTabbedPane tabs;
    public Lesson lesson;

    public LessonFrame(final Lesson lesson) {
        super("App Content Manager");

        /*JMenuBar menuBar = new JMenuBar();
        menuBar.add(grammarText.getEditMenu());
        menuBar.add(grammarText.getFormatMenu());
        menuBar.add(grammarText.getInsertMenu());*/

        this.lesson = lesson;
        setTitle(Config.LESSON_NAME + " " + (Main.getOrdering(lesson, Main.main.lessons) + 1) + ": " + (lesson.getEnglish().isEmpty() ? "No title" : lesson.getEnglish()));

        tabs = new JTabbedPane();
        tabs.addTab(Config.PATTERN_NAME, new ElementGroupsPanel(this, lesson.getPatterns()));
        tabs.addTab(Config.QUESTION_NAME, new ElementGroupsPanel(this, lesson.getQuestions()));
        tabs.addTab(Config.VOCABULARY_NAME, new ElementGroupsPanel(this, lesson.getVocabularies()));
        tabs.addTab(Config.GRAMMAR_NAME, new ElementGroupsPanel(this, lesson.getGrammars()));
        tabs.addTab(Config.DIALOG_NAME, new ElementGroupsPanel(this, lesson.getDialogues()));

        tabs.setSelectedIndex(Element.PATTERN);
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        buttonPanel.add(new JLabel(Config.ENGLISH_NAME), SwingUtils.makeConstraints("001100"));
        final ElementTextField english = new ElementTextField("english", lesson) {
            public void propagateChange() {
                super.propagateChange();
                Main.main.lessonList.refresh();
                setTitle(Config.LESSON_NAME + " " + (Main.getOrdering(lesson, Main.main.lessons) + 1) + ": " + (lesson.getEnglish().isEmpty() ? "No title" : lesson.getEnglish()));
            }
        };
        buttonPanel.add(english, SwingUtils.makeConstraints("101110"));

        buttonPanel.add(new JLabel(Config.SOUTH_NAME), SwingUtils.makeConstraints("011100"));
        final ElementTextField south = new ElementTextField("south", lesson);
        buttonPanel.add(south, SwingUtils.makeConstraints("111110"));

        buttonPanel.add(new JLabel(Config.NORTH_NAME), SwingUtils.makeConstraints("021100"));
        final ElementTextField north = new ElementTextField("north", lesson);
        buttonPanel.add(north, SwingUtils.makeConstraints("121110"));

        add(buttonPanel, SwingUtils.makeConstraints("001110"));
        add(tabs, SwingUtils.makeConstraints("011111"));

        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getPreferredSize().width / 2 + (new Random()).nextInt(50), screenSize.height / 2 - getPreferredSize().height / 2 + (new Random()).nextInt(50));
        SwingUtils.disableTabbingInTextAreas(this);
        setVisible(true);
    }

    public void refresh() {
        int selectedTabIndex = tabs.getSelectedIndex() < 0 ? 0 : tabs.getSelectedIndex();
        setTitle(Config.LESSON_NAME + " " + (Main.getOrdering(lesson, Main.main.lessons) + 1) + ": " + (lesson.getEnglish().isEmpty() ? "No title" : lesson.getEnglish()));
        for (int e = 0; e < Element.ELEMENT_TYPES.length; e++) {
            tabs.setTitleAt(e, Config.getElementNames()[e]);
            ((ElementGroupsPanel) tabs.getComponentAt(e)).refresh();
        }
        pack();
        repaint();
        tabs.setSelectedIndex(selectedTabIndex);
    }

    public void setSelectedTab(int elementType) {
        if (elementType < 0 || elementType > Element.DIALOG) elementType = 0;
        tabs.setSelectedIndex(elementType);
    }

    public ElementGroupsPanel getElementGroupsPanel() {
        return (ElementGroupsPanel) tabs.getSelectedComponent();
    }

    public Lesson getLesson() {
        return lesson;
    }

    @Override
    protected boolean exitAction(KeyEvent e) {
        if (e == null || (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_W && (e.getModifiersEx() == KeyEvent.META_DOWN_MASK | e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK))) {
            Main.main.removeLessonFrame(lesson.getId());
            dispose();
            Main.main.requestFocus();
            System.gc();
            return true;
        }
        return false;
    }

    @Override
    protected boolean hotkeyAction(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_LEFT && (e.getModifiersEx() == KeyEvent.META_DOWN_MASK | e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK)) {
            getElementGroupsPanel().getElementGroupPanel().prevAction();
            return true;
        }
        if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_RIGHT && (e.getModifiersEx() == KeyEvent.META_DOWN_MASK | e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK)) {
            getElementGroupsPanel().getElementGroupPanel().nextAction();
            return true;
        }
        if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_EQUALS && (e.getModifiersEx() == KeyEvent.META_DOWN_MASK | e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK)) {
            getElementGroupsPanel().getElementGroupPanel().plusAction();
            return true;
        }
        if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_MINUS && (e.getModifiersEx() == KeyEvent.META_DOWN_MASK | e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK)) {
            getElementGroupsPanel().getElementGroupPanel().minusAction();
            return true;
        }
        return false;
    }

    public Integer getSelectedTabIndex() {
        return tabs.getSelectedIndex();
    }
}
