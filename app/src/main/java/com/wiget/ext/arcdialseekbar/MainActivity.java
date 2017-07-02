package com.wiget.ext.arcdialseekbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ArcDialSeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = (ArcDialSeekBar) findViewById(R.id.clock_view_3);
        seekBar.setGradientColors(new int[]{Color.GREEN,Color.RED});
    }


}
