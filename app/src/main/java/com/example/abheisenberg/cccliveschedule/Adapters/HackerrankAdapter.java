package com.example.abheisenberg.cccliveschedule.Adapters;

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

import com.example.abheisenberg.cccliveschedule.DateOperations;
import com.example.abheisenberg.cccliveschedule.LinkMakers.URLMaker;
import com.example.abheisenberg.cccliveschedule.Models.Hackerrank;
import com.example.abheisenberg.cccliveschedule.Notifications.NotificationMaker;
import com.example.abheisenberg.cccliveschedule.Popups.HRContestDetails;
import com.example.abheisenberg.cccliveschedule.R;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by abheisenberg on 28/7/17.
 */

public class HackerrankAdapter extends RecyclerView.Adapter<HackerrankAdapter.HRViewHolder> {

    public static final String TAG = "HRAd";
    Context context;
    ArrayList<Hackerrank> arrayList;

    NotificationMaker notificationMaker;

    public HackerrankAdapter(Context context, ArrayList<Hackerrank> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.notificationMaker = new NotificationMaker(context);
    }

    public void  updateAdapter(ArrayList<Hackerrank> newList){
        this.arrayList = newList;
        notifyDataSetChanged();
    }

    @Override
    public HRViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new HRViewHolder(li.inflate(R.layout.list_item_hackerrank, parent, false));
    }

    @Override
    public void onBindViewHolder(HRViewHolder holder, int position) {
        final Hackerrank thisContest = arrayList.get(position);

        if(thisContest.getHas_started()){          //Contest has already started
            holder.tvHRStatus.setTextColor(Color.GREEN);
            holder.tvHRStatus.setText("Active Now");
            holder.tvHRStart_ed.setText("Started At:");
        } else {                                   //Contest has not yet started
            holder.tvHRStatus.setTextColor(context.getResources().getColor(R.color.colorSilver));
            holder.tvHRStatus.setText("Upcoming");
            holder.tvHRStart_ed.setText("Starts At:");
        }

        holder.tvHRDate.setText(thisContest.getStarts_at());
        holder.tvHRCName.setText(thisContest.getContest_name());
        holder.thisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent popupIntent = new Intent(context, HRContestDetails.class);
                Bundle extras = new Bundle();
                extras.putString("DETAILS", thisContest.getDescription());
                extras.putString("URL", thisContest.getContest_url());
                popupIntent.putExtras(extras);
                context.startActivity(popupIntent);
            }
        });
        holder.ivHRAlarmIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateOperations dateOperations = new DateOperations();
                dateOperations.markInCalendarHR(context, thisContest.getContest_name(), thisContest.getFormatted_date());
            }
        });

        long timeToStart = DateOperations.getRemainingMSecs(thisContest.getFormatted_date()) - 15*60*1000;
        Log.d(TAG, "timeToStart "+thisContest.getContest_name()+" -> "+timeToStart);
        if(timeToStart > 0){
            notificationMaker.ScheduleNotification(
                    "HackerRank: "+thisContest.getContest_name(),
                    URLMaker.HR_getFullLink(thisContest.getContest_url()),
                    timeToStart
            );
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class HRViewHolder extends RecyclerView.ViewHolder {

        TextView tvHRCName, tvHRDate, tvHRStatus, tvHRStart_ed;
        View thisView;
        ImageView ivHRAlarmIcon;

        public HRViewHolder(View itemView) {
            super(itemView);

            thisView = itemView;
            tvHRCName = ((TextView)itemView.findViewById(R.id.tvHRContestName));
            tvHRDate = ((TextView)itemView.findViewById(R.id.tvHRDate));
            tvHRStatus = ((TextView)itemView.findViewById(R.id.tvHRStatus));
            tvHRStart_ed = ((TextView)itemView.findViewById(R.id.tvHRStart_ed));
            ivHRAlarmIcon = ((ImageView)itemView.findViewById(R.id.ivHRAlarmIcon));
        }
    }

}
