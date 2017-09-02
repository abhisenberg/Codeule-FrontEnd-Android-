package com.example.abheisenberg.cccliveschedule.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abheisenberg.cccliveschedule.Adapters.HackerrankAdapter;
import com.example.abheisenberg.cccliveschedule.DateOperations;
import com.example.abheisenberg.cccliveschedule.Models.Hackerrank;
import com.example.abheisenberg.cccliveschedule.R;
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
public class HackerrankFragment extends Fragment {

    DatabaseReference reference;
    ArrayList<Hackerrank> contest_schedule;

    public static final String TAG = "HRFrag";

    public HackerrankFragment() {
        // Required empty public constructor
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hackerrank, container, false);

        contest_schedule = new ArrayList<>();

        AdView HRAd = (AdView) rootView.findViewById(R.id.HR_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        HRAd.loadAd(adRequest);
        HRAd.setAdListener(adListener);

        RecyclerView HRrv = (RecyclerView)rootView.findViewById(R.id.HRrv);
        final HackerrankAdapter adapter = new HackerrankAdapter(getActivity(), contest_schedule);
        HRrv.setLayoutManager(new LinearLayoutManager(getActivity()));
        HRrv.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchHRdata(dataSnapshot);
                adapter.updateAdapter(contest_schedule);
                getActivity().findViewById(R.id.HRloadingPanel).setVisibility(View.GONE);
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

    public void fetchHRdata(DataSnapshot dataSnapshot){
        DateOperations dateTimeOperations = new DateOperations();

        for(DataSnapshot ds: dataSnapshot.getChildren()){
            String date_str = ds.child("date_to_compare").getValue().toString();
            dateTimeOperations.setContest_date(date_str);

            Hackerrank contest = new Hackerrank(
                    ds.child("contest_name").getValue().toString(),
                    ds.child("start_date").getValue().toString(),
                    ds.child("url").getValue().toString(),
                    ds.child("description").getValue().toString(),
                    dateTimeOperations.isDateTimePassed(),
                    ds.child("date_to_compare").getValue().toString()
            );
        contest_schedule.add(contest);
        }
    }

}
