/*
 * File: ChannelMenu.java
 * Author: Fredrik Johansson
 * Date: 2016-12-21
 */
package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

class ChannelMenu extends JMenuBar {

    public interface OnClick {
        void onClick(String name, int id);
    }

    private Map<String, JMenu> menus = new HashMap<>();
    private OnClick onClickListener;


    public void addMenu(String menu) {
        JMenu newMenu = new JMenu(menu + " ▾");
        menus.put(menu, newMenu);
        add(newMenu);
    }

    /**
     *
     * @param inMenu
     * @param name
     * @param id
     * @throws IllegalArgumentException if inMenu doesn't correspond to a menu
     */
    public void addMenuItem(String inMenu, String name, int id) {
        JMenu selectedMenu = menus.get(inMenu);
        if (selectedMenu == null) {
            throw new IllegalArgumentException("No such menu");
        } else {
            selectedMenu.add(new ChannelItem(name, id, onClickListener));
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
                    if (listener != null) {
                        listener.onClick(name, id);
                    }
                }
            });
        }
    }
}
