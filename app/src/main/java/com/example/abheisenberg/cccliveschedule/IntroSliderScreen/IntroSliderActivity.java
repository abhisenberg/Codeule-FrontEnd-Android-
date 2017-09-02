package com.example.abheisenberg.cccliveschedule.IntroSliderScreen;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abheisenberg.cccliveschedule.MainActivity;
import com.example.abheisenberg.cccliveschedule.R;
import com.example.abheisenberg.cccliveschedule.Settings.UserPreferences;

public class IntroSliderActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Adapter adapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;

    public static final String TAG = "IntroAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!UserPreferences.getIsFirstLaunch(this)){
            startActivity(new Intent(this, MainActivity.class));
        }
        UserPreferences.setIsFirstLaunch(this, false);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro_slider);

        viewPager = (ViewPager) findViewById(R.id.introAct_viewPager);
        dotsLayout = (LinearLayout) findViewById(R.id.introAct_bottomDots);

        layouts = new int[] {
                R.layout.intro_screen_1,
                R.layout.intro_screen_2,
                R.layout.intro_screen_3,
                R.layout.intro_screen_4,
                R.layout.intro_screen_5,
                R.layout.intro_screen_6
        };

        addBottomDots(0);

        adapter = new Adapter(this, layouts);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);

                RelativeLayout rl = (RelativeLayout) findViewById(R.id.introAct_RelLayout);
                if(position == layouts.length-1){
                    Button btGotIt = new Button(IntroSliderActivity.this);
                    btGotIt.setId(R.id.btGotIt);
                    btGotIt.setOnClickListener(btGotItListener);
                    btGotIt.setText("Got it!");

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    rl.addView(btGotIt, lp);
                } else {
                    if(rl.findViewById(R.id.btGotIt) != null){
                        rl.removeView(rl.findViewById(R.id.btGotIt));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        Log.d(TAG, "addBottomDots: ");
        dots = new TextView[layouts.length];
        dotsLayout.removeAllViews();
        for(int i=0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.primary_material_dark_1));
            dotsLayout.addView(dots[i], ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (dots.length > 0){
            Log.d(TAG, "Setting white at page num "+(currentPage+1));
            dots[currentPage].setTextColor(getResources().getColor(R.color.colorSilver));
        }
    }

    private View.OnClickListener btGotItListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IntroSliderActivity.this.startActivity(new Intent(IntroSliderActivity.this, MainActivity.class));
        }
    };
}
