/*
 * File: TableauParser.java
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
 * ChannelsParser is a specific XML parser for the SR API of tableau's
 */
public class TableauParser extends Parser<Episode> {

    private List<Episode> episodes;

    /**
     * {@inheritDoc}
     */
    public TableauParser(InputStream stream) throws IOException, XMLStreamException {
        super(stream);
    }

    /**
     * Parses the XML-file according to the SR API.
     * @return A list containing information about all the episodes
     * @throws XMLStreamException If XML Stream encountered error
     */
    @Override
    public List<Episode> parse() throws XMLStreamException {
        XMLEventReader reader = getReader();

        episodes = new ArrayList<>();

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                if (isElement(event.asStartElement(), "scheduledepisode")) {
                    episodes.add(parseScheduledEpisode(reader));
                }
            }
        }

        return episodes;
    }

    /**
     * Parses a <scheduledepisode> in the XML-file
     * @param reader XML reader for the file
     * @return An Episode containing all the episode information
     * @throws XMLStreamException If XML Stream encountered error
     */
    private Episode parseScheduledEpisode(XMLEventReader reader)
                                                     throws XMLStreamException {

        Episode episode = new Episode();

        Map<String, Consumer<String>> mapElements = new HashMap<>();
        Map<String, Consumer<Iterator<Attribute>>> mapAttributes =
                                                                new HashMap<>();

        mapElements.put("episodeid", data -> episode.episodeid = toInt(data));
        mapElements.put("title", data -> episode.title = data);
        mapElements.put("starttimeutc",
                        data -> episode.starttime = toDate(data));
        mapElements.put("endtimeutc", data -> episode.endtime = toDate(data));
        mapElements.put("subtitle", data -> episode.subtitle = data);
        mapElements.put("description", data -> episode.description = data);
        mapElements.put("url", data -> episode.url = data);
        mapElements.put("imageurl", data -> episode.imageurl = data);
        mapElements.put("imageurltemplate",
                        data -> episode.imageurltemplate = data);

        mapAttributes.put("program", attrs -> parseProgram(attrs, episode));
        mapAttributes.put("channel", attrs -> parseChannel(attrs, episode));

        matchElements(reader, mapElements, mapAttributes, "scheduledepisode");

        return episode;
    }

    /**
     * Parse a <program> elements attributes
     * @param attributes Attributes to parse
     * @param episode Episode to add information to
     */
    private void parseProgram(Iterator<Attribute> attributes, Episode episode) {
        while (attributes.hasNext()) {
            Attribute attribute = attributes.next();
            if (isAttribute(attribute, "id")) {
                episode.programid = Integer.parseInt(attribute.getValue());
            } else if (isAttribute(attribute, "name")) {
                episode.programName = attribute.getValue();
            }
        }
    }

    /**
     * Parse a <channel> elements attributes
     * @param attributes Attributes to parse
     * @param episode Episode to add information to
     */
    private void parseChannel(Iterator<Attribute> attributes, Episode episode) {
        while (attributes.hasNext()) {
            Attribute attribute = attributes.next();
            if (isAttribute(attribute, "id")) {
                episode.channelid = toInt(attribute.getValue());
            } else if (isAttribute(attribute, "name")) {
                episode.channelName = attribute.getValue();
            }
        }
    }
}
