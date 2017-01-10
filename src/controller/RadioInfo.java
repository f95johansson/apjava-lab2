/*
 * File: RadioInfo.java
 * Author: Fredrik Johansson
 * Date: 2016-12-21
 */
package controller;

import model.ChannelsFetcher;
import model.TableauUpdater;
import model.parser.Channel;
import model.parser.Episode;
import view.MenuInfo;
import view.RadioUI;
import view.TableauRow;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * RadioInfo cares care of bridging the user interface and the logic.
 * It will also take care of what to do with user interaction and
 * information from the logic.
 */
public class RadioInfo implements RadioUI.EpisodeSelect,
                                  RadioUI.ChannelSelect,
                                  RadioUI.Refresh,
                                  TableauUpdater.TableauLoaded,
                                  ChannelsFetcher.ChannelsLoaded {

    private static final String PRIMARY_CHANNEL = "Rikskanal";

    private TableauUpdater updater;
    private AutoUpdater autoUpdater;
    private ChannelsFetcher fetcher;
    private RadioUI ui;

    /**
     * Starts a new ui and start to load in channels to be displayed to user
     */
    public RadioInfo() {
        updater = new TableauUpdater();
        updater.setTableauLoadedListener(this);

        autoUpdater = new AutoUpdater(updater);

        fetcher = new ChannelsFetcher();
        fetcher.setChannelsLoadedListener(this);

        ui = new RadioUI();
        ui.setEpisodeSelectListener(this);
        ui.setChannelSelectListener(this);
        ui.setOnRefreshListener(this);

        fetcher.fetch(); // load channels
    }

    /**
     * This method is used as a listener on the ui and will receive
     * event upon user interaction, whenever an episode is selected.
     * It will load information about the episode onto the ui.
     * @param name Title of episode
     * @param index Index of episode
     * @param id Id for episode
     */
    @Override
    public void onEpisodeSelect(String name, int index, int id) {
        Episode episode = updater.getEpisode(index);

        if (episode == null) {
            return; // can't find episode
        }

        InputStream image = null;
        try {
            image = new URL(episode.imageurl).openStream();
        } catch (IOException | NullPointerException e) {
            /* result: image = null // could not load image */
        }

        ui.setEpisodeContent(episode.title, episode.subtitle,
                    episode.description, image);
    }

    /**
     * This method is used as a listener on the ui and will receive
     * event upon user interaction, whenever a channel is selected.
     * It will load in the tableau for that channel and show it on the ui.
     * @param name Name of channel
     * @param id Id of channel
     */
    @Override
    public void onChannelSelect(String name, int id) {
        updater.setChannelToLoad(id);
        updater.update();

        Channel channel = fetcher.getChannel(id);
        if (channel != null) {
            ui.setColor(channel.color);
            ui.setTitle(channel.name);
            ui.clear();
        }
    }

    /**
     * This method is used as a listener on the ui and will receive
     * event upon user interaction, whenever a manual refresh is triggered.
     */
    @Override
    public void onRefresh() {
        updater.update();
    }

    /**
     * This method acts as listener on the model, whenever all the
     * channels have loaded. It will take this information about the
     * channels and show it to the user on the ui.
     * @param channels List of channels loaded. Will be null if
     *                 model couldn't load any channels
     */
    @Override
    public void onChannelsLoaded(List<Channel> channels) {
        Set<String> channelTypes = new LinkedHashSet<>();
        // uses set for uniqueness and linked to preserve order
        Map<String, List<MenuInfo>> channelItems = new HashMap<>();

        List<MenuInfo> displayChannels = new ArrayList<>();

        if (channels == null) {
            setErrorMessage("Could not load information. " +
                            "Check internet connection");
            return;
        }

        for (Channel channel : channels) {
            if (channel.channeltype.equals(PRIMARY_CHANNEL)) {
                InputStream image;
                try {
                    URL imageURL = new URL(channel.image);
                    image = imageURL.openStream();
                } catch (IOException e) {
                    image = null;
                }
                displayChannels.add(new MenuInfo(
                        image,
                        channel.name,
                        channel.id));

            } else {

                channelTypes.add(channel.channeltype);
                if (!channelItems.containsKey(channel.channeltype)) {
                    channelItems.put(channel.channeltype, new ArrayList<>());
                }
                channelItems.get(channel.channeltype).add(new MenuInfo(
                        null, // no image needed
                        channel.name,
                        channel.id));
            }
        }
        ui.setMenus(channelTypes, channelItems);
        ui.setDisplayMenu(displayChannels);

        autoUpdater.start(); // start if not already started

        onChannelSelect(channels.get(0).name, channels.get(0).id);
    }

    /**
     * This method acts as listener on the model, whenever tableau for
     * a channel has loaded. The tableau containing information about
     * each episode will be shown to the user on the ui.
     * @param channelID Channel id for tableau loaded. Will be -1 if
     *                  could not load tableau
     * @param episodes Episodes within the tableau. Will be null if
     *                 could not load tableau
     */
    @Override
    public void onTableauLoaded(int channelID, List<Episode> episodes) {
        List<TableauRow> tableauEpisodes = new ArrayList<>();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
        int nowIndex = 0;

        if (channelID == -1 || episodes == null) {
            setErrorMessage("Could not load information. " +
                            "Check internet connection");
            return;
        }

        for (int i = 0, episodesSize = episodes.size(); i < episodesSize; i++) {
            Episode episode = episodes.get(i);
            boolean hasHappened = false;

            if (episode.starttime.compareTo(ZonedDateTime.now()) < 0) {
                nowIndex = i;
                if (episode.endtime.compareTo(ZonedDateTime.now()) < 0) {
                    hasHappened = true;
                }
            }

            String time = episode.starttime.format(format);
            time += " - ";
            time += episode.endtime.format(format);
            tableauEpisodes.add(new TableauRow(time, episode.title,
                    episode.episodeid, !hasHappened));
        }

        ui.setTableauContent(tableauEpisodes);
        ui.setEpisodeSelected(nowIndex);
    }

    /**
     * Will show an error message to the user. Current implementation
     * show the message in the table, but future implementation might
     * be more standalone
     * @param message Error message to show to user
     */
    private void setErrorMessage(String message) {
        List<TableauRow> tableauEpisodes = new ArrayList<>();
        tableauEpisodes.add(new TableauRow("", message, -1, true));
        ui.clear();
        ui.setTableauContent(tableauEpisodes);
        ui.setEpisodeSelected(0);
    }

    /**
     * Main method. Will start up a RadioInfo
     */
    public static void main(String[] args) {
        new RadioInfo();
    }
}
