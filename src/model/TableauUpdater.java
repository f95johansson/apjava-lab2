/*
 * File: model.TableauUpdater.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model;

import model.parser.Episode;
import model.parser.TableauParser;
import model.svturl.SVTTableau;

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
        void onTableauLoaded(int channelID, List<Episode> episodes);
    }

    private Thread updater;
    private TableauLoaded tableauLoadedListener;
    private AtomicInteger channelID = new AtomicInteger();
    private AtomicBoolean idChanged = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Episode> cachedEpisodes;

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
        SVTTableau api = new SVTTableau(channelID.get());
        api.disablePagination();
        api.setDate(LocalDate.now());
        try {
            URL url = api.build();
            TableauParser tableauParser = new TableauParser(url.openStream());

            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            List<Episode> episodes = tableauParser.parse();


            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            tableauLoadedListener.onTableauLoaded(channelID.get(), episodes); // TODO if null
            cacheEpisodes(episodes);

        } catch (FileNotFoundException e) {
            // Load empty list of episodes

            if (Thread.interrupted()) {
                return;
            }
            List<Episode> episodes = new ArrayList<>();
            tableauLoadedListener.onTableauLoaded(channelID.get(), episodes);
            cacheEpisodes(episodes);

        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
            // FIXME Empty catch
        }
    }

    private void cacheEpisodes(List<Episode> episodes) {
        cachedEpisodes = new CopyOnWriteArrayList<>(episodes);
    }

    public Episode getEpisode(int index) {
        return cachedEpisodes.get(index);
    }

    public void setTableauLoadedListener(TableauLoaded listener) {
        tableauLoadedListener = listener;
    }
}
