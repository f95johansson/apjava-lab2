/*
 * File: ChannelsParserTest.java
 * Author: Fredrik Johansson
 * Date: 2017-01-08
 */
package model.parser;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChannelsParserTest {

    private static final String exampleXML =
            "<channels>\n" +
            "  <channel id=\"132\" name=\"P1\">\n" +
            "    <image>http://sverigesradio.se/diverse/appdata/isidor/imag" +
                    "es/news_images/132/2186746_512_512.jpg</image>\n" +
            "    <color>31a1bd</color>\n" +
            "    <tagline>den talade kanalen</tagline>\n" +
            "    <siteurl>http://sverigesradio.se/p1</siteurl>\n" +
            "    <liveaudio id=\"132\">\n" +
            "      <url>http://sverigesradio.se/topsy/direkt/132.mp3</url>\n"+
            "      <statkey>webbradio/start/direkt/132_P1</statkey>\n" +
            "    </liveaudio>\n" +
            "    <scheduleurl>http://api.sr.se/api/v2/scheduledepisodes?cha" +
                    "nnelid=132</scheduleurl>\n" +
            "    <channeltype>Rikskanal</channeltype>\n" +
            "    <xmltvid>p1.sr.se</xmltvid>\n" +
            "  </channel>\n" +
            "  <channel id=\"163\" name=\"P2\">\n" +
            "    <image>http://static-cdn.sr.se/sida/images/163/2186754_512" +
                    "_512.jpg?preset=api-default-square</image>\n" +
            "    <imagetemplate>http://static-cdn.sr.se/sida/images/163/218" +
                    "6754_512_512.jpg</imagetemplate>\n" +
            "    <color>ff5a00</color>\n" +
            "    <siteurl>http://sverigesradio.se/p2</siteurl>\n" +
            "    <liveaudio id=\"163\">\n" +
            "      <url>http://sverigesradio.se/topsy/direkt/163.mp3</url>\n" +
            "      <statkey>/app/direkt/p2[k(163)]</statkey>\n" +
            "    </liveaudio>\n" +
            "    <scheduleurl>http://api.sr.se/v2/scheduledepisodes?channel" +
                    "id=163</scheduleurl>\n" +
            "    <channeltype>Rikskanal</channeltype>\n" +
            "    <xmltvid>p2.sr.se</xmltvid>\n" +
            "  </channel>" +
            "</channels>";
    private static final List<Channel> realChannels = new ArrayList<>();

    @BeforeClass
    public static void setup() {
        Channel c1 = new Channel();
        Channel c2 = new Channel();

        c1.id = 132;
        c1.name = "P1";
        c1.image = "http://sverigesradio.se/diverse/appdata/isidor/images/new" +
                "s_images/132/2186746_512_512.jpg";
        c1.color = "31a1bd";
        c1.tagline = "den talade kanalen";
        c1.siteurl = "http://sverigesradio.se/p1";
        c1.channeltype = "Rikskanal";

        c2.id = 163;
        c2.name = "P2";
        c2.image = "http://static-cdn.sr.se/sida/images/163/2186754_512" +
                "_512.jpg?preset=api-default-square";
        c2.color = "ff5a00";
        c2.siteurl = "http://sverigesradio.se/p2";
        c2.channeltype = "Rikskanal";

        realChannels.add(c1);
        realChannels.add(c2);
    }

    @Test
    public void shouldParseID() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                exampleXML.getBytes(StandardCharsets.UTF_8));
        ChannelsParser parser = new ChannelsParser(stream);
        List<Channel> channels = parser.parse();

        assertEquals(realChannels.get(0).id, channels.get(0).id);
        assertEquals(realChannels.get(1).id, channels.get(1).id);
    }

    @Test
    public void shouldParseName() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                exampleXML.getBytes(StandardCharsets.UTF_8));
        ChannelsParser parser = new ChannelsParser(stream);
        List<Channel> channels = parser.parse();

        assertEquals(realChannels.get(0).name, channels.get(0).name);
        assertEquals(realChannels.get(1).name, channels.get(1).name);
    }

    @Test
    public void shouldParseColor() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                exampleXML.getBytes(StandardCharsets.UTF_8));
        ChannelsParser parser = new ChannelsParser(stream);
        List<Channel> channels = parser.parse();

        assertEquals(realChannels.get(0).color, channels.get(0).color);
        assertEquals(realChannels.get(1).color, channels.get(1).color);
    }

    // more specific tests could be added

    @Test
    public void shouldParseOneChannel() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                exampleXML.getBytes(StandardCharsets.UTF_8));
        ChannelsParser parser = new ChannelsParser(stream);
        List<Channel> channels = parser.parse();

        assertEquals(realChannels.get(0), channels.get(0));
    }

    @Test
    public void shouldParseMultipleChannels() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                exampleXML.getBytes(StandardCharsets.UTF_8));
        ChannelsParser parser = new ChannelsParser(stream);
        List<Channel> channels = parser.parse();

        assertEquals(realChannels, channels);
    }

    @Test
    public void shouldParseEmptyXML() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                "".getBytes(StandardCharsets.UTF_8));
        ChannelsParser parser = new ChannelsParser(stream);
        List<Channel> channels = parser.parse();

        assertEquals(new ArrayList<Channel>(), channels);
    }
}