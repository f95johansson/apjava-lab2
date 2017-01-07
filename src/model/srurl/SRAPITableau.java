/*
 * File: SVTTableau.java
 * Author: Fredrik Johansson
 * Date: 2016-12-20
 */
package model.srurl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SRAPITableau extends SRAPI {

    public SRAPITableau(int channelID) {
        super(SRAPI.TABLEAU_URL);
        appendParameter("channelid", channelID);
    }

    public void setDate(LocalDate date) {
        String stringDate = date.format(DateTimeFormatter.ISO_DATE);
        appendParameter("date", stringDate);
    }
}
