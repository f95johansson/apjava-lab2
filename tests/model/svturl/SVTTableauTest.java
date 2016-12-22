/*
 * File: SVTTableauTest.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.svturl;


import org.junit.Test;

import java.net.URL;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class SVTTableauTest {

    @Test
    public void shouldAddChannelID() throws Exception {
        SVTTableau svtTableau = new SVTTableau(165);
        assertEquals(
                new URL("http://api.sr.se/api/v2/scheduledepisodes?channelid=165"),
                svtTableau.build());
    }

    @Test
    public void shouldDisablePagination() throws Exception {
        SVTTableau svtTableau = new SVTTableau(165);
        svtTableau.disablePagination();
        assertEquals(
                new URL("http://api.sr.se/api/v2/scheduledepisodes?channelid=165&pagination=false"),
                svtTableau.build());
    }

    @Test
    public void shouldSetDate() throws Exception {
        SVTTableau svtTableau = new SVTTableau(165);
        svtTableau.setDate(LocalDate.of(2016, 10, 23));
        assertEquals(
                new URL("http://api.sr.se/api/v2/scheduledepisodes?channelid=165&date=2016-10-23"),
                svtTableau.build());
    }


}