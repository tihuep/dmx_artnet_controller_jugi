package ch.timonhueppi.etg.jugilightcontroller;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;

import ch.timonhueppi.etg.jugilightcontroller.model.ColorVariant;
import ch.timonhueppi.etg.jugilightcontroller.model.VariantModel;
import ch.timonhueppi.etg.jugilightcontroller.model.WhiteVariant;
import ch.timonhueppi.etg.jugilightcontroller.util.DmxLightValuesController;

/**
 * @author Timon Hüppi @tihuep
 * @author Bastian Kappeler @bastkapp
 * @version 1.0
 * @since 2022/04/08
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

    DmxLightValuesController dmx;

    /**
     * {@inheritDoc}
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DMX data
        dmx = new DmxLightValuesController(this);

        initializeUiElements();

        initializeHandlers();

        loadAndRenderVariants();
    }

    /**
     * Loads and renders the saved variants from the shared preferences
     */
    private void loadAndRenderVariants() {
        VariantModel.loadVariants(getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE));
        renderColorVariants();
        renderWhiteVariants();
    }

    /**
     * Initializes the UI elements
     */
    private void initializeUiElements() {
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
    }

    /**
     * Initializes the handlers
     */
    private void initializeHandlers() {

        // Color-brightness slider handler
        initializeColorBrightnessHandler();

        // Color picker handler
        initializeColorPickerHandler();

        // White-light-brightness slider handler
        initializeWhiteLightBrightnessHandler();

        // White-light-temperature slider handler
        InitializeWhiteLightTemperatureHandler();

        // Color variant save button handler
        initializeColorVariantSaveHandler();

        // White variant save button handler
        initializeWhiteVariantHandler();

        // Settings-button handler
        initializeSettingsButtonHandler();
    }

    /**
     * Renders all color variants that are saved
     * <p>
     * Buttons won't be rendered if the android version is lower than Nougat
     */
    private void renderColorVariants() {
        //Clears already rendered variants
        colorVariantContainer.removeAllViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            VariantModel.colorVariants.forEach(colorVariant -> colorVariantContainer.addView(createColorVariantButton(colorVariant)));
        }
    }

    /**
     * Renders all white variants that are saved
     * <p>
     * Buttons won't be rendered if the android version is lower than Nougat
     */
    private void renderWhiteVariants() {
        //Clears already rendered variants
        whiteVariantContainer.removeAllViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            VariantModel.whiteVariants.forEach(whiteVariant -> whiteVariantContainer.addView(createWhiteVariantButton(whiteVariant)));
        }
    }

    /**
     * Initializes a new color variant button with the specific handler
     *
     * @param colorVariant color variant to be rendered
     * @return a button that represents the color variant
     */
    @NonNull
    private Button createColorVariantButton(ColorVariant colorVariant) {
        Button variantButton = new Button(this);
        variantButton.setText(colorVariant.getName());

        variantButton.setOnClickListener(view -> {
            // Loads values to UI elements
            colorVariantName.setText(colorVariant.getName());
            colorBrightness.setProgress(colorVariant.getBrightness());

            try {
                colorPicker.selectByHsvColor(colorVariant.getHsv());
            } catch (IllegalAccessException e) {
                Log.e("ERROR", "Failed to select color", e);
            }
        });

        variantButton.setOnLongClickListener(view -> {
            // Removes variant
            VariantModel.colorVariants.remove(colorVariant);
            VariantModel.saveVariants(getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE));
            renderColorVariants();
            return true;
        });

        return variantButton;
    }

    @NonNull
    private Button createWhiteVariantButton(WhiteVariant whiteVariant) {
        Button variantButton = new Button(this);
        variantButton.setText(whiteVariant.getName());

        variantButton.setOnClickListener(view -> {
            //Loads saved values to UI elements
            whiteVariantName.setText(whiteVariant.getName());
            lightBrightness.setProgress(whiteVariant.getBrightness());
            lightTemp.setProgress(whiteVariant.getTemperature());
        });

        variantButton.setOnLongClickListener(view -> {
            //Removes long-pressed variant
            VariantModel.whiteVariants.remove(whiteVariant);
            VariantModel.saveVariants(getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE));
            renderWhiteVariants();
            return true;
        });
        return variantButton;
    }

    /**
     * Initializes the color-brightness slider handler
     */
    private void initializeColorBrightnessHandler() {
        colorBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                dmx.setColor(new ColorEnvelope(colorPicker.getColor()).getArgb(), i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * Initializes the color picker handler
     */
    private void initializeColorPickerHandler() {
        colorPicker.setColorListener((ColorListener) (color, fromUser) ->
                dmx.setColor(new ColorEnvelope(color).getArgb(), colorBrightness.getProgress())
        );
    }

    /**
     * Initializes the white-light-brightness slider handler
     */
    private void initializeWhiteLightBrightnessHandler() {
        lightBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                dmx.setLight(lightTemp.getProgress(), i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * Initializes the white-light-temperature slider handler
     */
    private void InitializeWhiteLightTemperatureHandler() {
        lightTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                dmx.setLight(i, lightBrightness.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * Initializes the color variant save button handler
     */
    private void initializeColorVariantSaveHandler() {
        colorVariantSave.setOnClickListener(view -> {
            if (colorVariantName.getText().toString().isEmpty()) return;

            // Checks if name already exists
            for (ColorVariant colorVariant : VariantModel.colorVariants)
                if (colorVariant.getName().equals(colorVariantName.getText().toString().toUpperCase()))
                    return;

            // Adds new variant to list
            VariantModel.colorVariants.add(
                    new ColorVariant(
                            colorVariantName.getText().toString().toUpperCase(),
                            colorBrightness.getProgress(),
                            colorPicker.getColor()
                    ));

            // Saves and rerenders variants
            VariantModel.saveVariants(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE));
            renderColorVariants();
        });
    }

    /**
     * Initializes the white variant save button handler
     */
    private void initializeWhiteVariantHandler() {
        whiteVariantSave.setOnClickListener(view -> {
            if (whiteVariantName.getText().toString().isEmpty()) return;

            // Checks if name already exists
            for (WhiteVariant whiteVariant : VariantModel.whiteVariants)
                if (whiteVariant.getName().equals(whiteVariantName.getText().toString().toUpperCase()))
                    return;

            // Adds new variant to list
            VariantModel.whiteVariants.add(
                    new WhiteVariant(
                            whiteVariantName.getText().toString().toUpperCase(),
                            lightBrightness.getProgress(),
                            lightTemp.getProgress()
                    ));

            // Saves and rerenders variants
            VariantModel.saveVariants(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE));
            renderWhiteVariants();
        });
    }

    /**
     * Initializes the color variant delete button handler
     */
    private void initializeSettingsButtonHandler() {
        buttonSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}