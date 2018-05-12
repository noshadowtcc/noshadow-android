package com.noshadow.app.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.noshadow.app.AppConstants;
import com.noshadow.app.api.ApiService;
import com.noshadow.app.managers.ManagerHelper;

import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Response;

public class LocationJob extends BaseJob {

    private LocationPayload _payload;
    public LocationJob(LocationPayload payload,
                       UUID identifier,
                       String displayname,
                       String displayDesc) {
        super(new Params(AppConstants.JobProority.HIGH).requireNetwork().persist());

        _payload = payload;

        if(identifier == null)
            setIdentifier(UUID.randomUUID());
        else
            setIdentifier(identifier);

        if(!displayname.isEmpty())
            setDisplayName(displayname);

        if(!displayDesc.isEmpty())
            setDisplayDescription(displayDesc);
    }
    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        final LocationJob self = this;

        try{

            String url = ManagerHelper.getInstance().getBaseUrl();

            ApiService service = ManagerHelper.getInstance().getNetworkManager().createRetrofitService(ApiService.class, url, 2);

            Call<EmptyProxy> s = service.sendLocation(_payload);

            Response<EmptyProxy> response = s.execute();

            EmptyProxy body = response.body();

            if(response.code() == 200){
                ManagerHelper.getInstance().getNetworkJobManager().removeJob(self.getIdentifier());
            }
            else if(response.code() == 400){
                ManagerHelper.getInstance().getNetworkJobManager().removeJob(self.getIdentifier());
            }
            else {
                sendError();
                ManagerHelper.getInstance()
                        .getNetworkJobManager()
                        .addJobInBackground(new LocationJob(_payload, getIdentifier(), getDisplayName(), getDisplayDescription()));
            }
        }

        catch (Exception e){
            ManagerHelper.getInstance().getNetworkJobManager().addJobInBackground(new LocationJob(_payload, getIdentifier(), getDisplayName(), getDisplayDescription()));
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.RETRY;
    }
}
