/*
 * File: ChannelsFetcher.java
 * Author: Fredrik Johansson
 * Date: 2016-12-23
 */
package model;

import model.parser.Channel;
import model.parser.ChannelsParser;
import model.svturl.SVTChannel;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelsFetcher implements Runnable {

    public interface ChannelsLoaded {
        void onChannelsLoaded(List<Channel> channels);
    }

    private Thread fetcher;
    private ChannelsLoaded channelsLoadedListener;
    private Map<Integer, Channel> cachedChannels;

    public void fetch() {
        if (fetcher == null || !fetcher.isAlive()) {
            fetcher = new Thread(this);
            fetcher.start();
        }
    }

    @Override
    public void run() {
        SVTChannel api = new SVTChannel();
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
            e.printStackTrace();
            // FIXME empty catch
        }
    }

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

    public Channel getChannel(int id) {
        if (cachedChannels == null || cachedChannels.size() == 0) {
            throw new IllegalStateException("Must fetch before getting specific channel");
        }
        return cachedChannels.get(id);
    }

    public void setChannelsLoadedListener(ChannelsLoaded listener) {
        channelsLoadedListener = listener;
    }
}
