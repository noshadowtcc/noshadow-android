package com.noshadow.app.model;

import java.util.UUID;

/**
 * Created by guidpt on 9/22/17.
 */

public class JobUpdate {

    private int _jobCount;

    private UUID _identifier;
    private boolean _sended;


    public JobUpdate(int jobCount, UUID identifier, boolean sended){
        _jobCount = jobCount;
        _identifier = identifier;
        _sended = sended;
    }

    public int getJobCount() {
        return _jobCount;
    }

    public UUID getIdentifier() {
        return _identifier;
    }

    public void setIdentifier(UUID _identifier) {
        this._identifier = _identifier;
    }

    public boolean isSended() {
        return _sended;
    }

    public void setSended(boolean _sended) {
        this._sended = _sended;
    }
}
