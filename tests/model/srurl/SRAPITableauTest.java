/*
 * File: SVTTableauTest.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.srurl;


import org.junit.Test;

import java.net.URL;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class SRAPITableauTest {

    @Test
    public void shouldAddChannelID() throws Exception {
        SRAPITableau SRAPITableau = new SRAPITableau(165);
        assertEquals(
                new URL("http://api.sr.se/api/v2/scheduledepisodes?" +
                        "channelid=165"),
                SRAPITableau.build());
    }

    @Test
    public void shouldDisablePagination() throws Exception {
        SRAPITableau SRAPITableau = new SRAPITableau(165);
        SRAPITableau.disablePagination();
        assertEquals(
                new URL("http://api.sr.se/api/v2/scheduledepisodes?" +
                        "channelid=165&pagination=false"),
                SRAPITableau.build());
    }

    @Test
    public void shouldSetDate() throws Exception {
        SRAPITableau SRAPITableau = new SRAPITableau(165);
        SRAPITableau.setDate(LocalDate.of(2016, 10, 23));
        assertEquals(
                new URL("http://api.sr.se/api/v2/scheduledepisodes?" +
                        "channelid=165&date=2016-10-23"),
                SRAPITableau.build());
    }


}