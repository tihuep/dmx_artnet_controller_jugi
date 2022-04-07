package ch.timonhueppi.etg.jugilightcontroller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Timon Hüppi @tihuep
 * @author Bastian Kappeler @bastkapp
 * @version 1.0
 * @since 2022/04/08
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

        // Initializes UI elements
        initializeUiElements();

        populateFieldsWithData();

        saveButtonHandler();
    }

    /**
     * Initializes the UI elements
     */
    private void initializeUiElements() {
        ipAddress = findViewById(R.id.ip_address);
        rgbdimmerDmx = findViewById(R.id.rgbdimmer_dmx);
        rgbredDmx = findViewById(R.id.rgbred_dmx);
        rgbgreenDmx = findViewById(R.id.rgbgreen_dmx);
        rgbblueDmx = findViewById(R.id.rgbblue_dmx);
        wwDmx = findViewById(R.id.ww_dmx);
        cwDmx = findViewById(R.id.cw_dmx);
        buttonSave = findViewById(R.id.button_save);
        error_message = findViewById(R.id.error_message);
    }

    /**
     * Initializes the save button handler
     */
    private void saveButtonHandler() {
        buttonSave.setOnClickListener(view -> {
            if (!validFields()) {
                error_message.setVisibility(View.VISIBLE);
                return;
            } else
                error_message.setVisibility(View.INVISIBLE);

            saveSettingsValues(
                    ipAddress.getText().toString(),
                    Integer.parseInt(rgbdimmerDmx.getText().toString()),
                    Integer.parseInt(rgbredDmx.getText().toString()),
                    Integer.parseInt(rgbgreenDmx.getText().toString()),
                    Integer.parseInt(rgbblueDmx.getText().toString()),
                    Integer.parseInt(wwDmx.getText().toString()),
                    Integer.parseInt(cwDmx.getText().toString())
            );

            finish();
        });
    }

    private void saveSettingsValues(String ipAddress, int rgbdimmerDmx, int rgbredDmx, int rgbgreenDmx, int rgbblueDmx, int wwDmx, int cwDmx) {
        SharedPreferences sharedPreferences = getSharedPreferences("DMX", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("ip_address", ipAddress);
        editor.putInt("rgbdimmer_dmx", rgbdimmerDmx);
        editor.putInt("rgbred_dmx", rgbredDmx);
        editor.putInt("rgbgreen_dmx", rgbgreenDmx);
        editor.putInt("rgbblue_dmx", rgbblueDmx);
        editor.putInt("ww_dmx", wwDmx);
        editor.putInt("cw_dmx", cwDmx);
        editor.apply();
    }

    /**
     * Populates the fields with data from sharedprefs
     */
    private void populateFieldsWithData() {
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
     * @return fields are valid
     */
    private boolean validFields() {
        if (!ipAddress.getText().toString().matches("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$"))
            return false;

        if (!rgbdimmerDmx.getText().toString().matches("^[0-9]{1,3}$"))
            return false;

        if (!rgbredDmx.getText().toString().matches("^[0-9]{1,3}$"))
            return false;

        if (!rgbgreenDmx.getText().toString().matches("^[0-9]{1,3}$"))
            return false;

        if (!rgbblueDmx.getText().toString().matches("^[0-9]{1,3}$"))
            return false;

        if (!wwDmx.getText().toString().matches("^[0-9]{1,3}$"))
            return false;

        return cwDmx.getText().toString().matches("^[0-9]{1,3}$");

    }
}