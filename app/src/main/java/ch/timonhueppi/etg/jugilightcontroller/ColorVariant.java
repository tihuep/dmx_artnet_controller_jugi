package ch.timonhueppi.etg.jugilightcontroller;

public class ColorVariant {

    private String name;
    private byte brightness;
    private byte red;
    private byte green;
    private byte blue;
    private int hsv;

    public ColorVariant(String name, byte brightness, byte red, byte green, byte blue, int hsv) {
        this.name = name;
        this.brightness = brightness;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.hsv = hsv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getBrightness() {
        return brightness;
    }

    public void setBrightness(byte brightness) {
        this.brightness = brightness;
    }

    public byte getRed() {
        return red;
    }

    public void setRed(byte red) {
        this.red = red;
    }

    public byte getGreen() {
        return green;
    }

    public void setGreen(byte green) {
        this.green = green;
    }

    public byte getBlue() {
        return blue;
    }

    public void setBlue(byte blue) {
        this.blue = blue;
    }

    public int getHsv() {
        return hsv;
    }

    public void setHsv(int hsv) {
        this.hsv = hsv;
    }
}
