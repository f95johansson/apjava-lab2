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
import view.RadioUI;
import view.TableauRow;
import view.Triplet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
            // TODO can't find episode
            return;
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
        List<Triplet<String, String, Integer>> channelItems = new ArrayList<>();

        List<Triplet<InputStream, String, Integer>> displayChannels = new ArrayList<>();

        for (Channel channel : channels) {
            if (channel.channeltype.equals(PRIMARY_CHANNEL)) {
                try {
                    URL imageURL = new URL(channel.image);
                    displayChannels.add(new Triplet<>(
                            imageURL.openStream(),
                            channel.name,
                            channel.id));
                } catch (IOException e) {
                    e.printStackTrace(); // FIXME empty catch
                }

            } else {

                channelTypes.add(channel.channeltype);
                channelItems.add(new Triplet<>(
                        channel.channeltype,
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
        DateTimeFormatter format = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT); // HH:MM
        int nowIndex = 0;

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

    public static void main(String[] args) {
        new RadioInfo();
    }
}
