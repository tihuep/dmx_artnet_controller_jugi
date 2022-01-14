package ch.timonhueppi.etg.jugilightcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Timon Hüppi @tihuep
 * @version 1.0
 * @since 2022/01/15
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * UI elements
     */
    EditText ipAddress;
    EditText rgbdimmerDmx;
    EditText rgbredDmx;
    EditText rgbgreenDmx;
    EditText rgbblueDmx;
    EditText wwDmx;
    EditText cwDmx;
    Button buttonSave;
    TextView error_message;

    /**
     * Here is where the magic of this activity happens
     *
     * @param savedInstanceState idk
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Loads UI elements
        ipAddress = findViewById(R.id.ip_address);
        rgbdimmerDmx = findViewById(R.id.rgbdimmer_dmx);
        rgbredDmx = findViewById(R.id.rgbred_dmx);
        rgbgreenDmx = findViewById(R.id.rgbgreen_dmx);
        rgbblueDmx = findViewById(R.id.rgbblue_dmx);
        wwDmx = findViewById(R.id.ww_dmx);
        cwDmx = findViewById(R.id.cw_dmx);
        buttonSave = findViewById(R.id.button_save);
        error_message = findViewById(R.id.error_message);

        //Loads data from sharedprefs and fills input fields with current values
        populateFieldsWithData();

        //Save button handler
        SettingsActivity context = this;
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checks if values entered are valid
                if (validateFields()){
                    //Hides error message
                    error_message.setVisibility(View.INVISIBLE);

                    //Writes values entered to sharedprefs
                    SharedPreferences sharedPreferences = getSharedPreferences("DMX", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ip_address", ipAddress.getText().toString());
                    editor.putInt("rgbdimmer_dmx", Integer.parseInt(rgbdimmerDmx.getText().toString()));
                    editor.putInt("rgbred_dmx", Integer.parseInt(rgbredDmx.getText().toString()));
                    editor.putInt("rgbgreen_dmx", Integer.parseInt(rgbgreenDmx.getText().toString()));
                    editor.putInt("rgbblue_dmx", Integer.parseInt(rgbblueDmx.getText().toString()));
                    editor.putInt("ww_dmx", Integer.parseInt(wwDmx.getText().toString()));
                    editor.putInt("cw_dmx", Integer.parseInt(cwDmx.getText().toString()));
                    editor.apply();

                    //Exits activity and returns to main activity
                    context.finish();
                }else{
                    //Displays error message
                    error_message.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Loads data from sharedprefs and fills input fields with current values
     */
    private void populateFieldsWithData(){
        SharedPreferences sharedPreferences = getSharedPreferences("DMX", Context.MODE_PRIVATE);

        ipAddress.setText(sharedPreferences.getString("ip_address", "127.0.0.1"));
        rgbdimmerDmx.setText(String.valueOf(sharedPreferences.getInt("rgbdimmer_dmx", 0)));
        rgbredDmx.setText(String.valueOf(sharedPreferences.getInt("rgbred_dmx", 0)));
        rgbgreenDmx.setText(String.valueOf(sharedPreferences.getInt("rgbgreen_dmx", 0)));
        rgbblueDmx.setText(String.valueOf(sharedPreferences.getInt("rgbblue_dmx", 0)));
        wwDmx.setText(String.valueOf(sharedPreferences.getInt("ww_dmx", 0)));
        cwDmx.setText(String.valueOf(sharedPreferences.getInt("cw_dmx", 0)));
    }

    /**
     * Checks if all data entered into input fields is valid. Because as we all know: Never trust user input. Very wise
     *
     * @return Boolean: data valid/data not valid
     */
    private boolean validateFields(){
        //Some regex magic I found on stackoverflow
        if (!ipAddress.getText().toString().matches("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$")){
            return false;
        }else if (!rgbdimmerDmx.getText().toString().matches("^[0-9]{1,3}$")){
            return false;
        } else if (!rgbredDmx.getText().toString().matches("^[0-9]{1,3}$")) {
            return false;
        }else if (!rgbgreenDmx.getText().toString().matches("^[0-9]{1,3}$")){
            return false;
        }else if (!rgbblueDmx.getText().toString().matches("^[0-9]{1,3}$")){
            return false;
        }else if (!wwDmx.getText().toString().matches("^[0-9]{1,3}$")){
            return false;
        }else if (!cwDmx.getText().toString().matches("^[0-9]{1,3}$")){
            return false;
        }
        return true;
    }
}