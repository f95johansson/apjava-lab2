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


/**
 * TableauUpdater takes care of downloading new tableau for selected
 * channel. Will run on its own thread and therefore uses a listener to
 * return information. Will only run one thread at a time, and will only
 * run thread when actually downloading, i.e. no waiting background thread.
 */
public class TableauUpdater implements Runnable {

    /**
     * Listener for when a tableau has loaded
     */
    public interface TableauLoaded {
        /**
         * Returns the tableau in form of a method call to listener
         * @param channelID Channel id for tableau loaded. Will be -1 if
         *                  could not read from xml/stream
         * @param episodes Episodes within the tableau. Will be null if
         *                  could not read from xml/stream
         */
        void onTableauLoaded(int channelID, List<Episode> episodes);
    }

    private Thread updater;
    private TableauLoaded tableauLoadedListener;
    private AtomicInteger channelID = new AtomicInteger(0);
    private AtomicBoolean idChanged = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Episode> cachedEpisodes =
            new CopyOnWriteArrayList<>();

    /**
     * Should be set before running {@link #update()}, otherwise update
     * will use id = 0.
     * @param id Id of channel to get schedule from
     */
    public void setChannelToLoad(int id) {
        channelID.set(id);
        idChanged.set(true);
    }

    /**
     * Will download a new tableau for the specified channel id,
     * on its own thread. Only one thread will run at a time. Calling
     * this method when update() is already downloading, and no new id
     * is set, this method will have no effect. Calling this method
     * when already downloading but a new id is set, old download will
     * close and a new download will begin with the new id.
     */
    public void update() {
        if (updater == null || !updater.isAlive()) {
            startNewUpdateThread();

        } else if (idChanged.get()) {
            // is already updating and channelID has been changed
            updater.interrupt(); // close current updating thread

            startNewUpdateThread();
        }
    }

    /**
     * Helper method for starting a new download thread.
     */
    private void startNewUpdateThread() {
        updater = new Thread(this);
        updater.start();
        idChanged.set(false);
    }

    /**
     * Downloads a new tableau and returns the result via the listener.
     * This method blocks the thread and should not be called outside
     * this class unless blocking is acceptable. Use {@link #update()} for
     * non-blocking use of this method.
     */
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

    /**
     * Cache loaded episode in a thread-safe way
     * @param episodes Episodes to cache
     */
    private void cacheEpisodes(List<Episode> episodes) {
        cachedEpisodes = new CopyOnWriteArrayList<>(episodes);
    }

    /**
     * Get episode from last loaded tableau. Thread-safe
     * @param index Index of episode to get
     * @return Episode on index, or null if no episode found on index
     */
    public Episode getEpisode(int index) {
        try {
            return cachedEpisodes.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Sets a listener for the result returned through {@link #update()}.
     * See {@link TableauLoaded} for specific returns. Only one listener
     * can be set at a time. Remove existing listener by setting
     * listener to null
     * @param listener Listener to listen to result from {@link #update()}
     */
    public void setTableauLoadedListener(TableauLoaded listener) {
        tableauLoadedListener = listener;
    }
}
