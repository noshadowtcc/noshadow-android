package com.noshadow.app.features;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.noshadow.app.R;
import com.noshadow.app.managers.ManagerHelper;
import com.noshadow.app.model.BluetoothConnected;
import com.noshadow.app.model.JobObject;
import com.noshadow.app.model.JobUpdate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    int _count = 0;

    ArrayList<JobObject> _itens;
    MaterialDialog progress;
    BackgrounditemsAdapter adapter;

    @BindView(R.id.rcv_bg_itens)
    RecyclerView _rcvBgItems;
    @BindView(R.id.textView_device)
    TextView textViewDevice;
    @BindView(R.id.textView_status)
    TextView textViewStatus;
    @BindView(R.id.card_view)
    CardView cardView;
    @BindView(R.id.rcv_root)
    LinearLayout rcvRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_items);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        ManagerHelper.getInstance().getBluetoothManager().connectBluetooth(this);

        showLoading("Conectando no dispositivo!");

        _itens = new ArrayList<>();

        for (JobObject item : ManagerHelper.getInstance().getNetworkJobManager().getJobItems()){
            _itens.add(item);
        }

        Collections.reverse(_itens);

//        Collections.sort(_itens, new Comparator<JobObject>() {
//            @Override
//            public int compare(JobObject t0, JobObject t1) {
//                return t0.getDate().compareTo(t1.getDate());
//            }
//        });

        _rcvBgItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BackgrounditemsAdapter(this, _itens);
        _rcvBgItems.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onUpdate(final JobUpdate event) {
        final Context context = this;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                _itens.clear();
                ArrayList<JobObject> itens = (ArrayList<JobObject>) ManagerHelper.getInstance().getNetworkJobManager().getJobItems();
                _itens.addAll(itens);
                adapter.notifyDataSetChanged();

                //TODO: ATUALIZAR A LISTA
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onConnected(final BluetoothConnected event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //TODO: ATUALIZAR A LISTA

                if(!event.getDeviceName().isEmpty()){
                    textViewDevice.setText(event.getDeviceName());
                    textViewStatus.setText("CONECTADO");
                    textViewStatus.setTextColor(getResources().getColor(R.color.green_light));
                    if (progress != null) {
                        progress.hide();
                    }
                }
                else{
                    textViewDevice.setText("");
                    textViewStatus.setText("SEM CONEXÃO");
                    textViewStatus.setTextColor(getResources().getColor(R.color.red));
                }



            }
        });
    }

    public void showLoading(String message) {
        if (progress == null) {
            progress = new MaterialDialog.Builder(this).title("Aguarde").content(message).widgetColor(getResources().getColor(R.color.colorPrimaryDark)).cancelable(false).progress(true, 0).show();
        } else {
            progress.setContent(message);
            progress.show();
        }
    }
}
