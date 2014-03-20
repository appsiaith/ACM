package view.panel;

import model.Element;
import model.ElementGroups;
import util.Logging;
import util.ProgramSettings;
import util.SwingUtils;
import view.Main;
import view.components.ImageButton;
import view.components.SwingDialog;
import view.frame.LessonFrame;
import view.frame.QuickImportFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * Created with IntelliJ IDEA.
 * User: endario
 * Date: 29/11/2013
 * Time: 18:03
 */
public class ElementGroupsPanel<T extends Element> extends MyPanel {

    protected LessonFrame lessonFrame;
    protected ElementGroupsList groupList;
    protected ElementGroupPanel elementGroupPanel;
    protected ElementGroups<T> elementGroups;

    /*public Timer autoAppendTimer = new Timer(200, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Config.AUTO_APPEND.equals("YES")) {
                if (elementGroup.checkRaw()) {
                    refresh();
                }
            }
        }
    });*/

    public ElementGroupsPanel(final LessonFrame lessonFrame, ElementGroups<T> elementGroups) {
        super();
        this.lessonFrame = lessonFrame;
        this.elementGroups = elementGroups;
        add(createButtonPanel(), SwingUtils.makeConstraints("001110"));
        add(createListPanel(), SwingUtils.makeConstraints("0,1,1,1,1,0"));
        elementGroupPanel = new ElementGroupPanel(this);
        GridBagConstraints constraints = SwingUtils.makeConstraints("021111");
        constraints.insets = new Insets(0, 0, 0, 0);
        add(elementGroupPanel, constraints);
        groupList.setSelectedIndex(0);
//        autoAdd();
    }

    /*public void autoAdd() {
        autoAppendTimer =
        autoAppendTimer.setRepeats(true);
        autoAppendTimer.setCoalesce(true);
        autoAppendTimer.start();
    }*/

    private JPanel createButtonPanel() {
        int column = 0;
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        ImageButton plusButton = new ImageButton("plus.png", "New Group", 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addGroup();
            }
        };
        plusButton.setFocusable(false);
        buttonPanel.add(plusButton, SwingUtils.makeConstraints(column++ + "01110"));
        plusButton.setPreferredSize(new Dimension(plusButton.getPreferredSize().width, ProgramSettings.BUTTON_HEIGHT));
        ImageButton minusButton = new ImageButton("minus.png", "Remove Group", 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                removeGroup();
            }
        };
        minusButton.setFocusable(false);
        buttonPanel.add(minusButton, SwingUtils.makeConstraints(column++ + "01110"));
        minusButton.setPreferredSize(new Dimension(minusButton.getPreferredSize().width, ProgramSettings.BUTTON_HEIGHT));
        ImageButton typeButton = new ImageButton("config.png", "Group Type", 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                SwingDialog.typeDialog(ElementGroupsPanel.this);
            }
        };
        typeButton.setFocusable(false);
        typeButton.setEnabled(false);
        if (elementGroups.getInstance().getType() == Element.QUESTION || elementGroups.getInstance().getType() == Element.DIALOG)
            buttonPanel.add(typeButton, SwingUtils.makeConstraints(column++ + "01110"));
        typeButton.setPreferredSize(new Dimension(typeButton.getPreferredSize().width, ProgramSettings.BUTTON_HEIGHT));
        ImageButton importButton = new ImageButton("import.png", "Quick Import", 20, false) {
            @Override
            public void actionPerformed(ActionEvent evt) {
                invokeLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                new QuickImportFrame(Main.getOrdering(lessonFrame.lesson, Main.main.lessons), elementGroups.getInstance().getType());
                            }
                        }
                );
            }
        };
        importButton.setFocusable(false);
        if (elementGroups.getInstance().getType() != Element.ABOUT)
            buttonPanel.add(importButton, SwingUtils.makeConstraints(column++ + "01110"));
        importButton.setPreferredSize(new Dimension(importButton.getPreferredSize().width, ProgramSettings.BUTTON_HEIGHT));
        typeButton.setEnabled(elementGroups.getInstance().getType() == Element.DIALOG || elementGroups.getInstance().getType() == Element.QUESTION);
        importButton.setEnabled(elementGroups.getInstance().getType() != Element.ABOUT);
        return buttonPanel;
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        groupList = new ElementGroupsList();
        panel.add(new JLabel("Current Group"), SwingUtils.makeConstraints("001101"));
        panel.add(groupList, SwingUtils.makeConstraints("101111"));
        return panel;
    }

    private void removeGroup() {
        int index = groupList.isSelectionEmpty() || groupList.getSelectedValue().equals("Empty") ? -1 : groupList.getSelectedIndex();
        if (index < 0) return;
        if (elementGroups.get(index).isRaw() || JOptionPane.showConfirmDialog(null, "This group still contains records, are you sure you want to delete this group?", "Delete Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            Logging.info("Remove Group " + index);
            Main.main.database.scheduleDeletion(elementGroups.get(index));
            elementGroups.remove(index);
            index = index - 1 < 0 ? 0 : index - 1;
            ((ElementGroupsListModel) groupList.getModel()).updateModel();
            groupList.setSelectedIndex(index);
            Logging.info("Group Removal Complete");
        }
    }

    private void addGroup() {
        int index = groupList.getSelectedValue().equals("Empty") ? -1 : groupList.getSelectedIndex();
        Logging.info("Add Group after " + index);
        elementGroups.addRaw(index + 1);
        ((ElementGroupsListModel) groupList.getModel()).updateModel();
        groupList.setSelectedIndex(index + 1);
        Logging.info("Group Addition Completed");
    }

    public ElementGroups<T> getElementGroups() {
        return elementGroups;
    }

    @Override
    public void refresh() {
        groupList.refresh();
        elementGroupPanel.refresh();
    }

    public LessonFrame getLessonFrame() {
        return lessonFrame;
    }

    public ElementGroupPanel getElementGroupPanel() {
        return elementGroupPanel;
    }

    public ElementGroupsList getGroupList() {
        return groupList;
    }

    class ListTransferHandler extends TransferHandler {

        private final DataFlavor selectedRowsFlavor = new DataFlavor(int[].class, "Selected Rows");

        public ListTransferHandler() {
        }

        @Override
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
                    if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
                    if (groupList.getSelectedValue().equals("Empty")) return null;
                    if (flavor.equals(DataFlavor.stringFlavor))
                        return lessonFrame.lesson.getId() + ","
                                + elementGroups.getInstance().getType() + ","
                                + groupList.getSelectedIndex();
                    return null;
                }
            };
        }

        @Override
        public boolean canImport(TransferSupport info) {
            boolean canImport = false;
            if (info.isDrop()) {
                JList.DropLocation location = (JList.DropLocation) info.getDropLocation();
                if (location.isInsert() && info.getTransferable().isDataFlavorSupported(DataFlavor.stringFlavor)
                        && !info.isDataFlavorSupported(selectedRowsFlavor))
                    try {
                        canImport = ((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor))
                                .split(",")[1].equals(String.valueOf(elementGroups.getInstance().getType()));
                    } catch (UnsupportedFlavorException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                else if (!location.isInsert() && info.getTransferable().isDataFlavorSupported(DataFlavor.stringFlavor)
                        && info.isDataFlavorSupported(selectedRowsFlavor))
                    try {
                        canImport = ((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor))
                                .split(",")[1].equals(String.valueOf(elementGroups.getInstance().getType())) & !elementGroups.isEmpty();
                    } catch (UnsupportedFlavorException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
//            if (canImport) groupList.setCursor(DragSource.DefaultMoveDrop);
//            else groupList.setCursor(DragSource.DefaultMoveNoDrop);
            return canImport;
        }

        @Override
        public int getSourceActions(JComponent c) {
            return TransferHandler.MOVE;
        }

        @Override
        public boolean importData(TransferSupport info) {
            if (info.isDrop()) {
                JList.DropLocation location = (JList.DropLocation) info.getDropLocation();
                int dropIndex = location.getIndex();
                int tableDropIndex = -1;
                if (location.isInsert()) {
                    try {
                        int sourceLessonId = Integer.valueOf(((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor)).split(",")[0]);
                        int sourceType = Integer.valueOf(((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor)).split(",")[1]);
                        int sourceIndex = Integer.valueOf(((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor)).split(",")[2]);
                        if (sourceLessonId == lessonFrame.lesson.getId()) {
                            //same lesson
                            elementGroups.add(dropIndex, elementGroups.get(sourceIndex));
                            elementGroups.remove(dropIndex <= sourceIndex ? sourceIndex + 1 : sourceIndex);
                            dropIndex = dropIndex <= sourceIndex ? dropIndex : dropIndex - 1;
                        } else {
                            //cross lesson
                            if (elementGroups.isEmpty()) dropIndex = 0;
                            elementGroups.add(dropIndex, Main.main.getLessonFrame(sourceLessonId).lesson.getElementGroups(sourceType).get(sourceIndex));
                            Main.main.getLessonFrame(sourceLessonId).lesson.getElementGroups(sourceType).remove(sourceIndex);
                            Main.main.getLessonFrame(sourceLessonId).getElementGroupsPanel().refresh();
                            Main.main.getLessonFrame(sourceLessonId).getElementGroupsPanel().getGroupList()
                                    .setSelectedIndex(sourceIndex - 1 < 0 ? 0 : sourceIndex - 1);
                        }
                        requestFocus();
                        refresh();
                        groupList.setSelectedIndex(dropIndex);
                        return true;
                    } catch (UnsupportedFlavorException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        int sourceLessonId = Integer.valueOf(((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor)).split(",")[0]);
                        int sourceType = Integer.valueOf(((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor)).split(",")[1]);
                        int sourceIndex = Integer.valueOf(((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor)).split(",")[2]);
                        int[] selectedRows = ((int[]) info.getTransferable().getTransferData(selectedRowsFlavor));
                        if (sourceLessonId == lessonFrame.lesson.getId()) {
                            //same lesson
                            if (sourceIndex != dropIndex)
                                tableDropIndex = elementGroups.get(sourceIndex).transferTo(elementGroups.get(dropIndex), selectedRows, elementGroups.get(dropIndex).size());
                        } else {
                            //cross lesson
                            tableDropIndex = Main.main.getLessonFrame(sourceLessonId).lesson.getElementGroups(sourceType).get(sourceIndex)
                                    .transferTo(elementGroups.get(dropIndex), selectedRows, elementGroups.get(dropIndex).size());
                            Main.main.getLessonFrame(sourceLessonId).getElementGroupsPanel().elementGroupPanel.refresh();
                        }
                        elementGroupPanel.tabs.setSelectedIndex(1);
                        requestFocus();
                        refresh();
                        groupList.setSelectedIndex(dropIndex);
                        elementGroupPanel.getTablePanel().setSelectedRows(tableDropIndex, tableDropIndex + selectedRows.length - 1);
                        return true;
                    } catch (UnsupportedFlavorException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }

        @Override
        protected void exportDone(JComponent c, Transferable t, int act) {
        }
    }

    public class ElementGroupsList extends JList {
        public ElementGroupsList() {
            super(new ElementGroupsListModel());
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            setLayoutOrientation(JList.HORIZONTAL_WRAP);
            setVisibleRowCount(-1);
            setTransferHandler(new ListTransferHandler());
            setDropMode(DropMode.ON_OR_INSERT);
            setDragEnabled(true);
            setFocusable(false);
            getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    ListSelectionModel model = (ListSelectionModel) e.getSource();
                    groupList.setCellRenderer(new DefaultListCellRenderer() {
                        public Component getListCellRendererComponent(JList list, Object value,
                                                                      int index, boolean isSelected, boolean cellHasFocus) {
                            JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                            lbl.setHorizontalTextPosition(SwingConstants.CENTER);
                            lbl.setHorizontalAlignment(SwingConstants.CENTER);
                            if (value.equals("Empty"))
                                lbl.setPreferredSize(new Dimension(60, lbl.getPreferredSize().height));
                            else lbl.setPreferredSize(new Dimension(30, lbl.getPreferredSize().height));
                            return lbl;
                        }
                    });
                    groupList.ensureIndexIsVisible(groupList.getSelectedIndex());
                    if (model.isSelectionEmpty() || groupList.getSelectedValue().equals("Empty"))
                        elementGroupPanel.setElementGroup(null);
                    else elementGroupPanel.setElementGroup(elementGroups.get(model.getMinSelectionIndex()));
                    elementGroupPanel.refresh();
                }
            });
        }

        public void refresh() {
            ((ElementGroupsListModel) getModel()).updateModel();
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    class ElementGroupsListModel extends DefaultListModel {
        public ElementGroupsListModel() {
            updateModel();
        }

        public void updateModel() {
            clear();
            if (elementGroups.isEmpty()) addElement("Empty");
            else for (int i = 1; i <= elementGroups.size(); i++) addElement(String.valueOf(i));
            fireContentsChanged(groupList, 0, getSize());
        }
    }
}
