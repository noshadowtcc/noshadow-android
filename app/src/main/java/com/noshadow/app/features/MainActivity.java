package com.noshadow.app.features;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.noshadow.app.R;
import com.noshadow.app.managers.ManagerHelper;
import com.noshadow.app.model.JobObject;
import com.noshadow.app.model.JobUpdate;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    int _count = 0;

    ArrayList<JobObject> _itens;

    @BindView(R.id.rcv_bg_itens)
    RecyclerView _rcvBgItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ManagerHelper.getInstance().getBluetoothManager().connectBluetooth(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onUpdate(final JobUpdate event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //TODO: ATUALIZAR A LISTA
            }
        });
    }
}
