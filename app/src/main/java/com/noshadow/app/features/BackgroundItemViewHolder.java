package com.noshadow.app.features;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.noshadow.app.R;
import com.noshadow.app.model.JobObject;
import com.noshadow.app.model.JobUpdate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LIGA on 07/02/2018.
 */

public class BackgroundItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    @BindView(R.id.textView_title)
    TextView txtTitle;

    @BindView(R.id.txt_status)
    TextView txtStatus;

    @BindView(R.id.txt_date)
    TextView txtDate;

    @BindView(R.id.txt_description)
    TextView txtDescription;

    @BindView(R.id.img_sended)
    ImageView imgSended;

    @BindView(R.id.img_pending)
    ImageView imgPending;

    private JobObject model;

    private Context context;

    public BackgroundItemViewHolder(final Context context, View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        EventBus.getDefault().register(this);
        this.context = context;
        ButterKnife.bind(this, itemView);
    }

    public void bind(JobObject model){
        this.model = model;

        txtTitle.setText(model.getDisplayName());

        setStatus(model.isSended());


        txtDate.setText(model.getDateFormated());
        txtDescription.setText(model.getDisplayDescription());
    }

    private void setStatus(boolean isSended) {

        this.model.setSended(isSended);

        if(isSended){
            imgSended.setVisibility(View.VISIBLE);
            imgPending.setVisibility(View.INVISIBLE);
            txtStatus.setText("Enviado");
            txtStatus.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

        }
        else{
            imgSended.setVisibility(View.INVISIBLE);
            imgPending.setVisibility(View.VISIBLE);
            txtStatus.setText("Pendente");
            txtStatus.setTextColor(context.getResources().getColor(R.color.orange_pending));
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onUpdate(final JobUpdate event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
               if(event.getIdentifier() != null && event.getIdentifier().equals(model.getId())) {
                   setStatus(event.isSended());
               }
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
