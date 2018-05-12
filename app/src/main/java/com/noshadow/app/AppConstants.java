package com.noshadow.app;

import android.support.annotation.IntDef;

/**
 * Created by LucasZambon on 30/04/2018.
 */


public class AppConstants {

    public static final String TAG_BASE_JOB = "JOB_QUEUE";

    @IntDef({JobProority.HIGH, JobProority.MEDIUM, JobProority.LOW})
    public @interface JobProority {

        int HIGH = 1000;
        int MEDIUM = 100;
        int LOW = 10;
    }
}
