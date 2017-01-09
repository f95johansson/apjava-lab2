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
            "    <channel id=\"132\" name=\"P1\">\n" +
            "      <image>http://sverigesradio.se/diverse/appdata/isidor/imag" +
                    "es/news_images/132/2186746_512_512.jpg</image>\n" +
            "      <color>31a1bd</color>\n" +
            "      <tagline>den talade kanalen</tagline>\n" +
            "      <siteurl>http://sverigesradio.se/p1</siteurl>\n" +
            "      <liveaudio id=\"132\">\n" +
            "        <url>http://sverigesradio.se/topsy/direkt/132.mp3</url>\n"+
            "        <statkey>webbradio/start/direkt/132_P1</statkey>\n" +
            "      </liveaudio>\n" +
            "      <scheduleurl>http://api.sr.se/api/v2/scheduledepisodes?cha" +
                    "nnelid=132</scheduleurl>\n" +
            "      <channeltype>Rikskanal</channeltype>\n" +
            "      <xmltvid>p1.sr.se</xmltvid>\n" +
            "    </channel>\n" +
            "</channels>";
    private static final List<Channel> realChannels = new ArrayList<>();

    @BeforeClass
    public static void setup() {
        Channel c1 = new Channel();

        c1.id = 132;
        c1.name = "P1";
        c1.image = "http://sverigesradio.se/diverse/appdata/isidor/images/new" +
                "s_images/132/2186746_512_512.jpg";
        c1.color = "31a1bd";
        c1.tagline = "den talade kanalen";
        c1.siteurl = "http://sverigesradio.se/p1";
        c1.channeltype = "Rikskanal";

        realChannels.add(c1);
    }

    @Test
    public void shouldParse() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                exampleXML.getBytes(StandardCharsets.UTF_8));
        ChannelsParser parser = new ChannelsParser(stream);
        List<Channel> channels = parser.parse();

        assertEquals(realChannels, channels);
    }
}