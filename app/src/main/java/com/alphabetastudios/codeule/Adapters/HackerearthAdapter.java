package com.alphabetastudios.codeule.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphabetastudios.codeule.DateOperations;
import com.alphabetastudios.codeule.LinkMakers.URLMaker;
import com.alphabetastudios.codeule.Models.Hackerearth;
import com.alphabetastudios.codeule.Notifications.NotificationMaker;
import com.alphabetastudios.codeule.Popups.HEContestDetails;
import com.alphabetastudios.codeule.R;

import java.util.ArrayList;

/**
 * Created by abheisenberg on 28/7/17.
 */

public class HackerearthAdapter extends RecyclerView.Adapter<HackerearthAdapter.HEViewHolder> {

    public static final String TAG = "HEad";

    Context context;
    ArrayList<Hackerearth> arrayList;

    NotificationMaker notificationMaker;

    public HackerearthAdapter(Context context, ArrayList<Hackerearth> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.notificationMaker = new NotificationMaker(context);
    }

    public void updateAdapter(ArrayList<Hackerearth> arrayList){
        this.arrayList = arrayList;
        Log.d(TAG, "list size: "+getItemCount());
        notifyDataSetChanged();
    }

    @Override
    public HEViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //TODO: Complete the inflation for both types of contests
        return new HEViewHolder(li.inflate(R.layout.list_item_hackerearth_upcoming, parent, false));
    }


    @Override
    public void onBindViewHolder(HEViewHolder holder, int position) {
        final Hackerearth thisContest = arrayList.get(position);

        holder.tvCName.setText(thisContest.getName());
        holder.tvCType.setText(thisContest.getType());

        if(thisContest.getDate().equals("Active Now")){
            holder.tvCSWheading.setTextColor(Color.GREEN);
            holder.tvCSWheading.setText("Active Now");
            holder.tvCSWTime.setText("Ends At: "+thisContest.getEnds_at());
        } else if (thisContest.getDate().equals("Ended")) {
            holder.tvCSWheading.setTextColor(Color.RED);
            holder.tvCSWheading.setText("Ended");
            holder.tvCSWTime.setText("");
        } else {
            holder.tvCSWheading.setTextColor(context.getResources().getColor(R.color.colorSilver));
            holder.tvCSWheading.setText("Starts At:");
            holder.tvCSWTime.setText(thisContest.getDate());
        }

        final String contestURL = URLMaker.HE_getFullLink(thisContest.getUrl());
        holder.thisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent popupIntent = new Intent(context, HEContestDetails.class);
                Bundle extras = new Bundle();
                extras.putString("DETAILS", thisContest.getDetails_about());
                extras.putString("OPENSAT", thisContest.getDetails_opensAt());
                extras.putString("CLOSESAT", thisContest.getDetails_closesAt());
                extras.putString("DURATION", thisContest.getDetails_duration());
                extras.putString("URL", contestURL);
                popupIntent.putExtras(extras);
                context.startActivity(popupIntent);
            }
        });

        holder.ivHEAlarmIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateOperations dateOperations = new DateOperations();
                Log.d(TAG, "Calendar marking: Start date -> "+thisContest.getFormatted_date());
                Log.d(TAG, "end date -> "+thisContest.getDetails_closesAtFormatted());
                dateOperations.markInCalendarHECC(context, thisContest.getName(), thisContest.getFormatted_date(),
                        thisContest.getDetails_closesAtFormatted());
            }
        });

        long timeToStart = DateOperations.getRemainingMSecs(thisContest.getFormatted_date()) - 15*60*1000;
        Log.d(TAG, "timeToStart "+thisContest.getName()+" -> "+timeToStart);
        if(timeToStart > 0){
            notificationMaker.ScheduleNotification(
                    "HackerEarth: "+thisContest.getName(),
                    contestURL,
                    timeToStart);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class HEViewHolder extends RecyclerView.ViewHolder {

        TextView tvCName, tvCType, tvCSWheading, tvCSWTime;
        View thisView;
        ImageView ivHEAlarmIcon;

        public HEViewHolder(View itemView) {
            super(itemView);

            ivHEAlarmIcon = (ImageView) itemView.findViewById(R.id.ivHEAlarmIcon);
            tvCName = ((TextView)itemView.findViewById(R.id.tvHEContestName));
            tvCType = ((TextView)itemView.findViewById(R.id.tvHEContestType));
            tvCSWheading = ((TextView)itemView.findViewById(R.id.tvHEWhenHeading));
            tvCSWTime = ((TextView)itemView.findViewById(R.id.tvHEWhenTime));
            thisView = itemView;
        }
    }

}
