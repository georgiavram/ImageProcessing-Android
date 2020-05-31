package com.example.vsco.editing;


import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Effect {
    public String filterName;
    public Bitmap image;

    public Effect() {
        image = null;
    }

    public Mat process(String filterName, Bitmap bmp){
        Mat input = new Mat(bmp.getHeight(), bmp.getWidth(), CvType.CV_8UC3);
        Utils.bitmapToMat(bmp, input);
        switch (filterName){
            case "Grayscale":
                    Processing.grayscaleFilter(input.nativeObj);
                break;
            case "Sepia":
                Processing.sepiaFilter(input.nativeObj);
                break;
            case "Sketch":
                Processing.sketchFilter(input.nativeObj);
                break;
            case "Negative":
                Processing.negativeFilter(input.nativeObj);
                break;
            case "Equalize":
                Processing.equalizeFilter(input.nativeObj);
                break;
            case "M5":
                Processing.M5Filter(input.nativeObj);
                break;
            case "HB1":
                Processing.HB1Filter(input.nativeObj);
                break;
            case "X1":
                Processing.X1Filter(input.nativeObj);
                break;
            case "C1":
                Processing.C1Filter(input.nativeObj);
                break;
            case "KU8":
                Processing.KU8Filter(input.nativeObj);
                break;
            case "P5":
                Processing.P5Filter(input.nativeObj);
                break;
            case "Canny":
                Processing.cannyFilter(input.nativeObj);
                break;
            default:
                break;
        }
        return input;
    }
}
