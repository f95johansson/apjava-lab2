/*
 * File: model.TableauUpdater.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model;

import model.parser.Episode;
import model.parser.TableauParser;
import model.srurl.SRAPITableau;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TableauUpdater implements Runnable {

    public interface TableauLoaded {
        /**
         *
         * @param channelID Channel id for tableau loaded. Will be -1 if
         *                  could not read from xml/stream
         * @param episodes Episodes within tha tableau. Will be null if
         *                  could not read from xml/stream
         */
        void onTableauLoaded(int channelID, List<Episode> episodes);
    }

    private Thread updater;
    private TableauLoaded tableauLoadedListener;
    private AtomicInteger channelID = new AtomicInteger();
    private AtomicBoolean idChanged = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Episode> cachedEpisodes = new CopyOnWriteArrayList<>();

    public void setChannelToLoad(int id) {
        channelID.set(id);
        idChanged.set(true);
    }

    public void update() {
        if (updater == null || !updater.isAlive()) {
            startNewUpdateThread();

        } else if (idChanged.get()) {
            // is already updating and channelID has been changed
            updater.interrupt(); // close current updating thread

            startNewUpdateThread();
        }
    }

    private void startNewUpdateThread() {
        updater = new Thread(this);
        updater.start();
        idChanged.set(false);
    }

    @Override
    public void run() {
        SRAPITableau api = new SRAPITableau(channelID.get());
        api.disablePagination();
        api.setDate(LocalDate.now());
        try {
            URL url = api.build();
            TableauParser tableauParser = new TableauParser(url.openStream());

            // Must always test to see if interrupted before continuing
            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            List<Episode> episodes = tableauParser.parse();

            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            if (tableauLoadedListener != null) {
               tableauLoadedListener.onTableauLoaded(channelID.get(), episodes);
            }
            cacheEpisodes(episodes);

        } catch (FileNotFoundException e) {
            // Load empty list of episodes

            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            List<Episode> episodes = new ArrayList<>();
            tableauLoadedListener.onTableauLoaded(channelID.get(), episodes);
            cacheEpisodes(episodes);

        } catch (XMLStreamException | IOException e) {
            // could not read from stream
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            tableauLoadedListener.onTableauLoaded(-1, null);
        }
    }

    private void cacheEpisodes(List<Episode> episodes) {
        cachedEpisodes = new CopyOnWriteArrayList<>(episodes);
    }

    public Episode getEpisode(int index) {
        try {
            return cachedEpisodes.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void setTableauLoadedListener(TableauLoaded listener) {
        tableauLoadedListener = listener;
    }
}
