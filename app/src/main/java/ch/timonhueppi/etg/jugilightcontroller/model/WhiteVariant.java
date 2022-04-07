package ch.timonhueppi.etg.jugilightcontroller.model;

/**
 * @author Timon Hüppi @tihuep
 * @version 1.0
 * @since 2022/01/15
 */
public class WhiteVariant {

    private String name;
    private int brightness;
    private int temperature;

    public WhiteVariant(String name, int brightness, int temperature) {
        this.name = name;
        this.brightness = brightness;
        this.temperature = temperature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
