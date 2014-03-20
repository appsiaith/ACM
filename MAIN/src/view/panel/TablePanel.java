package view.panel;

import model.Element;
import org.jdesktop.swingx.JXTable;
import util.ProgramSettings;
import util.SwingUtils;
import view.Main;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: rrd09
 * Date: 03/11/13
 * Time: 16:52
 */
public class TablePanel extends MyPanel {
    protected JXTable table;
    protected ElementGroupPanel elementGroupPanel;

    public TablePanel(final ElementGroupPanel panel) {
        super();
        elementGroupPanel = panel;
        table = new JXTable(new ElementTableModel());
        table.setTransferHandler(new TableTransferHandler());
        table.setSortable(false);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        table.setCellSelectionEnabled(true);
        table.setDragEnabled(true);
        table.setDropMode(DropMode.INSERT_ROWS);
        table.setFillsViewportHeight(true);
        table.setColumnControlVisible(true);
        table.setBackground(ProgramSettings.frontColor);
        add(new JScrollPane(table), SwingUtils.makeConstraints("011111"));

        ActionMap map = table.getActionMap();
        InputMap im = table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke tabKey = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTabKey = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_MASK);
        map.put(im.get(tabKey), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.isEditing() ? table.getEditingRow() : table.getSelectedRow();
                int col = table.isEditing() ? table.getEditingColumn() : table.getSelectedColumn();
                if (col < 0) col = 0;
                if (row < 0) row = 0;
                col++;
                // Move to next row and left column
                if (col >= table.getColumnCount()) {
                    col = 0;
                    row++;
                }
                // Move to top row
                if (row >= table.getRowCount()) row = 0;
                // Move cell selection
                table.changeSelection(row, col, false, false);
                table.requestFocus();
                table.editCellAt(row, col);
            }
        });
        map.put(im.get(shiftTabKey), new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.isEditing() ? table.getEditingRow() : table.getSelectedRow();
                int col = table.isEditing() ? table.getEditingColumn() : table.getSelectedColumn();
                if (col < 0) col = 0;
                if (row < 0) row = 0;
                col--;
                // Move to next row and left column
                if (col < 0) {
                    col = table.getColumnCount() - 1;
                    row--;
                }
                // Move to top row
                if (row < 0) row = table.getRowCount() - 1;
                // Move cell selection
                table.changeSelection(row, col, false, false);
                table.requestFocus();
                table.editCellAt(row, col);
            }
        });
    }

    public void refresh() {
        ((ElementTableModel) table.getModel()).fireTableDataChanged();
        table.repaint();
        elementGroupPanel.navigationPanel.setVisible(false);
    }

    public void setSelectedRows(int indexFrom, int indexTo) {
        table.getSelectionModel().clearSelection();
        table.getSelectionModel().setAnchorSelectionIndex(indexFrom);
        for (int i = indexFrom; i <= indexTo; i++)
            for (int c = 0; c < table.getColumnCount(); c++) table.changeSelection(i, c, false, true);
    }

    class TableTransferHandler extends TransferHandler {
        private final DataFlavor selectedRowsFlavor = new DataFlavor(int[].class, "Selected Rows");

        @Override
        protected Transferable createTransferable(JComponent c) {
            return new Transferable() {
                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{selectedRowsFlavor, DataFlavor.stringFlavor};
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return selectedRowsFlavor.equals(flavor) || DataFlavor.stringFlavor.equals(flavor);
                }

                @Override
                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                    if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
                    if (flavor.equals(selectedRowsFlavor)) return table.getSelectedRows();
                    if (flavor.equals(DataFlavor.stringFlavor))
                        return elementGroupPanel.elementGroupsPanel.getLessonFrame().lesson.getId() + ","
                                + elementGroupPanel.elementGroupsPanel.elementGroups.getInstance().getType() + ","
                                + elementGroupPanel.elementGroupsPanel.groupList.getSelectedIndex();
                    return null;
                }
            };
        }

        @Override
        public boolean canImport(TransferSupport info) {
            boolean canImport = false;
            if (info.isDrop()) {
                if (info.getTransferable().isDataFlavorSupported(DataFlavor.stringFlavor)
                        && info.isDataFlavorSupported(selectedRowsFlavor))
                    try {
                        canImport = ((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor))
                                .split(",")[1].equals(String.valueOf(elementGroupPanel.elementGroupsPanel.elementGroups.getInstance().getType()));
                    } catch (UnsupportedFlavorException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return canImport;
        }

        @Override
        public int getSourceActions(JComponent c) {
            return TransferHandler.MOVE;
        }

        @Override
        public boolean importData(TransferSupport info) {
            if (info.isDrop()) {
                int dropIndex = ((JTable.DropLocation) info.getDropLocation()).getRow();
                try {
                    int sourceLessonId = Integer.valueOf(((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor)).split(",")[0]);
                    int sourceType = Integer.valueOf(((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor)).split(",")[1]);
                    int sourceIndex = Integer.valueOf(((String) info.getTransferable().getTransferData(DataFlavor.stringFlavor)).split(",")[2]);
                    int[] selectedRows = ((int[]) info.getTransferable().getTransferData(selectedRowsFlavor));
                    if (sourceLessonId == elementGroupPanel.elementGroupsPanel.getLessonFrame().lesson.getId()) {
                        //same lesson & same accentButtonGroup
                        if (sourceIndex == elementGroupPanel.elementGroupsPanel.groupList.getSelectedIndex()) {
                            dropIndex = elementGroupPanel.getElementGroup().reorder(selectedRows, dropIndex);
                        }
                    } else {
                        //cross lesson
                        dropIndex = Main.main.getLessonFrame(sourceLessonId).lesson.getElementGroups(sourceType).get(sourceIndex).transferTo(elementGroupPanel.getElementGroup(), selectedRows, dropIndex);
                        Main.main.getLessonFrame(sourceLessonId).getElementGroupsPanel().elementGroupPanel.tablePanel.refresh();
                        Main.main.getLessonFrame(sourceLessonId).getElementGroupsPanel().elementGroupPanel.tablePanel.table.getSelectionModel().clearSelection();
                    }
                    requestFocus();
                    refresh();
                    setSelectedRows(dropIndex, dropIndex + selectedRows.length - 1);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void exportDone(JComponent component, Transferable t, int act) {
        }
    }

    class ElementTableModel extends AbstractTableModel implements TableModel {

        public ElementTableModel() {
        }

        @Override
        public int getColumnCount() {
            return elementGroupPanel.elementGroupsPanel.elementGroups.getInstance().getFieldNames().length;
        }

        @Override
        public String getColumnName(int column) {
            return elementGroupPanel.elementGroupsPanel.elementGroups.getInstance().getFieldNamesShort()[column];
        }

        @Override
        public int getRowCount() {
            return elementGroupPanel.getElementGroup() == null ? 0 : elementGroupPanel.getElementGroup().size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return ((Element) elementGroupPanel.getElementGroup().get(rowIndex)).getField(elementGroupPanel.elementGroupsPanel.elementGroups.getInstance().getFieldNames()[columnIndex]);
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            Element element = (Element) elementGroupPanel.getElementGroup().get(rowIndex);
            element.setField(elementGroupPanel.elementGroupsPanel.elementGroups.getInstance().getFieldNames()[columnIndex], (String) value);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
    }
}
