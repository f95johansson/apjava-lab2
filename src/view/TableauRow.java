/*
 * File: TableauRow.java
 * Author: Fredrik Johansson
 * Date: 2016-12-25
 */
package view;

public class TableauRow {
    private final String time;
    private final String text;
    private final int id;
    private final boolean enabled;

    public TableauRow(String time, String text, int id, boolean enabled) {
        this.time = " "+time;
        this.text = text;
        this.id = id;
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String toString() {
        return text;
    }

    public int getID() {
        return id;
    }

    public String getTime() {
        return time;
    }
}