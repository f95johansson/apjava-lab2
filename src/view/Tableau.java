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
import java.util.HashMap;
import java.util.Map;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

/**
 * Tableau is an extension of a table and will show two columns of
 * information about an episode, its time and name.
 */
class Tableau extends JPanel {

    /**
     * Listener for when an item/row is selected
     */
    public interface ItemSelect {
        /**
         * Information about episode is provided on listeners call
         * @param name Name of the episode
         * @param index Index of the episode
         * @param id Id of the episode
         */
        void onItemSelect(String name, int index, int id);
    }


    private static final Color SELECTED_ROW_BACKGROUND = new Color(36, 108,129);
    private static final Color EVEN_BACKGROUND = new Color(228, 234, 235);
    private static final Color ODD_BACKGROUND = Color.WHITE;
    private static final int MARGIN = 12;
    private static final int SIDE_SHADOW_WIDTH = 4;
    private static final int TOP_SHADOW_HEIGHT = 12;
    private static final int CORNER_SHADOW_SIZE = 12;
    private static final int ROW_HEIGHT = 48;
    private static final int HEADER_HEIGHT = ROW_HEIGHT - TOP_SHADOW_HEIGHT;

    private JTable table;
    private JScrollPane scroll;
    private DefaultTableModel model;
    private Image shadowTop;
    private Image shadowLeft;
    private Image shadowRight;
    private Image shadowLeftCorner;
    private Image shadowRightCorner;
    private Map<Integer, Integer> ids = new HashMap<>();

    /**
     * Will load images and set styles for the table
     * @throws CreationFailedException If images failed to load
     */
    public Tableau() throws CreationFailedException {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        table = new JTable(0, 2);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);

        JLabel label = new JLabel("<html><body>" +
                "<pre style=\"font-size:12px;font-family:sans-serif\">" +
                "Tid      Program" +
                "</pre>" +
                "</body></html>");

        label.setBorder(BorderFactory.createEmptyBorder(
                MARGIN+TOP_SHADOW_HEIGHT,
                0, 0, 0));
        label.setBackground(Color.WHITE);

        scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(
                        MARGIN+TOP_SHADOW_HEIGHT+HEADER_HEIGHT -
                                label.getPreferredSize().height,
                        MARGIN+SIDE_SHADOW_WIDTH,
                        0,
                        MARGIN+SIDE_SHADOW_WIDTH));

        add(label);
        add(scroll);

        scroll.setBackground(new Color(0, 0, 0, 0));
        setBackground(new Color(0, 0, 0, 0));


        model = new DefaultTableModel(0, 2) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? String.class : TableauRow.class;
            }

            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        table.setModel(model);
        table.setDragEnabled(false);
        table.setDefaultRenderer(TableauRow.class, new CellRenderer());
        table.setDefaultRenderer(String.class, new CellRenderer());
        table.setRowHeight(ROW_HEIGHT);

        table.setSelectionMode(SINGLE_SELECTION);

        table.setTableHeader(null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(0).setMaxWidth(110);
        table.getColumnModel().getColumn(0).setMinWidth(110);


        try {
            shadowTop = ImageIO.read(getClass().getResourceAsStream(
                    "/tablaou-top-shadow.png"));
            shadowLeft = ImageIO.read(getClass().getResourceAsStream(
                    "/tablaou-left-shadow.png"));
            shadowRight = ImageIO.read(getClass().getResourceAsStream(
                    "/tablaou-right-shadow.png"));
            shadowLeftCorner = ImageIO.read(getClass().getResourceAsStream(
                    "/tablaou-left-corner.png"));
            shadowRightCorner = ImageIO.read(getClass().getResourceAsStream(
                    "/tablaou-right-corner.png"));
        } catch (IOException e) {
            throw new CreationFailedException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new CreationFailedException("Failed to load images");
        }
    }

    /**
     * Clear all rows of episodes
     */
    public void clear() {
        model.setRowCount(0);
    }

    /**
     * Add a row of episode. Will be append last on the table rows
     * @param episode Row with episode information
     */
    public void addEpisode(TableauRow episode) {
        ids.put(model.getRowCount(), episode.getID());
        model.addRow(new Object[] {episode.getTime(), episode});
    }

    /**
     * Set which of the rows/episodes should be selected
     * @param index index of episode
     */
    public void setSelected(int index) {
        try {
            table.setRowSelectionInterval(index, index);
            Rectangle cellRect = table.getCellRect(index, 0, true);
            cellRect.height += scroll.getViewport().getHeight();
            table.scrollRectToVisible(cellRect);
        } catch (IllegalArgumentException e) {
            // index outside, ignore
        }
    }

    /**
     * Sets a listener which is triggered when a row is selected
     * @param itemSelectListener Listener to listen to event
     */
    public void setItemSelectListener(ItemSelect itemSelectListener) {
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) { // selection is triggered twice
                Object value = null;
                try {
                    value = table.getValueAt(table.getSelectedRow(), 1);
                } catch (ArrayIndexOutOfBoundsException e) {/* value = null */}

                if (value != null) {
                    String name = value.toString();
                    int id = ids.get(table.getSelectedRow());
                    if (itemSelectListener != null) {
                        itemSelectListener.onItemSelect(name,
                                                        table.getSelectedRow(),
                                                        id);
                    }
                }
                repaint();
            }
        });
    }

    /**
     * {@inheritDoc}
     *
     * Paints the extra graphics around the table
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(shadowTop,
                MARGIN + CORNER_SHADOW_SIZE,
                MARGIN,
                getWidth()-(MARGIN+CORNER_SHADOW_SIZE)*2,
                TOP_SHADOW_HEIGHT, null);

        g.drawImage(shadowLeft,
                MARGIN,
                MARGIN+TOP_SHADOW_HEIGHT,
                SIDE_SHADOW_WIDTH,
                getHeight()-MARGIN+TOP_SHADOW_HEIGHT, null);

        g.drawImage(shadowRight,
                getWidth()-MARGIN-SIDE_SHADOW_WIDTH,
                MARGIN+TOP_SHADOW_HEIGHT,
                SIDE_SHADOW_WIDTH,
                getHeight()-MARGIN+TOP_SHADOW_HEIGHT, null);

        g.drawImage(shadowLeftCorner, MARGIN, MARGIN, null);
        g.drawImage(shadowRightCorner,
                getWidth()-MARGIN-CORNER_SHADOW_SIZE, MARGIN, null);

        g.setColor(Color.WHITE);
        g.fillRect(MARGIN+SIDE_SHADOW_WIDTH,
                   MARGIN+TOP_SHADOW_HEIGHT,
                   getWidth()-(MARGIN+SIDE_SHADOW_WIDTH)*2,
                   HEADER_HEIGHT);

    }


    /**
     * Helper class for rendering each of the rows
     */
    private static class CellRenderer extends DefaultTableCellRenderer {
        /**
         * {@inheritDoc}
         *
         * Will render each other row a different color. Rows
         * which are disabled will be rendered with gray text, others
         * with black. Selected row will have a striking color to discern
         * it from other rows
         */
        @Override
        public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {

            Color background;
            Color foreground;


            TableauRow info = (TableauRow) table.getModel().getValueAt(row, 1);
            if (info.isEnabled()) {
                foreground = Color.BLACK;
            } else {
                foreground = Color.GRAY;
            }

            if (row % 2 == 0) {
                background = EVEN_BACKGROUND;
            } else {
                background = ODD_BACKGROUND;
            }

            if (isSelected) {
                background = SELECTED_ROW_BACKGROUND;
                foreground = Color.WHITE;
            }

            setBackground(background);
            setForeground(foreground);
            setValue(value);

            return this;
        }
    }
}
