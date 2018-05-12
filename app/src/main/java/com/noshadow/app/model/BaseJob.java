package com.noshadow.app.model;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

/**
 * Created by guidpt on 9/22/17.
 */

public abstract class BaseJob extends Job {

    private UUID _identifier;

    private String _displayName;

    private String _displayDescription;

    protected BaseJob(Params params) {
        super(params);

        //_identifier = UUID.randomUUID();
    }

    public UUID getIdentifier() {
        return _identifier;
    }

    public void setIdentifier(UUID _identifier) {
        this._identifier = _identifier;
    }

    public void sendError(){
        EventBus.getDefault().post(new JobError("Houve um erro ao enviar os itens, por favor, tente novamente mais tarde!"));
    }

    public String getDisplayName() {
        return _displayName;
    }

    public void setDisplayName(String displayName) {
        _displayName = displayName;
    }

    public String getDisplayDescription() {
        return _displayDescription;
    }

    public void setDisplayDescription(String _displayDescription) {
        this._displayDescription = _displayDescription;
    }
}
