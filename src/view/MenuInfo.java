/*
 * File: Triplet.java
 * Author: Fredrik Johansson
 * Date: 2016-12-22
 */
package view;

import java.io.InputStream;

/**
 * Helper class to contain information about an menu item
 */
public class MenuInfo {

    private InputStream image;
    private String name;
    private int id;

    /**
     * Provide menu item information
     * @param image Image for the menu item
     * @param name Name of the menu item
     * @param id Id of the menu item
     */
    public MenuInfo(InputStream image, String name, int id) {
        this.image = image;
        this.name = name;
        this.id = id;
    }

    public InputStream getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }
}
