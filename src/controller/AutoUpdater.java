/*
 * File: AutoUpdater.java
 * Author: Fredrik Johansson
 * Date: 2016-12-25
 */
package controller;

import model.TableauUpdater;

import java.util.Timer;
import java.util.TimerTask;

public class AutoUpdater {

    private static int UPDATE_TIME = 5 * 1000; // millisecond

    private TableauUpdater updater;
    private boolean started = false;

    public AutoUpdater(TableauUpdater updater) {
        this.updater = updater;
    }

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
