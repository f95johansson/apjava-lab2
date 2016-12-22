/*
 * File: ChannelMenu.java
 * Author: Fredrik Johansson
 * Date: 2016-12-21
 */
package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ChannelMenu extends JPanel {

    public interface OnClick {
        void onClick(String name, int id);
    }

    private static final Color LINE_COLOR = new Color(120, 120, 120);

    JMenuBar menuBar;
    private Map<String, JMenu> menus = new HashMap<>();
    private OnClick onClickListener;

    public ChannelMenu() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setBackground(Color.WHITE);

        menuBar = new JMenuBar();
        add(menuBar);
    }

    public void addMenu(String menu) {
        JMenu newMenu = new JMenu(menu + " ▾");
        menus.put(menu, newMenu);
        menuBar.add(newMenu);
    }

    public void addMenuItem(String inMenu, String item, int id) {
        JMenu selectedMenu = menus.get(inMenu);
        if (selectedMenu == null) {
            // TODO ignore or implement
        } else {
            selectedMenu.add(new ChannelItem(item, id, onClickListener));
        }
    }

    public void setOnClickListener(OnClick listener) {
        onClickListener = listener;
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension size = super.getMinimumSize();
        size.height = 22;
        return size;
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension size = super.getMaximumSize();
        size.height = 22;
        return size;
    }

    @Override
    public Dimension getPreferredSize() {
        return getMaximumSize();
    }


    /*
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(LINE_COLOR);
        g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
    }
    */

    private static class ChannelItem extends JMenuItem {

        public ChannelItem(String name, int id, OnClick listener) {
            super(name);
            /*
            setAction(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    listener.onClick(name, id); // TODO if null
                }
            });
            */
        }
    }
}
