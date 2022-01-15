package ch.timonhueppi.etg.jugilightcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;

/**
 * @author Timon Hüppi @tihuep
 * @version 1.0
 * @since 2022/01/15
 */
public class MainActivity extends AppCompatActivity {

    private final String SHARED_PREFS = "DMX";

    /**
     * UI elements
     */
    SeekBar colorBrightness;
    ColorPickerView colorPicker;
    SeekBar lightBrightness;
    SeekBar lightTemp;
    Button colorVariantSave;
    EditText colorVariantName;
    LinearLayout colorVariantContainer;
    Button whiteVariantSave;
    EditText whiteVariantName;
    LinearLayout whiteVariantContainer;
    Button buttonSettings;


    /**
     * 512 DMX channels
     */
    byte[] dmxData;

    /**
     * Here is where the magic of this activity happens
     *
     * @param savedInstanceState idk
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creates 512 empty DMX channels (0)
        dmxData = new byte[512];

        //Loads UI elements to Java variables
        colorBrightness = findViewById(R.id.color_brightness);
        colorPicker = findViewById(R.id.color_picker);
        lightBrightness = findViewById(R.id.light_brightness);
        lightTemp = findViewById(R.id.light_temp);
        colorVariantSave = findViewById(R.id.color_variant_save);
        colorVariantName = findViewById(R.id.color_variant_name);
        colorVariantContainer = findViewById(R.id.color_variant_container);
        whiteVariantSave = findViewById(R.id.white_variant_save);
        whiteVariantName = findViewById(R.id.white_variant_name);
        whiteVariantContainer = findViewById(R.id.white_variant_container);
        buttonSettings = findViewById(R.id.button_settings);

        //Sets all handlers of sliders, color pickers and buttons
        setHandlers();

        //Loads and renders saved variants
        VariantModel.loadVariants(getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE));
        renderColorVariants();
        renderWhiteVariants();
    }

    /**
     * Sets all Handlers of Sliders, ColorPickers and Buttons
     */
    private void setHandlers(){
        MainActivity context = this;

        //Handler of the color-brightness slider
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

        //Handler of the color picker
        colorPicker.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(int color, boolean fromUser) {
                setColor(new ColorEnvelope(color).getArgb(), colorBrightness.getProgress());
            }
        });

        //Handler of the white-light-brightness slider
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

        //Handler of the white-light-temperature slider
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

        //Handler of the color variant save button
        colorVariantSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checks if name is empty
                if (!colorVariantName.getText().toString().equals("")){
                    //Checks if name already exists
                    for (ColorVariant colorVariant : VariantModel.colorVariants) {
                        if (colorVariant.getName().equals(colorVariantName.getText().toString().toUpperCase())){
                            return;
                        }
                    }
                    //Adds new variant to list
                    VariantModel.colorVariants.add(
                            new ColorVariant(
                                    colorVariantName.getText().toString().toUpperCase(),
                                    colorBrightness.getProgress(),
                                    colorPicker.getColor()
                            )
                    );
                    //Saves and rerenders variants
                    VariantModel.saveVariants(getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE));
                    renderColorVariants();
                }
            }
        });

        //Handler of the white variant save button
        whiteVariantSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checks if name is empty
                if (!whiteVariantName.getText().toString().equals("")){
                    //Checks if name already exists
                    for (WhiteVariant whiteVariant : VariantModel.whiteVariants) {
                        if (whiteVariant.getName().equals(whiteVariantName.getText().toString().toUpperCase())){
                            return;
                        }
                    }
                    //Adds new variant to list
                    VariantModel.whiteVariants.add(
                            new WhiteVariant(
                                    whiteVariantName.getText().toString().toUpperCase(),
                                    lightBrightness.getProgress(),
                                    lightTemp.getProgress()
                            )
                    );
                    //Saves and rerenders variants
                    VariantModel.saveVariants(getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE));
                    renderWhiteVariants();
                }
            }
        });

        //Handler of the settings-button
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Sends new values of all color-light channels to DMX
     *
     * @param rgb Picked color (array of ints)
     * @param brightness Picked brightness of light
     */
    private void setColor(int[] rgb, int brightness){
        //Instantiates sharedprefs to read the specific DMX channels to send the values to
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        //Reads ip from sharedprefs and refreshes it in DMXSender
        getIP();

        //Calculates and consolidates values to a DMX byte-array
        dmxData[sharedPreferences.getInt("rgbdimmer_dmx", 1) - 1] = (byte) (brightness * 1.27);
        dmxData[sharedPreferences.getInt("rgbred_dmx", 1) - 1] = (byte) Math.floor(rgb[1]/2);
        dmxData[sharedPreferences.getInt("rgbgreen_dmx", 1) - 1] = (byte) Math.floor(rgb[2]/2);
        dmxData[sharedPreferences.getInt("rgbblue_dmx", 1) - 1] = (byte) Math.floor(rgb[3]/2);

        //Sends DMX data
        new DMXSender().execute(dmxData);
    }

    /**
     * Sends new values of all white-light channels to DMX
     *
     * @param temp Picked color temperature (value 0-100; warm-cold)
     * @param brightness Picked brightness of light
     */
    private void setLight(int temp, int brightness){
        //Instantiates sharedprefs to read the specific DMX channels to send the values to
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        //Reads ip from sharedprefs and refreshes it in DMXSender
        getIP();

        //Calculates and consolidates values to a DMX byte-array
        double brightnessFactor = brightness * 0.01;
        double tempMultiplied = temp * 1.27;
        dmxData[sharedPreferences.getInt("ww_dmx", 1) - 1] = (byte) Math.floor(tempMultiplied * brightnessFactor);
        dmxData[sharedPreferences.getInt("cw_dmx", 1) - 1] = (byte) Math.floor((127 - tempMultiplied) * brightnessFactor);

        //Sends DMX data
        new DMXSender().execute(dmxData);
    }

    /**
     * Reads ip from sharedprefs and refreshes it in DMXSender
     */
    private void getIP(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        DMXSender.IPAddress = sharedPreferences.getString("ip_address", "127.0.0.1");
    }

    /**
     * Renders all color variants that are saved
     */
    private void renderColorVariants(){
        //Clears already rendered variants
        colorVariantContainer.removeAllViews();
        for (ColorVariant colorVariant : VariantModel.colorVariants) {
            //Instantiates new button and sets properties and handlers
            Button variantButton = new Button(this);
            variantButton.setText(colorVariant.getName());
            variantButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Loads saved values to UI elements
                    colorVariantName.setText(colorVariant.getName());
                    colorBrightness.setProgress(colorVariant.getBrightness());
                    try {
                        colorPicker.selectByHsvColor(colorVariant.getHsv());
                    }catch (IllegalAccessException e){
                        //Very nice error handling
                    }
                }
            });
            variantButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //Removes long-pressed variant
                    VariantModel.colorVariants.remove(colorVariant);
                    VariantModel.saveVariants(getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE));
                    renderColorVariants();
                    return true;
                }
            });
            //Adds new variant-button to scroll view
            colorVariantContainer.addView(variantButton);
        }
    }

    /**
     * Renders all white variants that are saved
     */
    private void renderWhiteVariants(){
        //Clears already rendered variants
        whiteVariantContainer.removeAllViews();
        for (WhiteVariant whiteVariant : VariantModel.whiteVariants) {
            //Instantiates new button and sets properties and handlers
            Button variantButton = new Button(this);
            variantButton.setText(whiteVariant.getName());
            variantButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Loads saved values to UI elements
                    whiteVariantName.setText(whiteVariant.getName());
                    lightBrightness.setProgress(whiteVariant.getBrightness());
                    lightTemp.setProgress(whiteVariant.getTemperature());
                }
            });
            variantButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //Removes long-pressed variant
                    VariantModel.whiteVariants.remove(whiteVariant);
                    VariantModel.saveVariants(getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE));
                    renderWhiteVariants();
                    return true;
                }
            });
            //Adds new variant-button to scroll view
            whiteVariantContainer.addView(variantButton);
        }
    }
}