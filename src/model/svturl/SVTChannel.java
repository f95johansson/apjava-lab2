/*
 * File: SVTChannel.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.svturl;

public class SVTChannel extends SVTAPI {
    public SVTChannel() {
        super(SVTAPI.CHANNELS_URL);
    }

    public SVTChannel(int channelID) {
        super(SVTAPI.CHANNELS_URL+"/"+channelID);
    }
}
