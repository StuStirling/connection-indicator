package com.stustirling.connectionindicator.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.stustirling.connectionindicator.ConnectionIndicatorView;

public class MainActivity extends AppCompatActivity {

    private ConnectionIndicatorView one;
    private ConnectionIndicatorView two;
    private ConnectionIndicatorView three;
    private ConnectionIndicatorView four;
    private ConnectionIndicatorView five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        one = (ConnectionIndicatorView) findViewById(R.id.ci_ma_one);
        two = (ConnectionIndicatorView) findViewById(R.id.ci_ma_two);
        three = (ConnectionIndicatorView) findViewById(R.id.ci_ma_three);
        four = (ConnectionIndicatorView) findViewById(R.id.ci_ma_four);
        five = (ConnectionIndicatorView) findViewById(R.id.ci_ma_five);

        findViewById(R.id.btn_ma_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( one.isSearching() ) {
                    stopSearching();
                } else {
                    startSearching();
                }
            }
        });

        ((ToggleButton)findViewById(R.id.tb_ma_on)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean allOn) {
                displayAllOn(allOn);
            }
        });
    }


    private void startSearching() {
        one.startSearching();
        two.startSearching();
        three.startSearching();
        four.startSearching();
        five.startSearching();
    }

    private void stopSearching() {
        one.stopSearching();
        two.stopSearching();
        three.stopSearching();
        four.stopSearching();
        five.stopSearching();
    }

    private void displayAllOn( boolean allOn ) {
        if ( allOn ) {
            one.displayConnectionLevel(one.getBarCount());
            two.displayConnectionLevel(two.getBarCount());
            three.displayConnectionLevel(three.getBarCount());
            four.displayConnectionLevel(four.getBarCount());
            five.displayConnectionLevel(five.getBarCount());
        } else {
            one.displayConnectionLevel(0);
            two.displayConnectionLevel(0);
            three.displayConnectionLevel(0);
            four.displayConnectionLevel(0);
            five.displayConnectionLevel(0);
        }
    }


}
