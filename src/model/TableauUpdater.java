package model;/*
 * File: model.TableauUpdater.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */

import model.parser.Episode;
import model.parser.TableauParser;
import model.svturl.SVTTableau;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collection;

public class TableauUpdater {

    public interface TableauLoaded {
        void onTableauLoaded(Collection<Episode> episodes);
    }

    private TableauLoaded tableauLoadedListener;
    private int channelID;

    public TableauUpdater() {
    }

    public void setChannelToLoad(int id) {
        channelID = id;
    }

    public void update() {
        SVTTableau api = new SVTTableau(channelID);
        api.disablePagination();
        api.setDate(LocalDate.now());
        try {
            URL url = api.build();
            TableauParser tableauParser = new TableauParser(url.openStream());
            Collection<Episode> episodes = tableauParser.getResult();
            tableauLoadedListener.onTableauLoaded(episodes);
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
            // FIXME Empty catch
        }
    }

    public void setTablueLoadedListener(TableauLoaded listener) {
        tableauLoadedListener = listener;
    }
}
