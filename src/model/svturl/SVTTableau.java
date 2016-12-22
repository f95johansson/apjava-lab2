/*
 * File: SVTTableau.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.svturl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SVTTableau extends SVTAPI {

    public SVTTableau(int channelID) {
        super(SVTAPI.TABLUE_URL);
        appendParameter("channelid", channelID);
    }

    public void setDate(LocalDate date) {
        String stringDate = date.format(DateTimeFormatter.ISO_DATE);
        appendParameter("date", stringDate);
    }
}
