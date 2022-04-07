package ch.timonhueppi.etg.jugilightcontroller.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * General DMX controller.
 * Saves local DMX values and calls the DMX sender.
 *
 * @author Bastian Kappeler @bastkapp
 * @version 1.0
 * @since 2022/04/07
 */
public class DmxLightValuesController {

    private final byte[] data;
    private final Context context;

    private int whiteTemperature;
    private int whiteBrightness;

    /**
     * Constructor
     *
     * @param context Context of the application
     */
    public DmxLightValuesController(Context context) {
        this.data = new byte[512];
        this.context = context;
    }

    /**
     * Sends the values of all light channels to DMX
     */
    public void update() {
        loadIP();
        new DMXSender().execute(getData());
    }

    /**
     * @return DMX-Channel-Data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Reads the IP-Address from the SharedPreferences and sets it in the DMX-Sender
     */
    private void loadIP() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        DMXSender.IPAddress = sharedPreferences.getString("ip_address", "127.0.0.1");
    }

    /**
     * Sets the RGB-Values and brightness of the Color-Light
     * <p>
     * calls {@link #update()}
     *
     * @param r          Picked red value
     * @param g          Picked green value
     * @param b          Picked blue value
     * @param brightness Picked brightness for the Color-Light
     */
    public void setColor(int r, int g, int b, int brightness) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        data[sharedPreferences.getInt("rgbBrightness_dmx", 1) - 1] = (byte) (brightness * 1.27);
        data[sharedPreferences.getInt("rgbred_dmx", 1) - 1] = (byte) Math.floor(r >> 1);
        data[sharedPreferences.getInt("rgbgreen_dmx", 1) - 1] = (byte) Math.floor(g >> 1);
        data[sharedPreferences.getInt("rgbblue_dmx", 1) - 1] = (byte) Math.floor(b >> 1);

        update();
    }

    /**
     * Sets the temperature and brightness of the White-Light
     * <p>
     * calls {@link #update()}
     *
     * @param rawTemperature Picked color temperature (value 0-100; warm-cold)
     * @param rawBrightness  Picked brightness of light
     */
    public void setLight(int rawTemperature, int rawBrightness) {
        SharedPreferences sharedPreferences = getSharedPreferences();

        this.whiteTemperature = rawTemperature;
        this.whiteBrightness = rawBrightness;

        final double brightness = rawBrightness * 0.01;
        final double temperature = rawTemperature * 1.27;

        data[sharedPreferences.getInt("ww_dmx", 1) - 1] = (byte) Math.floor(temperature * brightness);
        data[sharedPreferences.getInt("cw_dmx", 1) - 1] = (byte) Math.floor((127 - temperature) * brightness);

        update();
    }

    /**
     * Sets the RGB-Values and brightness of the Color-Light
     *
     * @param rgb        Picked color (array of ints)
     * @param brightness Picked brightness for the Color-Light
     */
    public void setColor(int[] rgb, int brightness) {
        setColor(rgb[0], rgb[1], rgb[2], brightness);
    }

    /**
     * Sets the RGB-Values of the Color-Light with 100% brightness
     *
     * @param r Picked red value
     * @param g Picked green value
     * @param b Picked blue value
     */
    public void setColor(int r, int g, int b) {
        setColor(r, g, b, 255);
    }

    /**
     * Sets the RGB-Values of the Color-Light with 100% brightness
     *
     * @param rgb Picked color (array of ints)
     */
    public void setColor(int[] rgb) {
        setColor(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Sets the RGB-Values of the Color-Light with 100% brightness
     *
     * @param rgb Picked color (same value for r, g, b)
     */
    public void setColor(int rgb) {
        setColor(rgb, rgb, rgb, 255);
    }

    /**
     * Sets the RGB-Values and brightness of the Color-Light
     *
     * @param rgb        Picked color (same value for r, g, b)
     * @param brightness Picked brightness for the Color-Light
     */
    public void setColor(int rgb, int brightness) {
        setColor(rgb, rgb, rgb, brightness);
    }

    /**
     * Sets the temperature of the White-Light
     *
     * @param rawTemperature Picked color temperature (value 0-100; warm-cold)
     */
    public void setTemperature(int rawTemperature) {
        setLight(rawTemperature, getWhiteBrightness());
    }

    /**
     * Gets the brightness of the Color-Light
     *
     * @return The current temperature of the Color-Light
     */
    public byte getRgbBrightness() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return data[sharedPreferences.getInt("rgbBrightness_dmx", 1) - 1];
    }

    /**
     * Sets the brightness of the Color-Light
     *
     * @param brightness Picked brightness for the RGB light
     */
    public void setRgbBrightness(int brightness) {
        setColor(getRgb(), brightness);
    }

    /**
     * Gets the Red-Value of the Color-Light
     *
     * @return The current Red-Value of the Color-Light
     */
    public byte getRed() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return data[sharedPreferences.getInt("rgbred_dmx", 1) - 1];
    }

    /**
     * Sets the Red-Value of the light
     *
     * @param r Picked red value
     */
    public void setRed(int r) {
        setColor(r, getGreen(), getBlue());
    }

    /**
     * Gets the Green-Value of the Color-Light
     *
     * @return The current Green-Value of the Color-Light
     */
    public byte getGreen() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return data[sharedPreferences.getInt("rgbgreen_dmx", 1) - 1];
    }

    /**
     * Sets the Green-Value of the Color-Light
     *
     * @param g Picked green value
     */
    public void setGreen(int g) {
        setColor(getRed(), g, getBlue());
    }

    /**
     * Gets the Blue-Value of the Color-Light
     *
     * @return The current Blue-Value of the Color-Light
     */
    public byte getBlue() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return data[sharedPreferences.getInt("rgbblue_dmx", 1) - 1];
    }

    /**
     * Sets the Blue-Value of the Color-Light
     *
     * @param b Picked blue value
     */
    public void setBlue(int b) {
        setColor(getRed(), getGreen(), b);
    }

    /**
     * Gets the temperature of the White-Light
     *
     * @return The current temperature of the White-Light
     */
    public int getWhiteTemperature() {
        return whiteTemperature;
    }

    /**
     * Gets the brightness of the White-Light
     *
     * @return The current brightness of the White-Light
     */
    public int getWhiteBrightness() {
        return whiteBrightness;
    }

    /**
     * Sets the brightness of the White-Light
     *
     * @param rawBrightness Picked brightness of light
     */
    public void setWhiteBrightness(int rawBrightness) {
        setLight(getWhiteTemperature(), rawBrightness);
    }

    /**
     * Gets the RGB-Values of the light
     *
     * @return The current RGB-Values of the light
     */
    public int[] getRgb() {
        return new int[]{
                getRed(),
                getBlue(),
                getGreen()
        };
    }

    /**
     * @return The DMX-Address of the Light-Channels
     */
    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("DMX", Context.MODE_PRIVATE);
    }
}
