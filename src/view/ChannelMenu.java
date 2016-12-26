/*
 * File: ChannelMenu.java
 * Author: Fredrik Johansson
 * Date: 2016-12-21
 */
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class ChannelMenu extends JMenuBar {

    public interface OnClick {
        void onClick(String name, int id);
    }

    private static final Color LINE_COLOR = new Color(120, 120, 120);

    private Map<String, JMenu> menus = new HashMap<>();
    private OnClick onClickListener;

    public ChannelMenu() {
    }

    public void addMenu(String menu) {
        JMenu newMenu = new JMenu(menu + " ▾");
        menus.put(menu, newMenu);
        add(newMenu);
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

    private static class ChannelItem extends JMenuItem {

        public ChannelItem(String name, int id, OnClick listener) {
            setAction(new AbstractAction(name) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    listener.onClick(name, id); // TODO if null
                }
            });
        }
    }
}
