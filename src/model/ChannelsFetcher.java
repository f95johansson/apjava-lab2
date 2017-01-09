/*
 * File: ChannelsFetcher.java
 * Author: Fredrik Johansson
 * Date: 2016-12-23
 */
package model;

import model.parser.Channel;
import model.parser.ChannelsParser;
import model.srurl.SRAPIChannel;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ChannelsFetcher takes care of downloading channels. Will run on
 * its own thread and therefore uses a listener to
 * return information. Will only run one thread at a time, and will only
 * run thread when actually downloading, i.e. no waiting background thread.
 */
public class ChannelsFetcher implements Runnable {


    /**
     * Listener for when channels has loaded
     */
    public interface ChannelsLoaded {
        /**
         * Returns channels in form of a method call to listener
         * @param channels List of channels loaded. Will be null if
         *                 could not read from url/stream
         */
        void onChannelsLoaded(List<Channel> channels);
    }

    private Thread fetcher;
    private ChannelsLoaded channelsLoadedListener;
    private Map<Integer, Channel> cachedChannels;

    /**
     * Will download channels from the SR API, on its own thread.
     * Only one thread will run at a time. Calling this method when
     * fetch() is already downloading will have no effect.
     */
    public void fetch() {
        if (fetcher == null || !fetcher.isAlive()) {
            fetcher = new Thread(this);
            fetcher.start();
        }
    }

    /**
     * Downloads channels and returns the result via the listener.
     * This method blocks the thread and should not be called outside
     * this class unless blocking is acceptable. Use {@link #fetch()} for
     * non-blocking use of this method.
     */
    @Override
    public void run() {
        SRAPIChannel api = new SRAPIChannel();
        api.disablePagination();
        try {
            URL url = api.build();
            ChannelsParser parser = new ChannelsParser(url.openStream());
            List<Channel> channels = parser.parse();

            if (channelsLoadedListener != null) {
                channelsLoadedListener.onChannelsLoaded(channels);
            }
            cacheChannels(channels);

        } catch (XMLStreamException | IOException e) {
            // could not read from stream
            channelsLoadedListener.onChannelsLoaded(null);
        }
    }

    /**
     * Cache loaded channels in a thread-safe way
     * @param channels Channels to cache
     */
    private void cacheChannels(Collection<Channel> channels) {
        if (cachedChannels == null) {
            cachedChannels = new ConcurrentHashMap<>();
        } else {
            cachedChannels.clear();
        }

        for (Channel channel : channels) {
            cachedChannels.put(channel.id, channel);
        }
    }

    /**
     * Get channel information from last loaded channels. Thread-safe
     * @param id Id of channel to get
     * @return Channel with id, or null if no channel fetched with such id
     */
    public Channel getChannel(int id) {
        if (cachedChannels == null || cachedChannels.size() == 0) {
            throw new IllegalStateException(
                      "Must fetch before getting specific channel");
        }
        return cachedChannels.get(id);
    }

    /**
     * Sets a listener for the result returned through {@link #fetch()}.
     * See {@link ChannelsLoaded} for specific returns. Only one listener
     * can be set at a time. Remove existing listener by setting
     * listener to null
     * @param listener Listener to listen to result from {@link #fetch()}
     */
    public void setChannelsLoadedListener(ChannelsLoaded listener) {
        channelsLoadedListener = listener;
    }
}
