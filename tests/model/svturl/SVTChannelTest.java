/*
 * File: SVTChannelTest.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.svturl;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

public class SVTChannelTest {

    @Test
    public void shouldAddChannelID() throws Exception {
        SVTChannel svtChannel = new SVTChannel(165);
        assertEquals(
                new URL("http://api.sr.se/api/v2/channels/165"),
                svtChannel.build());
    }

    @Test
    public void shouldDisablePagination() throws Exception {
        SVTTableau svtTableau = new SVTTableau(165);
        svtTableau.disablePagination();
        assertEquals(
                new URL("http://api.sr.se/api/v2/scheduledepisodes?channelid=165&pagination=false"),
                svtTableau.build());
    }

}