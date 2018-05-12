package com.noshadow.app.services;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.noshadow.app.managers.ManagerHelper;

/**
 * Created by guidpt on 9/22/17.
 */

public class NoShadowJobService extends FrameworkJobSchedulerService {


    //    @NonNull
    @Override
    protected JobManager getJobManager() {
        return ManagerHelper.getInstance().getNetworkJobManager().getJobManager();
    }
}