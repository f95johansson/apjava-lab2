/*
 * File: RadioInfo.java
 * Author: Fredrik Johansson
 * Date: 2016-12-21
 */
package controller;

import model.TableauUpdater;
import model.parser.Episode;
import view.RadioUI;

import java.util.Collection;

public class RadioInfo implements RadioUI.EpisodeSelect,
                                  RadioUI.ChannelSelect,
                                  TableauUpdater.TableauLoaded{

    private TableauUpdater updater;
    private RadioUI ui;

    public RadioInfo() {
        updater = new TableauUpdater();
        updater.setTablueLoadedListener(this);

        ui = new RadioUI();
        ui.setEpisodeSelectListener(this);
        ui.setChannelSelectListener(this);
    }

    @Override
    public void onEpisodeSelect(String name, int index, int id) {
        System.out.println(name);

    }

    @Override
    public void onChannelSelect(String name, int id) {
        updater.setChannelToLoad(id);
        updater.update();
    }

    @Override
    public void onTableauLoaded(Collection<Episode> episodes) {
        System.out.println(episodes);
    }

    public static void main(String[] args) {
        new RadioInfo();
    }
}
