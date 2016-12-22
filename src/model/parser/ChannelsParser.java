/*
 * File: ChannelsParser.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.parser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

public class ChannelsParser extends Parser<Channel> {

    private List<Channel> channels;

    public ChannelsParser(InputStream stream) throws IOException, XMLStreamException {
        super(stream);
    }

    @Override
    public void parse() throws XMLStreamException {
        XMLEventReader reader = getReader();

        channels = new ArrayList<>();

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                if (isElement(event.asStartElement(), "channel")) {
                    channels.add(parseChannel(reader));
                }
            }
        }
    }

    @Override
    public Collection<Channel> getResult() {
        return channels;
    }

    private Channel parseChannel(XMLEventReader reader) throws XMLStreamException {

        Channel channel = new Channel();

        Map<String, Consumer<String>> mapElements = new HashMap<>();
        mapElements.put("id", data -> channel.id = toInt(data));
        mapElements.put("name", data -> channel.name = data);
        mapElements.put("image", data -> channel.image = data);
        mapElements.put("color", data -> channel.color = data);
        mapElements.put("tagline", data -> channel.tagline = data);
        mapElements.put("siteurl", data -> channel.siteurl = data);
        mapElements.put("channeltype", data -> channel.channeltype = data);

        matchElements(reader, mapElements, "channel");

        return channel;
    }
}
