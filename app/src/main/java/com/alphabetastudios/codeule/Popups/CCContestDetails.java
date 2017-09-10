package com.alphabetastudios.codeule.Popups;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.alphabetastudios.codeule.LinkMakers.WebpageOpener;
import com.alphabetastudios.codeule.R;

public class CCContestDetails extends AppCompatActivity {

    TextView tvCCOpensAtText, tvCCClosesAtText, tvWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cccontest_details);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.9), (int)(height*0.85));

        tvCCOpensAtText = (TextView) findViewById(R.id.tvCCOpensAtText);
        tvCCClosesAtText = (TextView) findViewById(R.id.tvCCClosesAtText);
        tvWebsite = (TextView) findViewById(R.id.tvWebsite);

        final String url;
        String opensAt, closesAt;

        Bundle extras = getIntent().getExtras();
        opensAt = extras.getString("OPENSAT");
        closesAt = extras.getString("CLOSESAT");
        url = extras.getString("URL");

        tvCCOpensAtText.setText(opensAt);
        tvCCClosesAtText.setText(closesAt);
        tvWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebpageOpener thisContestLink = new WebpageOpener(CCContestDetails.this, url);
                thisContestLink.LaunchLink();
            }
        });

    }
}
