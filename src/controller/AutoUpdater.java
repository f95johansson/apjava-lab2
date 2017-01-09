/*
 * File: AutoUpdater.java
 * Author: Fredrik Johansson
 * Date: 2016-12-25
 */
package controller;

import model.TableauUpdater;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Takes care of updating the tableau on a regular interval
 */
public class AutoUpdater {

    private static int UPDATE_TIME = 30 * 1000; // millisecond

    private TableauUpdater updater;
    private boolean started = false;

    /**
     * Sets the TableauUpdater to update
     * @param updater TableauUpdater to update
     */
    public AutoUpdater(TableauUpdater updater) {
        this.updater = updater;
    }

    /**
     * Start background thread which will regularly call method
     * {@link TableauUpdater#update()}.
     */
    public void start() {
        if (!started) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    updater.update();
                }
            }, 0, UPDATE_TIME);

            started = true;
        }
    }
}
