/*
 * File: Tableau.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class Tableau extends JPanel {

    public interface ItemSelect {
        void onItemSelect(String name, int index, int id);
    }

    private JTable table;
    private DefaultTableModel model;
    private Image shadowTop;

    public Tableau(int rows, int columns) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        table = new JTable(rows, columns);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(createEmptyBorder());
        table.setIntercellSpacing(new Dimension(0, 0));
        add(scroll);

        setBackground(new Color(0, 0, 0, 0));


        model = new DefaultTableModel(rows, columns) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        table.setModel(model);
        table.setDragEnabled(false);
        table.setDefaultRenderer(String.class, new CellRenderer());
        table.setRowHeight(48);

        table.setSelectionMode(SINGLE_SELECTION);

        table.setTableHeader(null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(0).setMaxWidth(110);
        table.getColumnModel().getColumn(0).setMinWidth(110);
        /*
        JTableHeader header = new JTableHeader();
        header.setDefaultRenderer(new CellRenderer());
        TableColumnModel headerModel = new DefaultTableColumnModel();
        headerModel.addColumn(new TableColumn());
        header.setColumnModel(headerModel);
        table.setTableHeader(header);
        */



        try {
            shadowTop = ImageIO.read(getClass().getResourceAsStream("/tablaou-top-shadow.png"));
        } catch (IOException e) {
            e.printStackTrace();
            // FIXME empty catch
        }
    }

    public void addEpisode(String time, String name, int id) {
        model.addRow(new String[] {time, name});
    }

    public void setItemSelectListener(ItemSelect itemSelectListener) {
        table.getSelectionModel().addListSelectionListener(event -> {
            Object value = table.getValueAt(table.getSelectedRow(), 1);
            if (value != null) {
                String name = value.toString();
                itemSelectListener.onItemSelect(name, table.getSelectedRow(), 1); // TODO if null
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.drawImage(shadowTop, 0, 0, getWidth(), 2, null);
    }


    private static class CellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {


            if (isSelected) {
                setBackground(new Color(36, 108, 129));
                setForeground(Color.WHITE);
            } else {
                setForeground(Color.BLACK);
                if (row % 2 == 0) {
                    setBackground(new Color(228, 234, 235));
                } else {
                    setBackground(Color.WHITE);
                }
            }

            setValue(value);

            return this;
        }
    }
}
