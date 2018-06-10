package com.noshadow.app.model;

import android.text.format.DateFormat;

import java.util.Date;
import java.util.UUID;

/**
 * Created by LIGA on 05/02/2018.
 */

public class JobObject {

    private UUID id;

    private String displayName;

    private String displayDescription;

    private Date date;

    private boolean sended;

    public JobObject(UUID id, String displayName, String displayDescription){
        this.id = id;
        this.displayName = displayName;
        this.sended = false;
        this.date = new Date();
        this.displayDescription = displayDescription;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public boolean isSended() {
        return sended;
    }

    public void setSended(boolean sended) {
        this.sended = sended;
    }

    public String getDisplayDescription() {
        return displayDescription;
    }

    public void setDisplayDescription(String displayDescription) {
        this.displayDescription = displayDescription;
    }

    public Date getDate() {
        return date;
    }

    public String getDateFormated() {
        return DateFormat.format("dd/MM/yyyy HH:mm:ss", date).toString();
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
