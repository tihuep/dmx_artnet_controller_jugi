package ch.timonhueppi.etg.jugilightcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;

public class MainActivity extends AppCompatActivity {

    SeekBar number1;
    EditText number2;
    EditText number3;
    EditText number4;
    ColorPickerView colorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number1 = findViewById(R.id.number1);
        colorPicker = findViewById(R.id.colorPicker);

        number2.setText("0");
        number3.setText("0");
        number4.setText("0");

        number1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                System.out.println("onprogresschanged " + i);

                setColor(new ColorEnvelope(colorPicker.getColor()).getArgb(), i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("onstarttrackingtouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println("onstoptrackingtouch");
            }
        });

        colorPicker.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(int color, boolean fromUser) {
                System.out.println(color);

                setColor(new ColorEnvelope(color).getArgb(), number1.getProgress());
            }
        });
    }

    private void setColor(int[] rgb, int brightness){
        byte[] dmxData = new byte[512];
        dmxData[0] = (byte) (brightness * 1.27);
        dmxData[1] = (byte) Math.floor(rgb[1]/2);
        dmxData[2] = (byte) Math.floor(rgb[2]/2);
        dmxData[3] = (byte) Math.floor(rgb[3]/2);

        new DMXSender().execute(dmxData);
    }
}