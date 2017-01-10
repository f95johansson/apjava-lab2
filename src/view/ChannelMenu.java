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

/**
 * Channel menu extends a normal JMenuBar add ease upp the usage with
 * listeners and dropdown menu items with corresponding id's
 */
class ChannelMenu extends JMenuBar {

    /**
     * A listener for whenever a menu item is clicked
     */
    public interface OnClick {
        /**
         * On result the name and id of the clicked menu item is provided
         * @param name Name of menu item
         * @param id Id corresponding to menu item
         */
        void onClick(String name, int id);
    }

    private Map<String, JMenu> menus = new HashMap<>();
    private OnClick onClickListener;


    /**
     * Add a menu (not menu item). This menu works as a dropdown when clicked
     * @param menu Name of menu
     */
    public void addMenu(String menu) {
        JMenu newMenu = new JMenu(menu + " ▾");
        menus.put(menu, newMenu);
        add(newMenu);
    }

    /**
     * Add a menu item to the specified menu
     * @param inMenu Name of menu to add menu item to
     * @param name Name of menu item
     * @param id Id of menu item
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

    /**
     * Sets a listener for all the menu items
     * @param listener Listener for menu item click
     */
    public void setOnClickListener(OnClick listener) {
        onClickListener = listener;
    }

    /**
     * Helper class to let menu items remember both name and id to provide
     * to listener
     */
    private static class ChannelItem extends JMenuItem {

        /**
         * Provide name and id which will be returned in listener
         * @param name Name of menu item
         * @param id Id of menu item
         * @param listener Listener for menu item click
         */
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
