package ch.timonhueppi.etg.jugilightcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    SeekBar number1;
    EditText number2;
    EditText number3;
    EditText number4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);

        number2.setText("0");
        number3.setText("0");
        number4.setText("0");

        number1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                System.out.println("onprogresschanged " + i);

                byte dmx_brightness = (byte) (i * 1.27);
                byte dmx_red = Byte.valueOf(number2.getText().toString());
                byte dmx_green = Byte.valueOf(number3.getText().toString());
                byte dmx_blue = Byte.valueOf(number4.getText().toString());

                byte[] dmxData = new byte[512];
                dmxData[0] = dmx_brightness;
                dmxData[1] = dmx_red;
                dmxData[2] = dmx_green;
                dmxData[3] = dmx_blue;

                new DMXSender().execute(dmxData);
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
    }
}