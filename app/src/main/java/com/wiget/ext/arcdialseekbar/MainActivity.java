package com.wiget.ext.arcdialseekbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ArcDialSeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = (ArcDialSeekBar) findViewById(R.id.arc_sb_view_1);
        //seekBar.setGradientColors(new int[]{Color.GREEN,Color.RED});

    }


}
