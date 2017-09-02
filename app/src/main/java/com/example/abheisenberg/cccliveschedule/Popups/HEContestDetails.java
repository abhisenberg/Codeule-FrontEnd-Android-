package com.example.abheisenberg.cccliveschedule.Popups;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.example.abheisenberg.cccliveschedule.LinkMakers.WebpageOpener;
import com.example.abheisenberg.cccliveschedule.R;

public class HEContestDetails extends AppCompatActivity {

    TextView tvHEOpensAtText, tvHEClosesAtText, tvHEDurationText, tvHELongTextDetails, tvWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hecontest_details);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.9), (int)(height*0.85));

        tvHEOpensAtText = (TextView) findViewById(R.id.tvHEOpensAtText);
        tvHEClosesAtText = (TextView) findViewById(R.id.tvHEClosesAtText);
        tvHEDurationText = (TextView) findViewById(R.id.tvHEDurationText);
        tvHELongTextDetails = (TextView) findViewById(R.id.tvHELongTextDetails);
        tvWebsite = (TextView) findViewById(R.id.tvWebsite);

        final String url;
        String opensAt, closesAt, duration, details;

        Bundle extras = getIntent().getExtras();
        opensAt = extras.getString("OPENSAT");
        closesAt = extras.getString("CLOSESAT");
        duration = extras.getString("DURATION");
        details = extras.getString("DETAILS");
        url = extras.getString("URL");

        tvHEOpensAtText.setText(opensAt);
        tvHEClosesAtText.setText(closesAt);
        tvHEDurationText.setText(duration);
        tvHELongTextDetails.setText(details);
        tvWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebpageOpener thisContestLink = new WebpageOpener(HEContestDetails.this, url);
                thisContestLink.LaunchLink();
            }
        });

    }
}
