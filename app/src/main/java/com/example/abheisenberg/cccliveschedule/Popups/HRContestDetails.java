package com.example.abheisenberg.cccliveschedule.Popups;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.example.abheisenberg.cccliveschedule.LinkMakers.URLMaker;
import com.example.abheisenberg.cccliveschedule.LinkMakers.WebpageOpener;
import com.example.abheisenberg.cccliveschedule.R;

public class HRContestDetails extends AppCompatActivity {

    TextView tvLongTextDetails, tvWebsite;
    String details, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrcontest_details);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.9), (int)(height*0.95));

        tvLongTextDetails = (TextView)findViewById(R.id.tvHRLongTextDetails);
        tvWebsite = (TextView)findViewById(R.id.tvWebsite);

        Bundle extras = getIntent().getExtras();
        details = extras.getString("DETAILS");
        url = extras.getString("URL");

        tvLongTextDetails.setText(details);
        tvWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebpageOpener thisContestLink = new WebpageOpener(HRContestDetails.this, URLMaker.HR_getFullLink(url));
                thisContestLink.LaunchLink();
            }
        });
    }

}
