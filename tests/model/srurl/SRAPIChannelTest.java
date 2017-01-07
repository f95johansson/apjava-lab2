/*
 * File: SVTChannelTest.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.srurl;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

public class SRAPIChannelTest {

    @Test
    public void shouldNotAddChannelID() throws Exception {
        SRAPIChannel SRAPIChannel = new SRAPIChannel();
        assertEquals(
                new URL("http://api.sr.se/api/v2/channels"),
                SRAPIChannel.build());
    }

    @Test
    public void shouldAddChannelID() throws Exception {
        SRAPIChannel SRAPIChannel = new SRAPIChannel(165);
        assertEquals(
                new URL("http://api.sr.se/api/v2/channels/165"),
                SRAPIChannel.build());
    }

    @Test
    public void shouldDisablePagination() throws Exception {
        SRAPITableau SRAPITableau = new SRAPITableau(165);
        SRAPITableau.disablePagination();
        assertEquals(
                new URL("http://api.sr.se/api/v2/scheduledepisodes?channelid=165&pagination=false"),
                SRAPITableau.build());
    }

}