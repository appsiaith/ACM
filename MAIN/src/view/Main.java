package view;

import database.Database;
import model.*;
import util.Config;
import util.Logging;
import util.ProgramSettings;
import util.SwingUtils;
import view.components.AccentDialog;
import view.components.ImageButton;
import view.components.MenuItem;
import view.components.SwingDialog;
import view.frame.*;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 01/11/2013
 * Time: 13:40
 */
public class Main extends MyFrame {

    public static Main main;

    public Database database;
    public Vector<Lesson> lessons;
    public ElementGroups<About> abouts;
    protected Hashtable<Integer, LessonFrame> lessonFrames = new Hashtable<Integer, LessonFrame>();

    public LessonList lessonList = new LessonList();
    private Vector<ImageButton> buttonGroup = new Vector<ImageButton>();

    public ImportFrame importFrame;
    public ConfigurationFrame configurationFrame;
    public StatisticFrame statisticFrame;
    public Vector<ImageButton> accentButtonGroup = new Vector<ImageButton>();
    public HashMap<Character, Character> activeAccent = null;

    public MenuItem menuRecent1;
    public MenuItem menuRecent2;
    public MenuItem menuRecent3;

    public static void main(String[] args) throws SQLException, ClassNotFoundException, FileNotFoundException {
        ProgramSettings.loadParameters();
        Logging.info("Enter Application");
        setUIFont(new javax.swing.plaf.FontUIResource(new Font("Tahoma", Font.PLAIN, 16)));
//        UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(0,0,0,0));
//        UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                if (main.database != null) main.database.closeConnection();
                ProgramSettings.saveParameters();
                Logging.info("Exit Application");
            }
        }));
        invokeLater(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                main = new Main();
                main.loadRecent();
                Logging.info("Loaded database in " + (double) (System.currentTimeMillis() - start) / 1000 + " seconds.");
            }
        });
    }

    public Main() {
        super("App Content Manager");
        setJMenuBar(makeMenuBar());
        add(makeButtonPanel(), SwingUtils.makeConstraints("001110"));
        add(makeSecondaryButtonPanel(), SwingUtils.makeConstraints("021110"));
        toggleButtons(false);
        add(new JScrollPane(lessonList), SwingUtils.makeConstraints("031111"));
        pack();
        setPreferredSize(new Dimension(getPreferredSize().width, Toolkit.getDefaultToolkit().getScreenSize().height - 200));
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getPreferredSize().width) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getPreferredSize().height) / 2);
        setVisible(true);
    }

    public class LessonList extends JList implements MouseListener {
        public LessonList() {
            super(new LessonListModel());
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            addMouseListener(this);
            setDragEnabled(true);
            setTransferHandler(new ListTransferHandler());
            setDropMode(DropMode.INSERT);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (isEnabled() && !isSelectionEmpty() && e.getClickCount() == 2)
                makeLessonFrame(lessons.get(getSelectedIndex()));
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        public void refresh() {
            ((LessonListModel) getModel()).updateModel();
        }
    }

    public void refresh() {
        setTitle("App Content Manager: " + Config.PROJECT_NAME);
        lessonList.refresh();
        getRootPane().revalidate();
        repaint();
        Iterator<LessonFrame> iterator = lessonFrames.values().iterator();
        while (iterator.hasNext()) iterator.next().refresh();
    }

    private boolean loadRecent() {
        Logging.info("Loading Most Recent Project");
        ProgramSettings.validateRecent();
        if (ProgramSettings.RECENT1.isEmpty()) return false;
        try {
            load(new Database(ProgramSettings.RECENT1));
            return true;
        } catch (SQLException e) {
            Logging.error("Auto-Open Most Recent Project Failed: " + e.getMessage());
        }
        return false;
    }

    @Override
    protected boolean exitAction(KeyEvent e) {
        if (e != null) return false;
        if (lessons != null && JOptionPane.showConfirmDialog(null, "Save the project before exit?", "Exit Program", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            closeFrames();
            try {
                database.save(this);
            } catch (SQLException e1) {
                SwingDialog.error("Error during saving " + e1.getMessage(), "Save and Exit Project");
            }
            System.exit(0);
            return true;
        }
        System.exit(0);
        return true;
    }

    @Override
    protected boolean hotkeyAction(KeyEvent e) {
        return false;
    }

    private JMenuBar makeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
        menu.add(new MenuItem("New Project", "new.png", KeyEvent.VK_N, "New Project") {
            @Override
            public void actionPerformed(ActionEvent e) {
                newAction();
            }
        });
        menu.add(new MenuItem("Open Project", "open.png", KeyEvent.VK_O, "Open Project") {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAction();
            }
        });
        menu.add(new MenuItem("Save Project", "save.png", KeyEvent.VK_S, "Save Project") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAction();
            }
        });

        JMenu submenu = new JMenu("Recent Projects");
        menuRecent1 = new MenuItem(ProgramSettings.RECENT1.isEmpty() ? "..." : ProgramSettings.RECENT1) {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAction(ProgramSettings.RECENT1);
            }
        };
        submenu.add(menuRecent1);
        menuRecent1.setEnabled(!ProgramSettings.RECENT1.isEmpty());
        menuRecent2 = new MenuItem(ProgramSettings.RECENT2.isEmpty() ? "..." : ProgramSettings.RECENT2) {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAction(ProgramSettings.RECENT2);
            }
        };
        submenu.add(menuRecent2);
        menuRecent2.setEnabled(!ProgramSettings.RECENT2.isEmpty());
        menuRecent3 = new MenuItem(ProgramSettings.RECENT3.isEmpty() ? "..." : ProgramSettings.RECENT3) {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAction(ProgramSettings.RECENT3);
            }
        };
        submenu.add(menuRecent3);
        menuRecent3.setEnabled(!ProgramSettings.RECENT3.isEmpty());
        menu.add(submenu);
        menu.add(new MenuItem("Exit Program") {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitAction(null);
            }
        });

        JMenu menuUtil = new JMenu("Tools");
        menuUtil.setMnemonic(KeyEvent.VK_U);
        menuBar.add(menuUtil);
        menuUtil.add(new MenuItem("Find Records", "find.png", -1, "Display a list of records matching the search term.") {
            @Override
            public void actionPerformed(ActionEvent e) {
                findAction();
            }
        });
        menuUtil.add(new MenuItem("Tidy Up Project", "clean.png", -1, "Remove Empty Records and Record Groups.") {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanAction();
            }
        });
        menuUtil.add(new MenuItem("Import Data", "import.png", KeyEvent.VK_I, "Import Data") {
            @Override
            public void actionPerformed(ActionEvent e) {
                importAction();
            }
        });
        menuUtil.add(new MenuItem("Statistics", "bar.png", -1, "A Summary of the Project") {
            @Override
            public void actionPerformed(ActionEvent e) {
                statsAction();
            }
        });
        menuUtil.add(new MenuItem("Configuration", "config.png", KeyEvent.VK_P, "Configuration") {
            @Override
            public void actionPerformed(ActionEvent e) {
                configAction();
            }
        });
        return menuBar;
    }

    private JPanel makeButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        ImageButton newButton = new ImageButton("new.png", "New", 32, true, new Dimension(72, 62)) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                newAction();
            }
        };
        ImageButton openButton = new ImageButton("open.png", "Open", 32, true, new Dimension(72, 62)) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                openAction();
            }
        };
        buttonGroup.add(new ImageButton("save.png", "Save", 32, true, new Dimension(72, 62)) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (isEnabled()) saveAction();
            }
        });
        buttonGroup.add(new ImageButton("import.png", "Import", 32, true, new Dimension(72, 62)) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (isEnabled()) importAction();
            }
        });
        buttonGroup.add(new ImageButton("config.png", "Config", 32, true, new Dimension(72, 62)) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (isEnabled()) configAction();
            }
        });

        int row = 0;
        buttonPanel.add(newButton, SwingUtils.makeConstraints(row + "01110"));
        row++;
        buttonPanel.add(openButton, SwingUtils.makeConstraints(row + "01110"));
        row++;
        buttonPanel.add(buttonGroup.get(row - 2), SwingUtils.makeConstraints(row + "01110"));
        row++;
        buttonPanel.add(buttonGroup.get(row - 2), SwingUtils.makeConstraints(row + "01110"));
        row++;
        buttonPanel.add(buttonGroup.get(row - 2), SwingUtils.makeConstraints(row + "01110"));

        return buttonPanel;
    }

    private JPanel makeSecondaryButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        ImageButton addButton = new ImageButton("plus.png", "New " + Config.LESSON_NAME, 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addAction();
            }
        };
        addButton.setPreferredSize(new Dimension(addButton.getPreferredSize().width, ProgramSettings.BUTTON_HEIGHT));
        buttonGroup.add(addButton);
        ImageButton deleteButton = new ImageButton("minus.png", "Remove", 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                deleteAction();
            }
        };
        deleteButton.setPreferredSize(new Dimension(addButton.getPreferredSize().width, ProgramSettings.BUTTON_HEIGHT));
        buttonGroup.add(deleteButton);
        buttonPanel.add(addButton, SwingUtils.makeConstraints("001110"));
        buttonPanel.add(deleteButton, SwingUtils.makeConstraints("101110"));
        ImageButton circumflex = new ImageButton("circumflex.png", 32) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ImageButton button = ((ImageButton) evt.getSource());
                if (button.getBackground().equals(Color.white)) {
                    setBackground(Color.gray);
                    toggleOffOthers(evt);
                    activeAccent = AccentDialog.circumflexes;
                } else {
                    setBackground(Color.white);
                    activeAccent = null;
                }
            }
        };
        circumflex.setBackground(Color.white);
        accentButtonGroup.add(circumflex);
        buttonGroup.add(circumflex);
        buttonPanel.add(circumflex, SwingUtils.makeConstraints("301110"));
        ImageButton umlaut = new ImageButton("umlaut.png", 32) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ImageButton button = ((ImageButton) evt.getSource());
                if (button.getBackground().equals(Color.white)) {
                    setBackground(Color.gray);
                    toggleOffOthers(evt);
                    activeAccent = AccentDialog.umlauts;
                } else {
                    setBackground(Color.white);
                    activeAccent = null;
                }
            }
        };
        umlaut.setBackground(Color.white);
        accentButtonGroup.add(umlaut);
        buttonGroup.add(umlaut);
        buttonPanel.add(umlaut, SwingUtils.makeConstraints("401110"));
        ImageButton acute = new ImageButton("acute.png", 32) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ImageButton button = ((ImageButton) evt.getSource());
                if (button.getBackground().equals(Color.white)) {
                    setBackground(Color.gray);
                    toggleOffOthers(evt);
                    activeAccent = AccentDialog.acutes;
                } else {
                    setBackground(Color.white);
                    activeAccent = null;
                }
            }
        };
        acute.setBackground(Color.white);
        accentButtonGroup.add(acute);
        buttonGroup.add(acute);
        buttonPanel.add(acute, SwingUtils.makeConstraints("501110"));
        ImageButton grave = new ImageButton("grave.png", 32) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ImageButton button = ((ImageButton) evt.getSource());
                if (button.getBackground().equals(Color.white)) {
                    setBackground(Color.gray);
                    toggleOffOthers(evt);
                    activeAccent = AccentDialog.graves;
                } else {
                    setBackground(Color.white);
                    activeAccent = null;
                }
            }
        };
        grave.setBackground(Color.white);
        accentButtonGroup.add(grave);
        buttonGroup.add(grave);
        buttonPanel.add(grave, SwingUtils.makeConstraints("601110"));
        ImageButton macron = new ImageButton("macron.png", 32) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ImageButton button = ((ImageButton) evt.getSource());
                if (button.getBackground().equals(Color.white)) {
                    setBackground(Color.gray);
                    toggleOffOthers(evt);
                    activeAccent = AccentDialog.macrons;
                } else {
                    setBackground(Color.white);
                    activeAccent = null;
                }
            }
        };
        macron.setBackground(Color.white);
        accentButtonGroup.add(macron);
        buttonGroup.add(macron);
        buttonPanel.add(macron, SwingUtils.makeConstraints("701110"));
        return buttonPanel;
    }

    private void configAction() {
        invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if (configurationFrame == null) configurationFrame = new ConfigurationFrame();
                        else configurationFrame.requestFocus();
                    }
                }
        );
    }

    private void statsAction() {
        invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if (statisticFrame == null) statisticFrame = new StatisticFrame();
                        else statisticFrame.requestFocus();
                    }
                }
        );
    }

    private void deleteAction() {
        int index = lessonList.isSelectionEmpty() || lessonList.getSelectedValue().equals("Empty") ? -1 : lessonList.getSelectedIndex();
        if (index < 0) return;
        if (lessons.get(index).isRaw() || JOptionPane.showConfirmDialog(null, "This " + Config.LESSON_NAME.toLowerCase() + " still contains records, are you sure you want to delete this " + Config.LESSON_NAME + "?", "Delete Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            String autoAppend = Config.AUTO_APPEND;
            Config.AUTO_APPEND = "NO";
            database.scheduleDeletion(lessons.get(index));
            if (lessonFrames.get(lessons.get(index).getId()) != null)
                lessonFrames.remove(lessons.get(index).getId()).dispose();
            lessons.remove(lessons.get(index));
            lessonList.refresh();
            lessonList.setSelectedIndex(index - 1 >= 0 ? index - 1 : 0);
            Config.AUTO_APPEND = autoAppend;
        }
    }

    private void addAction() {
        int index = lessonList.isSelectionEmpty() || lessonList.getSelectedValue().equals("Empty") ? lessons.size() - 1 : lessonList.getSelectedIndex();
        lessons.add(index + 1, new Lesson().fill());
        lessonList.refresh();
        lessonList.setSelectedIndex(index + 1);
    }

    private void importAction() {
        invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if (importFrame == null) importFrame = new ImportFrame();
                        else importFrame.requestFocus();
                    }
                }
        );
    }

    private void newAction() {
        invokeLater(new Runnable() {
            @Override
            public void run() {
                new NewProjectFrame();
            }
        });
    }

    private void openAction() {
        String fileName = SwingDialog.fileDialog("*.sqlite3");
        if (fileName != null) try {
            load(new Database(fileName));
        } catch (SQLException e1) {
            SwingDialog.error(e1.getMessage(), "Open Project");
        }
        else requestFocus();
    }

    private void openAction(String fileName) {
        File file = new File(fileName);
        if (file.exists()) try {
            load(new Database(fileName));
        } catch (SQLException e) {
            SwingDialog.error(e.getMessage(), "Open Project");
        }
    }

    volatile Vector<Integer> lessonIds = new Vector<Integer>();
    volatile Vector<Integer> tabIds = new Vector<Integer>();
    private static volatile JWindow window = new JWindow();

    static {
        window = new JWindow();
        window.setLayout(new GridBagLayout());
        window.setBackground(Color.WHITE);
        JLabel label = new JLabel("Saving Project");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        window.add(label, SwingUtils.makeConstraints("001111"));
        window.setPreferredSize(new Dimension(200, 50));
        window.pack();
        window.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - 200) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - 50) / 2);
//        window.setVisible(true);
    }

    private void saveAction() {
        closeFrames();
//        new Runnable() {
//            @Override
//            public void run() {
//        window.removeAll();
//        JLabel label = new JLabel("Saving Project");
//        label.setHorizontalAlignment(SwingConstants.CENTER);
//        window.add(label, SwingUtils.makeConstraints("001111"));
//        window.pack();
        window.setVisible(true);
//            }
//        }.run();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    database.save(Main.this);
                    loadRecent();
                    openFrames();
                } catch (SQLException e) {
                    e.printStackTrace();
                    SwingDialog.error("Error during saving " + e.getMessage(), "Save Project");
                } finally {
                    window.setVisible(false);
                }
            }
        });
    }

    private void findAction() {
        SwingDialog.findDialog();
    }

    private void cleanAction() {
        if (lessons != null && JOptionPane.showConfirmDialog(null,
                "<html><body><p style='width: 375px;'>" +
                        "Empty records, groups, and " + Config.LESSON_NAME.toLowerCase() + "s will be deleted.<br>" +
                        "The changes made by this action will not be automatically saved.<br><br>" +
                "Continue?", "Tidy Up Project", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            closeFrames();
            database.clean(lessons);
            openFrames();
        }
    }

    private void closeFrames() {
        Logging.info("Closing frames");
        setVisible(false);
        lessonIds.clear();
        tabIds.clear();
        if (configurationFrame != null) {
            configurationFrame.dispose();
            configurationFrame = null;
        }
        if (importFrame != null) {
            importFrame.dispose();
            importFrame = null;
        }
        if (statisticFrame != null) {
            statisticFrame.dispose();
            statisticFrame = null;
        }
        Iterator<LessonFrame> iterator = lessonFrames.values().iterator();
        while (iterator.hasNext()) {
            LessonFrame lessonFrame = iterator.next();
            lessonIds.add(lessonFrame.lesson.getId());
            tabIds.add(lessonFrame.getSelectedTabIndex());
            lessonFrame.dispose();
            iterator.remove();
        }
    }

    private void openFrames() {
        Logging.info("Reopening frames");
        requestFocus();
        refresh();
        setVisible(true);
        for (int i = 0; i < lessonIds.size(); i++) makeLessonFrame(getLessonById(lessonIds.get(i)), tabIds.get(i));
    }

    public LessonFrame getLessonFrame(int lessonId) {
        return lessonFrames.get(lessonId);
    }

    public LessonFrame removeLessonFrame(int lessonId) {
        return lessonFrames.remove(lessonId);
    }

    public void load(Database database) {
        this.database = database;
        try {
            lessons = database.load();
            setTitle("App Content Manager: " + Config.PROJECT_NAME);
            toggleButtons(true);
            lessonList.refresh();
            pack();
            repaint();
        } catch (SQLException e) {
            SwingDialog.error(e.getMessage(), "Load Database");
        }
    }

    public void toggleButtons(boolean enabled) {
        for (int b = 0; b < buttonGroup.size(); b++) buttonGroup.get(b).setEnabled(enabled);
        getJMenuBar().getMenu(0).getMenuComponent(2).setEnabled(enabled);
        getJMenuBar().getMenu(1).setEnabled(enabled);
    }

    public void makeLessonFrame(final Lesson lesson) {
        if (lesson == null) return;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (!lessonFrames.containsKey(lesson.getId())) {
                    LessonFrame lessonFrame = new LessonFrame(lesson);
                    lessonFrames.put(lesson.getId(), lessonFrame);
                } else {
                    lessonFrames.get(lesson.getId()).requestFocus();
                }
            }
        });
    }

    public class LessonListModel extends DefaultListModel {
        public LessonListModel() {
            updateModel();
        }

        public void updateModel() {
            clear();
            if (lessons != null) {
                if (lessons.isEmpty()) addElement("Empty");
                else for (int i = 0; i < lessons.size(); i++)
                    addElement(Config.LESSON_NAME + " " + (i + 1) + ": " + lessons.get(i).toString());
            }
            fireContentsChanged(this, 0, getSize());
        }
    }

    public void makeLessonFrame(final Lesson lesson, final int elementType) {
        if (lesson == null) return;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (!lessonFrames.containsKey(lesson.getId())) {
                    LessonFrame lessonFrame = new LessonFrame(lesson);
                    lessonFrames.put(lesson.getId(), lessonFrame);
                    lessonFrame.setSelectedTab(elementType);
                } else {
                    lessonFrames.get(lesson.getId()).requestFocus();
                    lessonFrames.get(lesson.getId()).setSelectedTab(elementType);
                }
            }
        });
    }

    public void makeLessonFrame(final Lesson lesson, final int elementType, final int groupIndex, final int elementIndex) {
        if (lesson == null) return;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (!lessonFrames.containsKey(lesson.getId())) {
                    LessonFrame lessonFrame = new LessonFrame(lesson);
                    lessonFrames.put(lesson.getId(), lessonFrame);
                    lessonFrame.setSelectedTab(elementType);
                    lessonFrame.getElementGroupsPanel().getGroupList().setSelectedIndex(groupIndex);
                    lessonFrame.getElementGroupsPanel().getElementGroupPanel().setSelectedRecord(elementIndex);
                } else {
                    lessonFrames.get(lesson.getId()).requestFocus();
                    lessonFrames.get(lesson.getId()).setSelectedTab(elementType);
                    lessonFrames.get(lesson.getId()).getElementGroupsPanel().getGroupList().setSelectedIndex(groupIndex);
                    lessonFrames.get(lesson.getId()).getElementGroupsPanel().getElementGroupPanel().setSelectedRecord(elementIndex);
                }
            }
        });
    }

    public void remakeLessonFrame(final Lesson lesson, final int elementType) {
        if (lesson == null) return;
        if (lessonFrames.containsKey(lesson.getId())) {
            lessonFrames.get(Main.main.lessons.get(lessonList.getSelectedIndex()).getId()).dispose();
            lessonFrames.remove(Main.main.lessons.get(lessonList.getSelectedIndex()).getId());
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (!lessonFrames.containsKey(lesson.getId())) {
                    LessonFrame lessonFrame = new LessonFrame(lesson);
                    lessonFrames.put(lesson.getId(), lessonFrame);
                } else {
                    lessonFrames.get(lesson.getId()).requestFocus();
                }
                lessonFrames.get(lesson.getId()).setSelectedTab(elementType);
            }
        });
    }

    private static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    public class ListTransferHandler extends TransferHandler {

        public boolean canImport(TransferHandler.TransferSupport info) {
            return info.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        protected Transferable createTransferable(JComponent c) {
            return new Transferable() {
                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{DataFlavor.stringFlavor};
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return (flavor.equals(DataFlavor.stringFlavor));
                }

                @Override
                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                    if (lessons == null || lessons.isEmpty()) return null;
                    if (flavor.equals(DataFlavor.stringFlavor)) return String.valueOf(lessonList.getSelectedIndex());
                    return null;
                }
            };
        }

        public int getSourceActions(JComponent c) {
            return TransferHandler.MOVE;
        }

        public boolean importData(TransferHandler.TransferSupport info) {
            int dropIndex = ((JList.DropLocation) info.getDropLocation()).getIndex();
            try {
                int selectedIndex = Integer.parseInt((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor));
                int lessonId = lessons.get(selectedIndex).getId();
                Logging.info("Drop " + selectedIndex + " to " + dropIndex);
                lessons.add(dropIndex, lessons.get(selectedIndex));
                lessons.remove(selectedIndex > dropIndex ? selectedIndex + 1 : selectedIndex);
                lessonList.refresh();
                lessonList.setSelectedIndex(selectedIndex > dropIndex ? dropIndex : dropIndex - 1);
                if (lessonFrames.containsKey(lessonId)) lessonFrames.get(lessonId).refresh();
                return true;
            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void exportDone(JComponent c, Transferable data, int action) {
        }
    }

    public static int getOrdering(Lesson lesson, Vector<Lesson> lessons) {
        for (int i = 0; i < lessons.size(); i++) if (lessons.get(i).equals(lesson)) return i;
        return -1;
    }

    public static int getOrdering(ElementGroup group, ElementGroups groups) {
        for (int i = 0; i < groups.size(); i++) if (groups.get(i).equals(group)) return i;
        return -1;
    }

    public static int getOrdering(Element element, ElementGroup group) {
        for (int i = 0; i < group.size(); i++) if (group.get(i).equals(element)) return i;
        return -1;
    }

    public Lesson getLessonById(int lessonId) {
        for (int l = 0; l < lessons.size(); l++)
            if (lessons.get(l).getId() == lessonId) return lessons.get(l);
        return null;
    }

    private void toggleOffOthers(ActionEvent e) {
        ImageButton button = ((ImageButton) e.getSource());
        for (int i = 0; i < accentButtonGroup.size(); i++) {
            if (!accentButtonGroup.get(i).equals(button)) {
                if (accentButtonGroup.get(i).getBackground().equals(Color.gray)) {
                    accentButtonGroup.get(i).setBackground(Color.white);
                }
            }
        }
    }

    private void toggleOffAll() {
        activeAccent = null;
        for (int i = 0; i < accentButtonGroup.size(); i++) {
            accentButtonGroup.get(i).setBackground(Color.white);
        }
    }

    public char doAccent(char c) {
        char returnChar = c;
        if (activeAccent != null && activeAccent.containsKey(c)) {
            returnChar = activeAccent.get(c);
            toggleOffAll();
        }
        return returnChar;
    }

}
