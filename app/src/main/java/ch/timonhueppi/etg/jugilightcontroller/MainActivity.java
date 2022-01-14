package ch.timonhueppi.etg.jugilightcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;

public class MainActivity extends AppCompatActivity {

    SeekBar colorBrightness;
    ColorPickerView colorPicker;
    SeekBar lightBrightness;
    SeekBar lightTemp;
    Button buttonSettings;

    byte[] dmxData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dmxData = new byte[512];

        colorBrightness = findViewById(R.id.color_brightness);
        colorPicker = findViewById(R.id.color_picker);
        lightBrightness = findViewById(R.id.light_brightness);
        lightTemp = findViewById(R.id.light_temp);
        buttonSettings = findViewById(R.id.button_settings);

        MainActivity context = this;

        colorBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setColor(new ColorEnvelope(colorPicker.getColor()).getArgb(), i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        colorPicker.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(int color, boolean fromUser) {
                setColor(new ColorEnvelope(color).getArgb(), colorBrightness.getProgress());
            }
        });

        lightBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setLight(lightTemp.getProgress(), i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        lightTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setLight(i, lightBrightness.getProgress());
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setColor(int[] rgb, int brightness){
        getIP();
        dmxData[2] = (byte) (brightness * 1.27);
        dmxData[3] = (byte) Math.floor(rgb[1]/2);
        dmxData[4] = (byte) Math.floor(rgb[2]/2);
        dmxData[5] = (byte) Math.floor(rgb[3]/2);

        new DMXSender().execute(dmxData);
    }

    private void setLight(int temp, int brightness){
        getIP();
        double brightnessFactor = brightness * 0.01;
        double tempMultiplied = temp * 1.27;
        dmxData[0] = (byte) Math.floor(tempMultiplied * brightnessFactor);
        dmxData[1] = (byte) Math.floor((127 - tempMultiplied) * brightnessFactor);

        new DMXSender().execute(dmxData);
    }

    private void getIP(){
        SharedPreferences sharedPreferences = getSharedPreferences("DMX", Context.MODE_PRIVATE);
        DMXSender.IPAddress = sharedPreferences.getString("ip_address", "127.0.0.1");
    }
}