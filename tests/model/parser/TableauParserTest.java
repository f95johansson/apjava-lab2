/*
 * File: TableauParserTest.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.parser;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TableauParserTest {

    private static final String exampleXML =
            "<sr>\n" +
            "  <copyright>Copyright Sveriges Radio 2012. All rights reserved." +
                    "</copyright>\n" +
            "  <schedule>\n" +
            "    <scheduledepisode>\n" +
            "      <episodeid>22441</episodeid>\n" +
            "      <title>Ekonyheter </title>\n" +
            "      <starttimeutc>2012-09-19T04:00:00Z</starttimeutc>\n" +
            "      <endtimeutc>2012-09-19T04:03:00Z</endtimeutc>\n" +
            "      <program id=\"83\" name=\"Ekot\" />\n" +
            "      <channel id=\"164\" name=\"P3\" />\n" +
            "    </scheduledepisode>\n" +
            "    <scheduledepisode>\n" +
            "      <episodeid>128761</episodeid>\n" +
            "      <title>P3 Musik</title>\n" +
            "      <starttimeutc>2012-09-19T04:05:00Z</starttimeutc>\n" +
            "      <endtimeutc>2012-09-19T04:30:00Z</endtimeutc>\n" +
            "      <program id=\"4323\" name=\"P3 Musik\" />\n" +
            "      <channel id=\"164\" name=\"P3\" />\n" +
            "      <imageurl>http://sverigesradio.se/sida/images/4835/3650649" +
                    "_2048_1152.jpg?preset=api-default-square</imageurl>\n" +
            "      <imageurltemplate>http://sverigesradio.se/sida/images/4835" +
                    "/3650649_2048_1152.jpg</imageurltemplate>\n" +
            "    </scheduledepisode>\n" +
            "  </schedule>\n" +
            "</sr>";
    private static final List<Episode> realEpisodes = new ArrayList<>();

    @BeforeClass
    public static void setup() {
        Episode e1 = new Episode();
        Episode e2 = new Episode();

        e1.episodeid = 22441;
        e1.title = "Ekonyheter ";
        e1.starttime = ZonedDateTime.of(2012, 9, 19, 6, 0, 0, 0,
                       ZoneId.systemDefault());
        e1.endtime = ZonedDateTime.of(2012, 9, 19, 6, 3, 0, 0,
                     ZoneId.systemDefault());
        e1.programid = 83;
        e1.programName = "Ekot";
        e1.channelid = 164;
        e1.channelName = "P3";

        e2.episodeid = 128761;
        e2.title = "P3 Musik";
        e2.starttime = ZonedDateTime.of(2012, 9, 19, 6, 5, 0, 0,
                       ZoneId.systemDefault());
        e2.endtime = ZonedDateTime.of(2012, 9, 19, 6, 30, 0, 0,
                     ZoneId.systemDefault());
        e2.programid = 4323;
        e2.programName = "P3 Musik";
        e2.channelid = 164;
        e2.channelName = "P3";
        e2.imageurl = "http://sverigesradio.se/sida/images/4835/3650649_2048_" +
                "1152.jpg?preset=api-default-square";
        e2.imageurltemplate = "http://sverigesradio.se/sida/images/4835/36506" +
                "49_2048_1152.jpg";

        realEpisodes.add(e1);
        realEpisodes.add(e2);
    }

    @Test
    public void shouldParseID() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                exampleXML.getBytes(StandardCharsets.UTF_8));
        TableauParser parser = new TableauParser(stream);
        List<Episode> episodes = parser.parse();

        assertEquals(realEpisodes.get(0).episodeid, episodes.get(0).episodeid);
        assertEquals(realEpisodes.get(1).episodeid, episodes.get(1).episodeid);
    }

    @Test
    public void shouldParseTitle() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                exampleXML.getBytes(StandardCharsets.UTF_8));
        TableauParser parser = new TableauParser(stream);
        List<Episode> episodes = parser.parse();

        assertEquals(realEpisodes.get(0).title, episodes.get(0).title);
        assertEquals(realEpisodes.get(1).title, episodes.get(1).title);
    }

    @Test
    public void shouldParseTime() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                exampleXML.getBytes(StandardCharsets.UTF_8));
        TableauParser parser = new TableauParser(stream);
        List<Episode> episodes = parser.parse();

        assertEquals(realEpisodes.get(0).starttime, episodes.get(0).starttime);
        assertEquals(realEpisodes.get(0).endtime, episodes.get(0).endtime);
        assertEquals(realEpisodes.get(1).starttime, episodes.get(1).starttime);
        assertEquals(realEpisodes.get(1).endtime, episodes.get(1).endtime);
    }

    // more specific tests could be added

    @Test
    public void shouldParseOneEpisode() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                exampleXML.getBytes(StandardCharsets.UTF_8));
        TableauParser parser = new TableauParser(stream);
        List<Episode> episodes = parser.parse();

        assertEquals(realEpisodes.get(0), episodes.get(0));
    }

    @Test
    public void shouldParseMultipleEpisodes() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                exampleXML.getBytes(StandardCharsets.UTF_8));
        TableauParser parser = new TableauParser(stream);
        List<Episode> episodes = parser.parse();

        assertEquals(realEpisodes, episodes);
    }

    @Test
    public void shouldParseEmptyXML() throws Exception {
        InputStream stream = new ByteArrayInputStream(
                "".getBytes(StandardCharsets.UTF_8));
        TableauParser parser = new TableauParser(stream);
        List<Episode> episodes = parser.parse();

        assertEquals(new ArrayList<Episode>(), episodes);
    }
}