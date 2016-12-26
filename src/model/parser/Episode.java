/*
 * File: ScheduledEpisode.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.parser;

import java.time.ZonedDateTime;

public class Episode {

    public int episodeid = -1;
    public String title;
    public String subtitle;
    public String description;
    public String url;
    public String imageurl;
    public String imageurltemplate;
    public ZonedDateTime starttime;
    public ZonedDateTime endtime;
    public int programid;
    public String programName;
    public int channelid;
    public String channelName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Episode episode = (Episode) o;

        if (episodeid != episode.episodeid) return false;
        if (programid != episode.programid) return false;
        if (channelid != episode.channelid) return false;
        if (title != null ? !title.equals(episode.title) : episode.title != null)
            return false;
        if (subtitle != null ? !subtitle.equals(episode.subtitle) : episode.subtitle != null)
            return false;
        if (description != null ? !description.equals(episode.description) : episode.description != null)
            return false;
        if (url != null ? !url.equals(episode.url) : episode.url != null)
            return false;
        if (imageurl != null ? !imageurl.equals(episode.imageurl) : episode.imageurl != null)
            return false;
        if (imageurltemplate != null ? !imageurltemplate.equals(episode.imageurltemplate) : episode.imageurltemplate != null)
            return false;
        if (starttime != null ? !starttime.equals(episode.starttime) : episode.starttime != null)
            return false;
        if (endtime != null ? !endtime.equals(episode.endtime) : episode.endtime != null)
            return false;
        if (programName != null ? !programName.equals(episode.programName) : episode.programName != null)
            return false;
        return channelName != null ? channelName.equals(episode.channelName) : episode.channelName == null;

    }

    @Override
    public int hashCode() {
        int result = episodeid;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (subtitle != null ? subtitle.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (imageurl != null ? imageurl.hashCode() : 0);
        result = 31 * result + (imageurltemplate != null ? imageurltemplate.hashCode() : 0);
        result = 31 * result + (starttime != null ? starttime.hashCode() : 0);
        result = 31 * result + (endtime != null ? endtime.hashCode() : 0);
        result = 31 * result + programid;
        result = 31 * result + (programName != null ? programName.hashCode() : 0);
        result = 31 * result + channelid;
        result = 31 * result + (channelName != null ? channelName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "episodeid=" + episodeid +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", imageurltemplate='" + imageurltemplate + '\'' +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", programid=" + programid +
                ", programName='" + programName + '\'' +
                ", channelid=" + channelid +
                ", channelName='" + channelName + '\'' +
                '}';
    }
}
