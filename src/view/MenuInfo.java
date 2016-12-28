/*
 * File: Triplet.java
 * Author: Fredrik Johansson
 * Date: 2016-12-22
 */
package view;

import java.io.InputStream;

public class MenuInfo {

    private InputStream image;
    private String name;
    private int id;

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
