package com.alphabetastudios.codeule.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphabetastudios.codeule.Adapters.HackerearthAdapter;
import com.alphabetastudios.codeule.DateOperations;
import com.alphabetastudios.codeule.Models.Hackerearth;
import com.alphabetastudios.codeule.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HackerearthFragment extends Fragment {

    DatabaseReference reference;
    ArrayList<Hackerearth> contest_schedule;
    public static final String TAG = "HEFrag";

    public HackerearthFragment() {
        // Required empty public constructor
    }

    public void setReference (DatabaseReference reference){
        this.reference = reference;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hackerearth, container, false);
        contest_schedule = new ArrayList<>();

        AdView HEAd = (AdView) rootView.findViewById(R.id.HE_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        HEAd.loadAd(adRequest);
        HEAd.setAdListener(adListener);

        RecyclerView HErv = (RecyclerView) rootView.findViewById(R.id.HErv);
        final HackerearthAdapter adapter = new HackerearthAdapter(getActivity(), contest_schedule);
        HErv.setLayoutManager(new LinearLayoutManager(getActivity()));
        HErv.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchHEdata(dataSnapshot);
                adapter.updateAdapter(contest_schedule);
                getActivity().findViewById(R.id.HEloadingPanel).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    AdListener adListener = new AdListener() {
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            Log.d(TAG, "onAdClosed: ");
        }

        @Override
        public void onAdFailedToLoad(int i) {
            super.onAdFailedToLoad(i);
            Log.d(TAG, "onAdFailedToLoad: ");
        }

        @Override
        public void onAdLeftApplication() {
            super.onAdLeftApplication();
            Log.d(TAG, "onAdLeftApplication: ");
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            Log.d(TAG, "onAdOpened: ");
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            Log.d(TAG, "onAdLoaded: ");
        }
    };

    private void fetchHEdata(DataSnapshot dataSnapshot) {
        DateOperations dtOperations = new DateOperations();

        for(DataSnapshot ds: dataSnapshot.getChildren()){
            String contest_date = ds.child("date").getValue().toString();
            String date_to_compare = ds.child("date_to_compare").getValue().toString();
            String end_date = "";
            String end_date_formatted = "";
            String type = ds.child("type").getValue().toString();
            String name = ds.child("name").getValue().toString();

            char contestStatus = type.equals("CodeArena")? 'i' : checkStatus(contest_date, date_to_compare, dtOperations);
            /*
            CodeArena contest is an exception since it is always active but the website returns an end-date which is just a minute
            ahead of the current time. So we have to initialize it manually.
             */
            switch (contestStatus){
                case 'e':
                    contest_date = "Ended";
                    break;

                case 'i':
                    contest_date = "Active Now";
                    end_date = "Please check website";
                    break;

                case 'a':
                    end_date = date_to_compare;
                    break;
            }

            if(ds.child("details").hasChild("end_date")){
                end_date_formatted = ds.child("details").child("end_date").getValue().toString();
            } else {
                end_date_formatted = "";
            }

            Log.d(TAG, "fetchHEdata: "+name);

            Hackerearth thisContest = new Hackerearth(
                    contest_date,
                    date_to_compare,
                    name,
                    end_date,
                    type,
                    ds.child("url").getValue().toString(),
                    ds.child("details").child("Opens at:").getValue().toString(),
                    ds.child("details").child("Closes at:").getValue().toString(),
                    ds.child("details").child("Duration:").getValue().toString(),
                    ds.child("details").child("about").getValue().toString(),
                    end_date_formatted
            );

            contest_schedule.add(thisContest);
        }
    }

    /*
    checkStatus() returns the following characters:
        u: Upcoming event, Start DateTime to be shown
        a: Active Now, End DateTime available to be shown
        i: Active Now, but End DateTime is not available to be shown, leave it blank
        e: Contest ended
     */

    private char checkStatus(String contest_date,String date_to_compare, DateOperations dtOpereations){
        dtOpereations.setContest_date(date_to_compare);

        if(!contest_date.equals("Active Now")){
            if(!dtOpereations.isDateTimePassed()){
                return 'u';         //Start date has not passed till now, upcoming event
            } else {
                //The data on server is outdated and the contest has started, but
                //since it is outdated we dont have ending date-time
                return 'i';
            }
        } else {
            if(!dtOpereations.isDateTimePassed()){
                return 'a';         //End datetime has not passed yet, contest is Active
            } else {
                return 'e';         //End datetime has passed, contest has ended
            }
        }
    }

}
