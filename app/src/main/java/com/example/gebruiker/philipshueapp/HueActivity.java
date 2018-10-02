package com.example.gebruiker.philipshueapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class HueActivity extends AppCompatActivity {

    private String url = Config.API_URL;

    private TextView textView;
    private Switch onSwitch;
    private HueManager manager;
    private SeekBar hueBar;
    private SeekBar satBar;
    private SeekBar briBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue);

        textView = findViewById(R.id.textView);
        onSwitch = findViewById(R.id.onSwitch);
        hueBar = findViewById(R.id.hueBar);
        satBar = findViewById(R.id.satBar);
        briBar = findViewById(R.id.briBar);

        manager = manager.getInstance(this, null);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        Light light = (Light) bundle.getSerializable("LIGHT");
        textView.setText(light.toString());

        url = Config.API_URL + "/" + light.getId() + "/state";

        onSwitch.setChecked(light.isOn());

        onSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                manager.switchOn(url, b);
            }
        });

        hueBar.setProgress(light.getHue());

        hueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                manager.putHue(url, seekBar.getProgress());
            }
        });

        satBar.setProgress(light.getSaturation());

        satBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                manager.putSaturation(url, seekBar.getProgress());
            }
        });

        briBar.setProgress(light.getBrightness());

        briBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                manager.putBrightness(url, seekBar.getProgress());
            }
        });

    }
}
