/*
 * File: Parser.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.parser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


/**
 * Abstract parser for XML parsing. Will ease XML parsing by matching
 * element names with corresponding action. Uses StAX XML reader.
 * @param <T> What should be produced with each element
 */
abstract class Parser<T> {

    private XMLEventReader eventReader;

    /**
     * Takes an inputstream which should contain an xml file, otherwise
     * exception is thrown.
     * @param stream Stream to read as a xml file
     * @throws IOException If something when wrong when reading inputstream
     * @throws XMLStreamException Something went wrong with basing
     *                            XML on inputstream
     */
    public Parser(InputStream stream) throws IOException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        eventReader = factory.createXMLEventReader(stream);
    }

    /**
     * Return the xml reader. Should be used by extending subclasses
     * @return The reader used to parse the xml file
     */
    XMLEventReader getReader() {
        return eventReader;
    }

    /**
     * Abstract method which should implement how the xml should be parsed
     * @return A list containing the desired output
     * @throws XMLStreamException If XML Stream encountered error
     */
    public abstract List<T> parse() throws XMLStreamException;

    /**
     * Method to ease the parsing of the xml file by mathing element
     * to action
     * @param reader Reader used to process xml file
     * @param mapElement Map of Element name to appropriate action
     * @param endElement Name of element to stop at
     * @throws XMLStreamException If XML Stream encountered error
     */
    void matchElements(XMLEventReader reader,
                       Map<String, Consumer<String>> mapElement,
                       String endElement) throws XMLStreamException {
        matchElements(reader, mapElement, null, endElement);
    }

    /**
     * Method to ease the parsing of the xml file by mathing element
     * to action
     * @param reader Reader used to process xml file
     * @param mapElement Map of Element name to appropriate action
     * @param mapAttributes Map of list of attributes to appropriate action
     * @param endElement Name of element to stop at
     * @throws XMLStreamException If XML Stream encountered error
     */
    void matchElements(XMLEventReader reader,
                       Map<String, Consumer<String>> mapElement,
                       Map<String, Consumer<Iterator<Attribute>>> mapAttributes,
                       String endElement) throws XMLStreamException {

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            if (event.isStartElement()) {
                StartElement element = event.asStartElement();

                if (mapAttributes != null) {
                    Consumer<Iterator<Attribute>> run = mapAttributes.get(
                            element.getName().getLocalPart());

                    // If there exist an action for these attributes:
                    if (run != null) {
                        run.accept(element.getAttributes());
                        continue;
                    }
                }

                Consumer<String> run = mapElement.get(
                        element.getName().getLocalPart());

                // If there exist an action for this element
                if (run != null) {
                    event = reader.nextEvent();
                    if (event.isCharacters()) {
                        run.accept(event.asCharacters().getData());
                    }
                }

            } else if (event.isEndElement()) {
                if (event.asEndElement().getName().getLocalPart()
                        .equalsIgnoreCase(endElement)) {
                    break;
                }
            }
        }
    }

    /**
     * Check if element match string name
     * @param event Element event
     * @param compare Name to compare with
     * @return True if they math, else false
     */
    boolean isElement(StartElement event, String compare) {
        return event.getName().getLocalPart().equalsIgnoreCase(compare);
    }


    /**
     * Check if element match string name
     * @param event Element event
     * @param compare Name to compare with
     * @return True if they math, else false
     */
    boolean isElement(EndElement event, String compare) {
        return event.getName().getLocalPart().equals(compare);
    }

    /**
     * Check if Attribute math string name
     * @param event Attribute event
     * @param compare Name to compare with
     * @return True if they math, else false
     */
    boolean isAttribute(Attribute event, String compare) {
        return event.getName().getLocalPart().equalsIgnoreCase(compare);
    }

    /**
     * Helper method to convert string to int
     */
    int toInt(String data) {
        return Integer.parseInt(data);
    }

    /**
     * Helper method to convert string to datetime
     * @param data String in form of the ISO standard:
     *             <Year>-<Month>-<Day>T<Hours>:<Minutes>:<Seconds><TimeZone>
     *             E.g. 16-01-23T20:56:11+01
     * @return A ZonedDateTime witch take the timezone into consideration
     */
    ZonedDateTime toDate(String data) {
        DateTimeFormatter format =
                DateTimeFormatter.ofPattern("y-M-d'T'H:m:sX");

        return ZonedDateTime.parse(data,
               format).withZoneSameInstant(ZoneId.systemDefault());
    }
}
