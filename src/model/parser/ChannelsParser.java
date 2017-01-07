/*
 * File: ChannelsParser.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.parser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

/**
 * ChannelsParser is a specific XML parser for the SR API of channels
 */
public class ChannelsParser extends Parser<Channel> {

    private List<Channel> channels;

    /**
     * {@inheritDoc}
     */
    public ChannelsParser(InputStream stream) throws IOException,
                                                     XMLStreamException {
        super(stream);
    }

    /**
     * Parses the XML-file according to the SR API.
     * @return A list containing information about all the channels
     * @throws XMLStreamException If XML Stream encountered error
     */
    @Override
    public List<Channel> parse() throws XMLStreamException {
        XMLEventReader reader = getReader();

        channels = new ArrayList<>();

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                if (isElement(event.asStartElement(), "channel")) {
                    Iterator<Attribute> attributes =
                            event.asStartElement().getAttributes();

                    Channel channel = parseChannel(reader);
                    parseChannelAttributes(attributes, channel);
                    channels.add(channel);
                }
            }
        }

        return channels;
    }

    /**
     * Parses a <channel> element in the XML-file.
     * @param reader XML reader for the file
     * @return A Channel containing all the information from the XML-file
     * @throws XMLStreamException If XML Stream encountered error
     */
    private Channel parseChannel(XMLEventReader reader)
                                                    throws XMLStreamException {

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

    /**
     * Parses the attributes for a <channel> element
     * @param attributes Attributes to parse
     * @param channel The channel to add information to
     */
    private void parseChannelAttributes(Iterator<Attribute> attributes,
                                        Channel channel) {
        while (attributes.hasNext()) {
            Attribute attribute = attributes.next();
            if (isAttribute(attribute, "id")) {
                channel.id = Integer.parseInt(attribute.getValue());
            } else if (isAttribute(attribute, "name")) {
                channel.name = attribute.getValue();
            }
        }
    }
}
