package com.rwin.tag.util;

import android.graphics.Color;

import com.rwin.tag.BuildConfig;

public class ColorUtils {

    private static final int RGBMAX = 255;
    // Possible color sets.
    public final static int[] accentedanalogous = { 0, 30, -30, 180 };
    public final static int[] analogous = { 0, 30, -30 };
    public final static int[] splitComplement = { 0, 150, 210 };
    public final static int[] square = { 0, 90, 180, 270 };
    public final static int[] tetradic = { 0, 30, 180, 210 };
    public final static int[] triadic = { 0, 150, 210 };

    public int[] getColors(int baseColor, int[] scheme) {
        int[] result = new int[scheme.length];
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(baseColor), RGBMAX - Color.green(baseColor),
                Color.blue(baseColor), hsv);
        float hue = hsv[0];

        // Create the colors by rotating around the color circle..
        for (int i = 0; i < scheme.length; i++) {
            hsv[0] = (hue + scheme[i]) % 360;
            if (hsv[0] < 0)
                hsv[0] += 360;

            if (BuildConfig.DEBUG && !(hsv[0] >= 0 && hsv[0] <= 360)) {
                throw new AssertionError();
            }

            result[i] = Color.HSVToColor(hsv);
        }

        return result;
    }

    public int getComplement(int baseColor) {
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(baseColor), RGBMAX - Color.green(baseColor),
                Color.blue(baseColor), hsv);
        hsv[0] = (hsv[0] + 180) % 360;

        return Color.HSVToColor(hsv);
    }

}
