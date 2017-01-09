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
import java.time.format.FormatStyle;
import java.util.*;

public class RadioInfo implements RadioUI.EpisodeSelect,
                                  RadioUI.ChannelSelect,
                                  TableauUpdater.TableauLoaded,
                                  ChannelsFetcher.ChannelsLoaded {

    private static final String PRIMARY_CHANNEL = "Rikskanal";

    private TableauUpdater updater;
    private AutoUpdater autoUpdater;
    private ChannelsFetcher fetcher;
    private RadioUI ui;

    public RadioInfo() {
        updater = new TableauUpdater();
        updater.setTableauLoadedListener(this);

        autoUpdater = new AutoUpdater(updater);

        fetcher = new ChannelsFetcher();
        fetcher.setChannelsLoadedListener(this);

        ui = new RadioUI();
        ui.setEpisodeSelectListener(this);
        ui.setChannelSelectListener(this);

        fetcher.fetch();
    }

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
            /* image = null */
        }

        ui.setEpisodeContent(episode.title, episode.subtitle, episode.description, image);
    }

    @Override
    public void onChannelSelect(String name, int id) {
        updater.setChannelToLoad(id);
        updater.update();

        Channel channel = fetcher.getChannel(id);
        ui.setColor(channel.color);
        ui.setTitle(channel.name);

        ui.clear();
    }

    @Override
    public void onChannelsLoaded(List<Channel> channels) {
        Set<String> channelTypes = new LinkedHashSet<>();
        // uses set for uniqueness and linked to preserve order
        Map<String, List<MenuInfo>> channelItems = new HashMap<>();

        List<MenuInfo> displayChannels = new ArrayList<>();

        if (channels == null) {
            setErrorMessage("Could not load information. Check internet connection");
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

        ui.setChannelSelected(0);

        autoUpdater.start();
    }

    @Override
    public void onTableauLoaded(int channelID, List<Episode> episodes) {
        List<TableauRow> tableauEpisodes = new ArrayList<>();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm"); // HH:MM
        int nowIndex = 0;

        if (channelID == -1 || episodes == null) {
            setErrorMessage("Could not load information. Check internet connection");
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
            tableauEpisodes.add(new TableauRow(time, episode.title, episode.episodeid, !hasHappened));
        }

        ui.setTableauContent(tableauEpisodes);
        ui.setEpisodeSelected(nowIndex);
    }

    private void setErrorMessage(String message) {
        List<TableauRow> tableauEpisodes = new ArrayList<>();
        tableauEpisodes.add(new TableauRow("", message, -1, true));
        ui.setTableauContent(tableauEpisodes);
        ui.setEpisodeSelected(0);
    }

    public static void main(String[] args) {
        new RadioInfo();
    }
}
