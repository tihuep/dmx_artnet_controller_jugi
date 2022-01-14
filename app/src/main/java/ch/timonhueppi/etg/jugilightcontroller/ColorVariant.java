package ch.timonhueppi.etg.jugilightcontroller;

/**
 * @author Timon Hüppi @tihuep
 * @version 1.0
 * @since 2022/01/15
 */
public class ColorVariant {

    private String name;
    private int brightness;
    private int hsv;

    public ColorVariant(String name, int brightness, int hsv) {
        this.name = name;
        this.brightness = brightness;
        this.hsv = hsv;
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

    public int getHsv() {
        return hsv;
    }

    public void setHsv(int hsv) {
        this.hsv = hsv;
    }
}
