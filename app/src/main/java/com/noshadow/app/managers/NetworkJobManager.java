package com.noshadow.app.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noshadow.app.R;
import com.noshadow.app.model.BaseJob;
import com.noshadow.app.model.JobError;
import com.noshadow.app.model.JobObject;
import com.noshadow.app.model.JobUpdate;
import com.noshadow.app.services.NoShadowJobService;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by guidpt on 9/22/17.
 */

public class NetworkJobManager extends BaseManager {

    private JobManager _jobManager;

    private List<JobObject> _itens;

    private final Gson _gson = new Gson();

    public NetworkJobManager(Context context) {
        _context = context;
	    getJobManager();
        init();
    }

    public void init()
    {
	    Type type = new TypeToken<ArrayList<JobObject>>(){}.getType();
	    _itens = _gson.fromJson(ManagerHelper.getInstance().getConfigManager().getConfigString(R.string.netwotk_job_count),  type);
	    if(_itens == null)
		    _itens = new ArrayList<>();

	    new Thread(runnable).start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

	        int count = _itens.size();

            if(count == 0){
                _itens = new ArrayList<>();

                ManagerHelper.getInstance().getConfigManager().setConfig(getString(R.string.netwotk_job_count), _itens);
                EventBus.getDefault().post(new JobUpdate(getJobCount(), null, false));
            }
        }
    };

    public synchronized JobManager getJobManager() {
        if (_jobManager == null) {
            configureJobManager();
        }
        return _jobManager;
    }

    private void configureJobManager() {
        Configuration.Builder builder = new Configuration.Builder(_context)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void v(String text, Object... args) {

                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120);//wait 2 minute
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.scheduler(FrameworkJobSchedulerService.createSchedulerFor(_context, NoShadowJobService.class), true);
        }
        else {
//            int enableGcm = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(_context);
//            if (enableGcm == ConnectionResult.SUCCESS) {
//                builder.scheduler(GcmJobSchedulerService.createSchedulerFor(_context, GcmJobService.class), true);
//            }
        }
        _jobManager = new JobManager(builder.build());
    }

    public <T extends BaseJob> void addJobInBackground(T job){

        boolean added = false;

        for (JobObject item : _itens){
            if(job.getIdentifier().equals(item.getId()))
                added = true;
        }

        if(!added)
            _itens.add(0, new JobObject(job.getIdentifier(), job.getDisplayName(), job.getDisplayDescription()));

        if(_itens.size() > 20){
            _itens.remove(_itens.size()-1);
        }

        EventBus.getDefault().post(new JobUpdate(getJobCount(), job.getIdentifier(), false));
        _jobManager.addJobInBackground(job);

        ManagerHelper.getInstance().getConfigManager().setConfig(getString(R.string.netwotk_job_count), _itens);
    }

    public <T extends BaseJob> void addJobInBackgroundWithoutNotifiy(T job){

//        boolean added = false;
//
//        for (UUID item : _itens){
//            if(job.getIdentifier().equals(item))
//                added = true;
//        }
//
//        if(!added)
//            _itens.add(job.getIdentifier());
//
//        EventBus.getDefault().post(new JobUpdate(getJobCount()));
        _jobManager.addJobInBackground(job);

        //ManagerHelper.getInstance().getConfigManager().setConfig(getString(R.string.netwotk_job_count), _itens);
    }

    public void removeJob(UUID identifier){
        for(JobObject item: _itens){
            if(item.getId().compareTo(identifier) == 0){
               item.setSended(true);
                break;
            }
        }
        ManagerHelper.getInstance().getConfigManager().setConfig(getString(R.string.netwotk_job_count), _itens);
        EventBus.getDefault().post(new JobUpdate(getJobCount(), identifier, true));
    }

    public int getJobCount() {
        int count = 0;
        for(JobObject item: _itens){
            if(!item.isSended()){
              count++;
            }
        }
        return count;
    }

    public List<JobObject> getJobItems() {
        return _itens;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void start(){
        if(_jobManager != null && isNetworkAvailable()) {
            _jobManager.start();
            new Thread(runnable).start();
        }
        else{
            EventBus.getDefault().post(new JobError("Não há sinal de internet para enviar os itens, tente novamente mais tarde!"));
        }
    }


    public void clearAllItens() {
        if(_itens == null)
            _itens = new ArrayList<>();

        ManagerHelper.getInstance().getConfigManager().setConfig(getString(R.string.netwotk_job_count), _itens);
    }
}
