/*
 * File: Channel.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.parser;

/**
 * Simple class for containing all possible information about an channel
 */
public class Channel {

    public int id = 0;
    public String name = "";
    public String image = "";
    public String color = "";
    public String tagline = "";
    public String siteurl = "";
    public String channeltype = "";

    /**
     * {@inheritDoc}
     *
     * Generated with IntelliJ 2016.2
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Channel channel = (Channel) o;

        if (id != channel.id) return false;
        if (name != null ? !name.equals(channel.name) : channel.name != null)
            return false;
        if (image != null ? !image.equals(channel.image) : channel.image != null)
            return false;
        if (color != null ? !color.equals(channel.color) : channel.color != null)
            return false;
        if (tagline != null ? !tagline.equals(channel.tagline) : channel.tagline != null)
            return false;
        if (siteurl != null ? !siteurl.equals(channel.siteurl) : channel.siteurl != null)
            return false;
        return channeltype != null ? channeltype.equals(channel.channeltype) : channel.channeltype == null;

    }

    /**
     * {@inheritDoc}
     *
     * Generated with IntelliJ 2016.2
     */
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (tagline != null ? tagline.hashCode() : 0);
        result = 31 * result + (siteurl != null ? siteurl.hashCode() : 0);
        result = 31 * result + (channeltype != null ? channeltype.hashCode() : 0);
        return result;
    }
}
