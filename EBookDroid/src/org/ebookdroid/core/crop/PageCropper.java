package org.ebookdroid.core.crop;

import com.foobnix.android.utils.Dips;
import com.foobnix.android.utils.LOG;
import com.foobnix.pdf.info.wrapper.MagicHelper;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;

public class PageCropper {
    public static final int MAX_HEIGHT = Dips.DP_300;
    public static final int MAX_WIDTH = Dips.DP_200;

    public static RectF getCropBounds(Bitmap bitmap1, final Rect bitmapBounds, final RectF pageSliceBounds) {

        int f = bitmap1.getPixel(0, 0);
        int f2 = bitmap1.getPixel(bitmap1.getWidth() - 1, bitmap1.getHeight() - 1);

        int width = bitmap1.getWidth();
        int height = bitmap1.getHeight();

        int topY = height;
        int topX = width;
        int bottomY = 0;
        int bottomX = 0;

        int dx = Math.max(1, bitmap1.getHeight() / MAX_HEIGHT);
        int dy = Math.max(1, bitmap1.getWidth() / MAX_WIDTH);
        LOG.d("getCropBounds-dx-dy", dx, dy);

        for (int y = 0; y < height; y += dx) {
            for (int x = 0; x < width; x += dy) {
                int p = bitmap1.getPixel(x, y);
                if (p == Color.WHITE || p == f || p == f2) {
                    continue;
                }

                if (MagicHelper.isColorDarkSimple(p)) {
                    if (x < topX)
                        topX = x;
                    if (y < topY)
                        topY = y;
                    if (x > bottomX)
                        bottomX = x;
                    if (y > bottomY)
                        bottomY = y;
                } else {

                }
            }
        }

        LOG.d("getCropBounds", topX, topY, bottomX, bottomY, "WxH", width, height);

        if (topY == height) {
            topY = 0;
        }
        if (topX == width) {
            topX = 0;
        }

        if (bottomY == 0) {
            bottomY = height;
        }

        if (bottomX == 0) {
            bottomX = width;
        }

        float k = 0.02f;
        float left = Math.max(0, (float) topX / width - k);
        float top = Math.max(0, (float) topY / height - k);

        float right = Math.min(1, (float) bottomX / width + k);
        float bottom = Math.min(1, (float) bottomY / height + k);

        LOG.d("getCropBounds-crop", left, top, right, bottom);
        return new RectF(left * pageSliceBounds.width() + pageSliceBounds.left, top * pageSliceBounds.height() + pageSliceBounds.top, right * pageSliceBounds.width() + pageSliceBounds.left,
                bottom * pageSliceBounds.height() + pageSliceBounds.top);
    }

}
