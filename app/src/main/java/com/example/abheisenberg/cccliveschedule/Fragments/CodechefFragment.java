package com.example.abheisenberg.cccliveschedule.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abheisenberg.cccliveschedule.Adapters.CodechefAdapter;
import com.example.abheisenberg.cccliveschedule.DateOperations;
import com.example.abheisenberg.cccliveschedule.Models.Codechef;
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
public class CodechefFragment extends Fragment {
    
    public static final String TAG = "CCFrag";
    
    DatabaseReference reference;
    ArrayList<Codechef> contest_list;

    public CodechefFragment() {
        // Required empty public constructor
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_codechef, container, false);

        contest_list = new ArrayList<>();

        AdView ccAd = (AdView) rootView.findViewById(R.id.CC_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        ccAd.loadAd(adRequest);
        ccAd.setAdListener(adListener);

        RecyclerView CCrv = (RecyclerView) rootView.findViewById(R.id.CRrv);
        final CodechefAdapter adapter = new CodechefAdapter(getActivity(), contest_list);
        CCrv.setLayoutManager(new LinearLayoutManager(getActivity()));
        CCrv.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchCCDate(dataSnapshot);
                adapter.updateAdapter(contest_list);
                getActivity().findViewById(R.id.CCloadingPanel).setVisibility(View.GONE);
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

    private void fetchCCDate(DataSnapshot dataSnapshot) {
        DateOperations dtOperations = new DateOperations();

        for(DataSnapshot ds: dataSnapshot.getChildren()){

            String start_time = ds.child("start_time").getValue().toString();
            String end_time = ds.child("end_time").getValue().toString();

            Codechef contest = new Codechef(
                    ds.child("contest_code").getValue().toString(),
                    ds.child("contest_name").getValue().toString(),
                    start_time,
                    end_time,
                    checkStatus(start_time, end_time, dtOperations)
            );

            contest_list.add(contest);
        }
    }

    /*
      checkStatus() returns the following chars:
        'Upcoming': upcoming contest, not started yet
        'Active Now': active contest, not ended yet
        'Ended': ended contest
     */

    private String checkStatus(String start_time, String end_time, DateOperations dtOperations){

        dtOperations.setContest_date(start_time);

        if(dtOperations.isDateTimePassed()){
            dtOperations.setContest_date(end_time);

            if(dtOperations.isDateTimePassed()){
                return "Ended";
            } else {
                return "Active Now";
            }
        } else {
            return "Upcoming";
        }
    }

}
