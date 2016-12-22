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
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

abstract class Parser<T> {

    private XMLEventReader eventReader;

    public Parser(InputStream stream) throws IOException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        eventReader = factory.createXMLEventReader(stream);
    }

    XMLEventReader getReader() {
        return eventReader;
    }

    public abstract void parse() throws XMLStreamException;

    public abstract Collection<T> getResult();

    void matchElements(XMLEventReader reader,
                       Map<String, Consumer<String>> mapElement,
                       String endElement) throws XMLStreamException {
        matchElements(reader, mapElement, null, endElement);
    }

    void matchElements(XMLEventReader reader,
                       Map<String, Consumer<String>> mapElement,
                       Map<String, Consumer<Iterator<Attribute>>> mapAttributes,
                       String endElement) throws XMLStreamException {

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            if (event.isStartElement()) {
                StartElement element = event.asStartElement();

                if (mapAttributes != null) {
                    Consumer<Iterator<Attribute>> run = mapAttributes.get(element.getName().getLocalPart());

                    if (run != null) {
                        run.accept(element.getAttributes());
                        continue;
                    }
                }

                Consumer<String> run = mapElement.get(element.getName().getLocalPart());

                if (run != null) {
                    event = reader.nextEvent();
                    if (event.isCharacters()) {
                        run.accept(event.asCharacters().getData());
                    }
                }

            } else if (event.isEndElement()) {
                if (event.asEndElement().getName().getLocalPart().equalsIgnoreCase(endElement)) {
                    break;
                }
            }
        }
    }

    boolean isElement(StartElement event, String compare) {
        return event.getName().getLocalPart().equalsIgnoreCase(compare);
    }

    boolean isElement(EndElement event, String compare) {
        return event.getName().getLocalPart().equals(compare);
    }

    boolean isAttribute(Attribute event, String compare) {
        return event.getName().getLocalPart().equalsIgnoreCase(compare);
    }

    int toInt(String data) {
        return Integer.parseInt(data);
    }

    ZonedDateTime toDate(String data) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("y-M-d'T'H:m:sX");

        return ZonedDateTime.parse(data,
               format).withZoneSameInstant(ZoneId.systemDefault());
    }
}
