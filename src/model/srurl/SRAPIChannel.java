/*
 * File: SVTChannel.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.srurl;

/**
 * Builds a URl for SR Channel API. Either a channel id can be specified for
 * information about that channel, or no id is specified to get information
 * about all channels
 */
public class SRAPIChannel extends SRAPI {

    /**
     * No channel specified, will result in all channels and their information
     */
    public SRAPIChannel() {
        super(SRAPI.CHANNELS_URL);
    }

    /**
     * A channel specified, will result that channel and its information
     */
    public SRAPIChannel(int channelID) {
        super(SRAPI.CHANNELS_URL+"/"+channelID);
    }
}
