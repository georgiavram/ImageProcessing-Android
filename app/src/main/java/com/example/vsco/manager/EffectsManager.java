package com.example.vsco.manager;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.vsco.adapter.EffectsAdapter;
import com.example.vsco.editing.Effect;
import com.example.vsco.editing.Processing;
import com.zomato.photofilters.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.CvType.CV_8UC4;

public final class EffectsManager {
    private static List<Effect> filterThumbs = new ArrayList<Effect>(10);
    private static List<Effect> processedThumbs = new ArrayList<Effect>(10);

    private EffectsManager() {
    }

    public static void addThumb(Effect thumbnailItem) {
        filterThumbs.add(thumbnailItem);
    }

    public static List<Effect> processThumbs(Context context) {
        for (Effect thumb : filterThumbs) {
            float size = context.getResources().getDimension(R.dimen.thumbnail_size);
            thumb.image = Bitmap.createScaledBitmap(thumb.image, (int) size, (int) size, false);
            Mat m = thumb.process(thumb.filterName, thumb.image);
            Utils.matToBitmap(m, thumb.image);
            processedThumbs.add(thumb);
        }
        return processedThumbs;
    }

    public static void clearThumbs() {
        filterThumbs = new ArrayList<>();
        processedThumbs = new ArrayList<>();
    }

}
