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
import android.widget.Toast;

import com.example.abheisenberg.cccliveschedule.DateOperations;
import com.example.abheisenberg.cccliveschedule.LinkMakers.URLMaker;
import com.example.abheisenberg.cccliveschedule.LinkMakers.WebpageOpener;
import com.example.abheisenberg.cccliveschedule.Models.Codechef;
import com.example.abheisenberg.cccliveschedule.Notifications.NotificationMaker;
import com.example.abheisenberg.cccliveschedule.Popups.CCContestDetails;
import com.example.abheisenberg.cccliveschedule.R;

import java.util.ArrayList;

/**
 * Created by abheisenberg on 28/7/17.
 */

public class CodechefAdapter extends RecyclerView.Adapter<CodechefAdapter.CCViewHolder> {
    public static final String TAG = "CCAd";
    Context context;
    ArrayList<Codechef> arrayList;

    NotificationMaker notificationMaker = new NotificationMaker(context);

    public CodechefAdapter(Context context, ArrayList<Codechef> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.notificationMaker = new NotificationMaker(context);
    }

    public void updateAdapter(ArrayList<Codechef> arrayList){
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public CCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new CCViewHolder(li.inflate(R.layout.list_item_codechef, parent, false));
    }

    @Override
    public void onBindViewHolder(CCViewHolder holder, int position) {
        final Codechef thisContest = arrayList.get(position);

        String status = thisContest.getStatus();
        switch (status){
            case "Active Now":
                holder.tvcstatus.setTextColor(Color.GREEN);
                break;

            case "Ended":
                holder.tvcstatus.setTextColor(Color.RED);
                break;

            case "Upcoming":
                holder.tvcstatus.setTextColor(context.getResources().getColor(R.color.colorSilver));
                break;
        }

        final String fullURL = URLMaker.CC_getFullLink(thisContest.getContest_code());

        holder.tvcstatus.setText(status);
        holder.tvcstartsat.setText(thisContest.getStart_time());
        holder.tvcname.setText(thisContest.getContest_name());
        holder.tvccode.setText(thisContest.getContest_code());

        final String contestURL = URLMaker.CC_getFullLink(thisContest.getContest_code());
        holder.thisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent popupIntent = new Intent(context, CCContestDetails.class);
                Bundle extras = new Bundle();
                extras.putString("OPENSAT", thisContest.getStart_time());
                extras.putString("CLOSESAT", thisContest.getEnd_time());
                extras.putString("URL", contestURL);
                popupIntent.putExtras(extras);
                context.startActivity(popupIntent);
            }
        });


        holder.ivCCAlarmClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateOperations dateOperations = new DateOperations();
                dateOperations.markInCalendarHECC(context, thisContest.getContest_name(), thisContest.getStart_time(),
                        thisContest.getEnd_time());
            }
        });

        //Notify user only if the date has not been passed
        long timeToAlarmAndNoti = DateOperations.getRemainingMSecs(thisContest.getStart_time()) - 15*60*1000;
        Log.d(TAG, "timeToAlarmAndNoti "+thisContest.getContest_name()+" -> "+timeToAlarmAndNoti);
        if (timeToAlarmAndNoti > 0){
            notificationMaker.ScheduleNotification(
                    "CodeChef: "+thisContest.getContest_name(),
                    fullURL,
                    timeToAlarmAndNoti
            );
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CCViewHolder extends RecyclerView.ViewHolder {

        TextView tvcname, tvccode, tvcstartsat, tvcstatus;
        View thisView;
        ImageView ivCCAlarmClock;

        public CCViewHolder(View itemView) {
            super(itemView);

            ivCCAlarmClock = (ImageView) itemView.findViewById(R.id.ivCCAlarmIcon);
            tvccode = (TextView) itemView.findViewById(R.id.tvCCContestCode);
            tvcname = (TextView) itemView.findViewById(R.id.tvCCContestName);
            tvcstartsat = (TextView) itemView.findViewById(R.id.tvCCStartsAt);
            tvcstatus = ((TextView)itemView.findViewById(R.id.tvCCContestStatus));
            thisView = itemView;
        }
    }

}
