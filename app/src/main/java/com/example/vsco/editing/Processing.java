package com.example.vsco.editing;

public class Processing {
    public static native void grayscaleFilter(long intput);
    public static native void sepiaFilter(long input);
    public static native void sketchFilter(long input);
    public static native void negativeFilter(long input);
    public static native void equalizeFilter(long input);
    public static native void M5Filter(long input);
    public static native void HB1Filter(long input);
    public static native void X1Filter(long input);
    public static native void C1Filter(long input);
    public static native void KU8Filter(long input);
    public static native void P5Filter(long input);
    public static native void cannyFilter(long input);

    public static native void brightnessFilter(long in, int brightness);
    public static native void contrastFilter(long in, float contrast);
    public static native void saturationFilter(long in, float saturation);
    public static native void blurFilter(long in, int blur);
    public static native void vignetteFilter(long in, float radius);
    public static native void wbFilter(long in, int wb, int tint);
    public static native void rgbFilter(long in, int red, int green, int blue);
    public static native void flipVertically(long in);
    public static native void splitFilter(long in);
    public static native void crop(long in, long out, int width, int height);
    public static native void resize(long in, long out, int width, int height);
    public static native void rotate(long in, long out);
}
