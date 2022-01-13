package ch.timonhueppi.etg.jugilightcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;

public class MainActivity extends AppCompatActivity {

    SeekBar colorBrightness;
    ColorPickerView colorPicker;
    SeekBar lightBrightness;
    SeekBar lightTemp;

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
    }

    private void setColor(int[] rgb, int brightness){
        dmxData[0] = (byte) (brightness * 1.27);
        dmxData[1] = (byte) Math.floor(rgb[1]/2);
        dmxData[2] = (byte) Math.floor(rgb[2]/2);
        dmxData[3] = (byte) Math.floor(rgb[3]/2);

        new DMXSender().execute(dmxData);
    }

    private void setLight(int temp, int brightness){
        double brightnessFactor = brightness * 0.01;
        double tempMultiplied = temp * 1.27;
        byte cw = (byte) Math.floor(tempMultiplied * brightnessFactor);
        byte ww = (byte) Math.floor((127 - tempMultiplied) * brightnessFactor);
        dmxData[4] = cw;
        dmxData[5] = ww;

        new DMXSender().execute(dmxData);
    }
}